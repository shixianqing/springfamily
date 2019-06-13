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