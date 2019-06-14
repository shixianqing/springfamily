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
