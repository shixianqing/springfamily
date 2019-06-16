# spring security 流程梳理

## 自定义用户认证逻辑

   1. 处理用户信息获取逻辑
   
   2. 处理用户信息校验逻辑
   
   3. 处理密码加解密
   
### 处理用户信息获取逻辑

   spring security 中提供了一个接口UserDetailsService，允许我们自定义实现用户信息获取逻辑
       
       public interface UserDetailsService {
       
       	 UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
       }
    
   实现该接口，可以根据用户传来的用户名到数据库里查询用户信息
        
        @Service
        public class UserDetailServiceImpl implements UserDetailsService {
        
            @Autowired
            private UserMapper userMapper;
        
            @Autowired
            private BCryptPasswordEncoder encoder;
        
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userMapper.loadUserByUsername(username);
                if (user == null){
                    throw new UsernameNotFoundException(username+"用户不存在");
                }
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                org.springframework.security.core.userdetails.User userDetail =
                        new org.springframework.security.core.userdetails.User(username,user.getPassword(),authorities);
                return userDetail;
            }
        }


### 处理用户信息校验逻辑

   spring security 提供了接口UserDetails，默认实现为User
    
    public interface UserDetails extends Serializable {
        
        //权限集合
        Collection<? extends GrantedAuthority> getAuthorities();
    
        String getPassword();
    
        String getUsername();
    
        //账户是否过期
        boolean isAccountNonExpired();
    
        //账户是否被锁定
        boolean isAccountNonLocked();
    
        //密码是否过期
        boolean isCredentialsNonExpired();
    
        //账户是否可用
        boolean isEnabled();
    }
    
### 处理密码加解密
    
   用户密码通常是以密文形式存在数据库中，通过查询数据库方式，处理用户信息认证，spring security默认使用DaoAuthenticationProvider，将加密对象放到DaoAuthenticationProvider，
        
        @Bean
        public AuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
            provider.setPasswordEncoder(passwordEncoder());
            provider.setUserDetailsService(userDetailsService);
            return provider;
        }
    
        @Bean
        public PasswordEncoder passwordEncoder() {
    
            return new BCryptPasswordEncoder();
        }

## 个性化用户认证流程

1. 自定义登录页面
2. 自定义登录成功处理
3. 自定义登录失败处理

### 自定义登录页面
spring security 有自己一套默认的登录页面，在实际企业开发中，我们需要使用自定义的登录页面。
<br>步骤：

1. 创建一个实体类，继承<span style="color:red; font-weight: bold">WebSecurityConfigurerAdapter</span>,重写<span style="text-decoration:underline;color:red">void configure(HttpSecurity http) throws Exception</span>方法
2. 设置HttpSecurity 

		@Override
	    protected void configure(HttpSecurity http) throws Exception {
	        		http
	                .formLogin()
	                .loginPage("/page/login.html")
	                .and()
	                .csrf()
	                .disable();
	    }

### 自定义登录成功处理

目前很多项目都是前后端分离的，所以当登录成功后，不能直接重定向到某一个页面，需要将用户信息或者服务端生成的token返给前端。<br>
实现<span style="color:red; font-weight: bold">AuthenticationSuccessHandler</span>接口，重写<span style="color:red; font-weight: bold">onAuthenticationSuccess</span>方法

		/**
		 * @Author:shixianqing
		 * @Date:2019/5/27 10:14
		 * @Description: 授权成功处理器
		 **/
		public class CustomAuthSuccessHanlder implements AuthenticationSuccessHandler {
		    @Override
		    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		                                        Authentication authentication) throws IOException, ServletException {
		        //设置token，返回给前端
		        String s = JSONObject.toJSONString(MetaResponse.success());
		        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		        response.getOutputStream().write(s.getBytes());
		    }
		}
		

<span style="color:red; font-weight: bold">AuthenticationSuccessHandler</span>接口的默认实现是<span style="color:red; font-weight: bold">SavedRequestAwareAuthenticationSuccessHandler</span>，
<span style="color:red; font-weight: bold">Authentication</span>是一个接口，封装了用户的相关信息

	{
			"authenticated":true,//表示已授权
			"authorities":[
				{
					"authority":"ROLE_USER" //角色集合
				}
			],
			"details":{
				"remoteAddress":"0:0:0:0:0:0:0:1",
				"sessionId":"9B2051321807D9F840975BC3D6218DF9"
			},
			"name":"admin",
			"principal":{
				"accountNonExpired":true,//账户没过期
				"accountNonLocked":true,//账户没锁定
				"authorities":[{"$ref":"$.data.authorities[0]"}],
				"credentialsNonExpired":true,//密码没有过期
				"enabled":true,//账户可用
				"username":"admin"//用户名
			}
		}

### 自定义登录失败处理
实现<span style="color:red; font-weight: bold">AuthenticationFailureHandler</span>接口，重写<span style="color:red; font-weight: bold">onAuthenticationFailure</span>方法。默认实现是<span style="color:red; font-weight: bold">SimpleUrlAuthenticationFailureHandler</span>

		/**
		 * @Author:shixianqing
		 * @Date:2019/5/27 11:12
		 * @Description:
		 * 授权失败处理器
		 **/
		@Slf4j
		public class CustomAuthFailHandler implements AuthenticationFailureHandler {
		    @Override
		    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		                                        AuthenticationException exception) throws IOException {
		        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		        log.error("{}",exception);
		        String respMsg = JSONObject.toJSONString(MetaResponse.error("用户名或密码不正确！"));
		        response.getOutputStream().write(respMsg.getBytes());
		    }
		}
		
### 授权成功处理器与授权失败处理器如何与过滤器关联？
当前前端发起一个请求到后端时，会到**UsernamePasswordAuthenticationFilter**过滤器里。该过滤器继承了**AbstractAuthenticationProcessingFilter**，所以请求进到AbstractAuthenticationProcessingFilter的**doFilter**方法

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
				throws IOException, ServletException {
	
			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) res;
	
			/**
				判断当前请求是否需要授权
				根据请求方式与请求url判断，当请求方式为POST请求时，需要授权拦截；当请求url与设置的url集合匹配上，需要授权
			**/
			if (!requiresAuthentication(request, response)) {
				chain.doFilter(request, response);
	
				return;
			}
	
			if (logger.isDebugEnabled()) {
				logger.debug("Request is to process authentication");
			}
	
			Authentication authResult;
	
			try {
				//获取授权结果
				authResult = attemptAuthentication(request, response);
				if (authResult == null) {
					// return immediately as subclass has indicated that it hasn't completed
					// authentication
					return;
				}
				
				/**
				如果会话已经存在，并且与来自客户端的会话ID匹配，则
				将创建会话，并将会话属性复制到该会话。如果客户端请求的会话ID无效，则不会执行任何操作，
				因为如果会话ID与当前会话不匹配，则无需更改会话ID。如果没有会话，则除非设置了属性，
				否则不会执行任何操作，在这种情况下，如果不存在会话，则将创建会话
				**/
				sessionStrategy.onAuthentication(authResult, request, response);
			}
			catch (InternalAuthenticationServiceException failed) {
				logger.error(
						"An internal error occurred while trying to authenticate the user.",
						failed);
				//授权失败，执行授权失败处理器
				unsuccessfulAuthentication(request, response, failed);
	
				return;
			}
			catch (AuthenticationException failed) {
				//授权失败，执行授权失败处理器
				unsuccessfulAuthentication(request, response, failed);
	
				return;
			}
	
			// Authentication success
			if (continueChainBeforeSuccessfulAuthentication) {
				chain.doFilter(request, response);
			}
			
			//授权成功后，将授权信息放到SecurityContext上下文里，然后执行授权成功处理器
			successfulAuthentication(request, response, chain, authResult);
		}

attemptAuthentication方法是一个抽象方法，被UsernamePasswordAuthenticationFilter过滤器实现

		public Authentication attemptAuthentication(HttpServletRequest request,
				HttpServletResponse response) throws AuthenticationException {
			if (postOnly && !request.getMethod().equals("POST")) {
				throw new AuthenticationServiceException(
						"Authentication method not supported: " + request.getMethod());
			}
	
			String username = obtainUsername(request);
			String password = obtainPassword(request);
	
			if (username == null) {
				username = "";
			}
	
			if (password == null) {
				password = "";
			}
	
			username = username.trim();
	
			UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
					username, password);
	
			// Allow subclasses to set the "details" property
			setDetails(request, authRequest);
			
			//执行授权
			return this.getAuthenticationManager().authenticate(authRequest);
		}

自定义成功或失败处理器，需要将处理器设置到过滤器里。

    @Bean
    public Filter customAuthenicationFilter() throws Exception {

        CustomAuthenicationFilter filter = new CustomAuthenicationFilter();
        filter.setAuthenticationSuccessHandler(new CustomAuthSuccessHanlder());
        filter.setAuthenticationFailureHandler(new CustomAuthFailHandler());
        //这句很关键，重用WebSecurityConfigurerAdapter配置的AuthenticationManager，不然要自己组装AuthenticationManager
        filter.setAuthenticationManager(super.authenticationManagerBean());
        return filter;
    }
    
    

## 表单登录，用户认证完整流程

![](https://i.imgur.com/cMmNU61.png)
	
FilterChainProxy 负责所有过滤器的执行调度

1. FilterChainProxy 交由DelegatingFilterProxy初始化

		@Override
		protected void initFilterBean() throws ServletException {
			synchronized (this.delegateMonitor) {
				if (this.delegate == null) {
					// If no target bean name specified, use filter name.
					if (this.targetBeanName == null) {
						this.targetBeanName = getFilterName();
					}
					// Fetch Spring root application context and initialize the delegate early,
					// if possible. If the root application context will be started after this
					// filter proxy, we'll have to resort to lazy initialization.
					WebApplicationContext wac = findWebApplicationContext();
					if (wac != null) {
						this.delegate = initDelegate(wac);
					}
				}
			}
		}
		
2. FilterChainProxy 执行doFilter方法，获取spring security中的过滤器链，然后将过滤器链交由VirtualFilterChain处理

		@Override
		public void doFilter(ServletRequest request, ServletResponse response)
				throws IOException, ServletException {
			if (currentPosition == size) {
				//省略部分代码

				// Deactivate path stripping as we exit the security filter chain
				this.firewalledRequest.reset();
				
				//ApplicationFilterChain
				originalChain.doFilter(request, response);
			}
			else {
				currentPosition++;
				
				//依次获取待执行过滤器
				Filter nextFilter = additionalFilters.get(currentPosition - 1);

				//省略部分代码
				
				执行过滤器
				nextFilter.doFilter(request, response, this);
			}
		}
		
	**nextFilter.doFilter(request, response, this);** 执行过滤器，并将自身传递过去，这样控制权又回到了***VirtualFilterChain***手里

3. 执行SecurityContextPersistenceFilter过滤器，从session中拿取SecurityContext上下文，如果session中没有，则创建一个SecurityContext，并放到SecurityContextHolder中，执行完该过滤器后，再把SecurityContext上下文放到session里，key为SPRING_SECURITY_CONTEXT，value为SecurityContext对象，然后SecurityContext从SecurityContextHolder清除。

		public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
				throws IOException, ServletException {
				
			//省略了部分代码........
			
			HttpRequestResponseHolder holder = new HttpRequestResponseHolder(request,
					response);
			
			//从session中获取SecurityContext，如果session中没有，则创建
			SecurityContext contextBeforeChainExecution = repo.loadContext(holder);
	
			try {
				
				//将SecurityContext放到线程副本中，实现一次请求过程共享授权信息
				SecurityContextHolder.setContext(contextBeforeChainExecution);
	
				chain.doFilter(holder.getRequest(), holder.getResponse());
	
			}
			finally {
				
				//从线程副本中拿到SecurityContext
				SecurityContext contextAfterChainExecution = SecurityContextHolder
						.getContext();
				
				//从线程副本中清除SecurityContext
				SecurityContextHolder.clearContext();
				
				//将SecurityContext放到session中
				repo.saveContext(contextAfterChainExecution, holder.getRequest(),
						holder.getResponse());
				
				//省略了部分代码
			}
		}
		

	SecurityContextPersistenceFilter的作用：
	
	  - SecurityContext 中封装了Authentication对象，内含有用户认证过程中的授权信息，一开始SecurityContext中没有这些信息，会在用户认证完成之后，在其他过滤器中将用户认证信息放到SecurityContext里。	
	  
	  - 实现用户认证信息在一次请求中共享 

4. UsernamePasswordAuthenticationFilter过滤器执行用户认证

	![](https://i.imgur.com/qqpwXvD.png)

	如图所示，这几个类是用户认证的核心
	
	ProviderManager实现了AuthenticationManager，UsernamePasswordAuthenticationFilter类执行`getAuthenticationManager().authenticate(authRequest);`实际上执行的是ProviderManager的authenticate方法，ProviderManager部分源码如下：
   
   
   	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		//省略部分代码
		Class<? extends Authentication> toTest = authentication.getClass();
		for (AuthenticationProvider provider : getProviders()) {
			
			//判断AuthenticationProvider是否支持authentication
			//表单提交，执行的AbstractUserDetailsAuthenticationProvider的supports方法
			if (!provider.supports(toTest)) {
				continue;
			}

			//省略部分代码
		
			try {
				
				//调用AbstractUserDetailsAuthenticationProvider的authenticate方法
				result = provider.authenticate(authentication);

				if (result != null) {
					copyDetails(authentication, result);
					break;
				}
			}
			catch (AccountStatusException e) {
				prepareException(e, authentication);
				// SEC-546: Avoid polling additional providers if auth failure is due to
				// invalid account status
				throw e;
			}
			catch (InternalAuthenticationServiceException e) {
				prepareException(e, authentication);
				throw e;
			}
			catch (AuthenticationException e) {
				lastException = e;
			}
		}

		//省略部分代码
		
	}

	`ProviderManager`的`authenticate`方法中调用了`AbstractUserDetailsAuthenticationProvider`的`authenticate`方法，`AbstractUserDetailsAuthenticationProvider`部分源码如下：
	
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication,
				() -> messages.getMessage(
						"AbstractUserDetailsAuthenticationProvider.onlySupports",
						"Only UsernamePasswordAuthenticationToken is supported"));
	
		// Determine username
		String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED"
				: authentication.getName();
	
		boolean cacheWasUsed = true;
		
		//从用户缓存中获取用户认证信息
		UserDetails user = this.userCache.getUserFromCache(username);
	
		if (user == null) {
			cacheWasUsed = false;
	
			try {
				//如果缓存中获取不到，则调UserDetailsService的loadUserByName方法，获取认证信息
				user = retrieveUser(username,
						(UsernamePasswordAuthenticationToken) authentication);
			}
			catch (UsernameNotFoundException notFound) {
				logger.debug("User '" + username + "' not found");
	
				if (hideUserNotFoundExceptions) {
					throw new BadCredentialsException(messages.getMessage(
							"AbstractUserDetailsAuthenticationProvider.badCredentials",
							"Bad credentials"));
				}
				else {
					throw notFound;
				}
			}
	
			Assert.notNull(user,
					"retrieveUser returned null - a violation of the interface contract");
		}
	
		try {
			
			/**
				执行内部类DefaultPreAuthenticationChecks类中的check方法
				校验账户是否被锁定、账户是否可用、账户是否过期
			**/
			preAuthenticationChecks.check(user);
			
			//该方法是抽象方法，执行的是DaoAuthenticationProvider的additionalAuthenticationChecks方法
			//检验用户密码
			additionalAuthenticationChecks(user,
					(UsernamePasswordAuthenticationToken) authentication);
		}
		catch (AuthenticationException exception) {
			if (cacheWasUsed) {
				// There was a problem, so try again after checking
				// we're using latest data (i.e. not from the cache)
				cacheWasUsed = false;
				user = retrieveUser(username,
						(UsernamePasswordAuthenticationToken) authentication);
				preAuthenticationChecks.check(user);
				additionalAuthenticationChecks(user,
						(UsernamePasswordAuthenticationToken) authentication);
			}
			else {
				throw exception;
			}
		}
		
		
		//执行的是内部类DefaultPostAuthenticationChecks中的check方法，校验密码是否过期
		postAuthenticationChecks.check(user);
	
		if (!cacheWasUsed) {
			this.userCache.putUserInCache(user);
		}
	
		Object principalToReturn = user;
	
		if (forcePrincipalAsString) {
			principalToReturn = user.getUsername();
		}
	
		//将认证信息重新封装到Authentication对象中
		return createSuccessAuthentication(principalToReturn, authentication, user);
	}

5. `FilterSecurityInterceptor`这个过滤器授权验证的。`FilterSecurityInterceptor`的工作流程引用一下，可以理解如下：`FilterSecurityInterceptor`从`SecurityContextHolder`中获取`Authentication`对象，然后比对***用户拥有的权限***和***资源所需的权限***。前者可以通过`Authentication`对象直接获得，而后者则需要引入我们之前一直未提到过的两个类：`SecurityMetadataSource`，`AccessDecisionManager`。`SecurityMetadataSource`封装了资源所需要的权限，`AccessDecisionManager`处理权限比对。部分源码如下：
	
		public void doFilter(ServletRequest request, ServletResponse response,
				FilterChain chain) throws IOException, ServletException {
			FilterInvocation fi = new FilterInvocation(request, response, chain);
			invoke(fi);
		}

		public void invoke(FilterInvocation fi) throws IOException, ServletException {
		if ((fi.getRequest() != null)
				&& (fi.getRequest().getAttribute(FILTER_APPLIED) != null)
				&& observeOncePerRequest) {
			// filter already applied to this request and user wants us to observe
			// once-per-request handling, so don't re-do security checking
			fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
		}
		else {
			// first time this request being called, so perform security checking
			if (fi.getRequest() != null && observeOncePerRequest) {
				fi.getRequest().setAttribute(FILTER_APPLIED, Boolean.TRUE);
			}

			//调用父类AbstractSecurityInterceptor的beforeInvocation方法，
			//主要判断用户权限与资源权限是否匹配
			InterceptorStatusToken token = super.beforeInvocation(fi);

			try {
				fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
			}
			finally {
				super.finallyInvocation(token);
			}

			super.afterInvocation(token, null);
		}
	}