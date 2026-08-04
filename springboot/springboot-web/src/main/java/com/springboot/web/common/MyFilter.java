package com.springboot.web.common;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;


@WebFilter(filterName = "MyFilter",urlPatterns = "/*")
public class MyFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("Filter init ...");
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		System.out.println("前置过滤");
		chain.doFilter(request,response);
		System.out.println("后置过滤");
	}

	@Override
	public void destroy() {
		System.out.println("destroy ... ");
	}
}