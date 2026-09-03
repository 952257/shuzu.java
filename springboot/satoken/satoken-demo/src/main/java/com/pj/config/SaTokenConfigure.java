package com.pj.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handler -> {
                    SaRouter.match("/**")
                            .notMatch(
                                    "/user/doLogin",
                                    "/user/isLogin",
                                    "/acc/doLogin",
                                    "/acc/isLogin",
                                    "/acc/logout",
                                    "/error"
                            )
                            .check(r -> StpUtil.checkLogin());

                    SaRouter.match("/**")
                            .notMatch(
                                    "/user/doLogin",
                                    "/user/isLogin",
                                    "/acc/doLogin",
                                    "/acc/isLogin",
                                    "/acc/logout",
                                    "/acc/tokenInfo",
                                    "/error"
                            )
                            .check(r -> StpUtil.checkPermission(SaHolder.getRequest().getRequestPath()));
                }))
                .addPathPatterns("/**");
    }
}
