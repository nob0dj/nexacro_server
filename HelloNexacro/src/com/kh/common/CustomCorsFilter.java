package com.kh.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class CorsFilter
 */
@WebFilter(filterName = "CustomCorsFilter", urlPatterns = { "/*" })
public class CustomCorsFilter implements Filter {

    /**
     * Default constructor. 
     */
    public CustomCorsFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
        System.out.println("CORSFilter HTTP Request: " + request.getMethod());
 
        // Authorize (allow) all domains to consume the content
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "accept, cache-control, content-type, expires, if-modified-since, pragma, x-requested-with");
    	response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS, HEAD");
//    	response.setHeader("Access-Control-Max-Age", "3600");
//    	response.setHeader("Access-Control-Allow-Credentials", "true");

    	/*
    	 * 오류메세지
    	 * 
    	 * Request header field pragma is not allowed by Access-Control-Allow-Headers in preflight response.
    	 * => pragma 추가해결
    	 * 
    	 * Response to preflight request doesn't pass access control check: It does not have HTTP ok status.
    	 * => resp.setStatus(HttpServletResponse.SC_OK); 로 해결. SC_ACCEPTED 또한 잘 작동한다.
    	 * 
    	 */
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
 
        // For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per CORS handshake
        if (request.getMethod().equals("OPTIONS")) {
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
//            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }
 
        // pass the request along the filter chain
        chain.doFilter(request, servletResponse);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
