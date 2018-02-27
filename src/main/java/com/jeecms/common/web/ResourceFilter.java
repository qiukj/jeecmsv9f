package com.jeecms.common.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeecms.common.util.StrUtils;

/**
 * 资源过滤器
 * 
 * @author tom
 * 
 */
public class ResourceFilter implements Filter {
	protected final Logger log = LoggerFactory
			.getLogger(ResourceFilter.class);

	public void destroy() {
	}

	@SuppressWarnings("static-access")
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String uri=request.getRequestURI();
		String suffix=StrUtils.getSuffix(uri);
		//wenku目录下只运行直接访问的资源只有swf和pdf
		//swf为v8之前版本的格式 pdf为v8的版本格式
		if(!suffix.equals("swf")&&!suffix.equals("pdf")){
			response.sendError(response.SC_FORBIDDEN);
		}else{
			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
	}
}
