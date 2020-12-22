package com.wu.springsecuritydemo.config;

import com.wu.springsecuritydemo.pojo.ResultBean;
import net.sf.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    //必须的Bean，这里不做密码加密
    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }


    //配置认证信息
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("admin")
//                .password("123")
//                .roles("admin")
//                .and()
//                .withUser("admin200")
//                .password("123")
//                .roles("user");
//    }

    @Bean
    protected UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("admin").password("123").roles("admin").build());
        manager.createUser(User.withUsername("admin200").password("123").roles("user").build());
        return manager;
    }


    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_admin > ROLE_user");
        return roleHierarchy;
    }



    //配置拦截规则
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/admin/**").hasRole("admin")
                .antMatchers("/user/**").hasRole("user")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/doLogin")
                .usernameParameter("name")
                .passwordParameter("pass")
//                .successForwardUrl("/success")
//                .defaultSuccessUrl("/success")
                .successHandler((req, resp, auth) -> {  //登录成功返回次操作
                    resp.setContentType("application/json;charset=utf-8");
                    PrintWriter out = resp.getWriter();
                    out.write(JSONObject.fromObject(ResultBean.SUCCESS_CUSTOM(10000, "登录成功","")).toString());
//                    out.write(JSONObject.fromObject(auth.getPrincipal()).toString());  //返回用户信息
                    out.flush();
                    out.close();

                })
                .failureHandler((req, resp, exception) -> { //如果账号密码错误，账号被锁定，过期等，会返回次操作
                    resp.setContentType("application/json;charset=utf-8");
                    PrintWriter out = resp.getWriter();
                    ResultBean resultBean = judgeloginException(exception);//判断登录失败是因为什么原因
                    out.write(JSONObject.fromObject(resultBean).toString());
                    out.flush();
                    out.close();
                })
                .permitAll()
                .and()
                .logout()
//                .logoutUrl("/logout")  //默认get请求
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
//                .invalidateHttpSession(true)  //session失效 默认true
//                .clearAuthentication(true)  //清除认证信息 默认true
                .logoutSuccessUrl("/login.html")
                .permitAll()
                .and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint((req, resp, auth) -> { //如果没有登录，返回次提示
                    resp.setContentType("application/json;charset=utf-8");
                    PrintWriter out = resp.getWriter();
                    out.write("未登录，请登录");
                    out.flush();
                    out.close();
                });
    }

    //判断登录失败是因为什么原因
    private ResultBean judgeloginException(AuthenticationException exception) {
        String result = "";
        if (exception instanceof BadCredentialsException) {
            result = "账号或密码错误";
        } else if (exception instanceof LockedException) {
            result = "账号被锁定";
        } else if (exception instanceof CredentialsExpiredException) {
            result = "密码过期";
        } else if (exception instanceof AccountExpiredException) {
            result = "账号过期";
        } else if (exception instanceof DisabledException) {
            result = "账号被禁用";
        }
        return ResultBean.LOSS_CUSTOM(10001, result,"");
    }


    //忽略某些资源不做拦截
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/js/**", "/css/**");
    }
}
