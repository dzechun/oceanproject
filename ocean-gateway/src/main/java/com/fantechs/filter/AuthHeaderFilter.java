/*
 * Copyright (c) 2018. paascloud.net All Rights Reserved.
 * 项目名称：paascloud快速搭建企业级分布式微服务平台
 * 类名称：AuthHeaderFilter.java
 * 创建人：刘兆明
 * 联系方式：paascloud.net@gmail.com
 * 开源地址: https://github.com/paascloud
 * 博客地址: http://blog.paascloud.net
 * 项目官网: http://paascloud.net
 */

package com.fantechs.filter;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.common.base.utils.TokenUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * The class Auth header filter.
 *
 * @author paascloud.net @gmail.com
 */
@Slf4j
@Component
public class AuthHeaderFilter extends ZuulFilter {

	//排除过滤的 uri 地址
	private static final String LOGIN_URI = "/ocean-security/";
	private static final String SWAGGER_URI = "/v2/api-docs";

	/**
	 * Filter type string.
	 *
	 * @return the string
	 */
	@Override
	public String filterType() {
		return "pre";
	}

	/**
	 * Filter order int.
	 *
	 * @return the int
	 */
	@Override
	public int filterOrder() {
		return 0;
	}

	/**
	 * Should filter boolean.
	 *
	 * @return the boolean
	 */
	@Override
	public boolean shouldFilter() {
		RequestContext requestContext = RequestContext.getCurrentContext();
		HttpServletRequest request = requestContext.getRequest();

		System.out.println(request.getRequestURI());

		if (request.getRequestURI().contains(SWAGGER_URI)|| request.getRequestURI().contains(LOGIN_URI)) {
			return false;
		}
			return true;
	}

	/**
	 * Run object.
	 *
	 * @return the object
	 */
	@Override
	public Object run() {
		RequestContext requestContext = RequestContext.getCurrentContext();
		HttpServletRequest request = requestContext.getRequest();

		String token =request .getHeader("token");
		boolean flag =false;
		ResponseEntity<String> result = new ResponseEntity<>();
		if (StringUtils.isEmpty(token)) {
			requestContext.setSendZuulResponse(false); //对该请求不进行路由
			requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
			result.setCode(ErrorCodeEnum.UAC10010002.getCode());
			result.setMessage(ErrorCodeEnum.UAC10010002.getMsg());
			requestContext.setResponseBody(JSONObject.toJSONString(result));
			requestContext.getResponse().setContentType("text/html;charset=UTF-8");

		}
		SysUser user = TokenUtil.load(token);
		if(StringUtils.isEmpty(user)){
			requestContext.setSendZuulResponse(false);
			requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
			result.setCode(ErrorCodeEnum.UAC10011039.getCode());
			result.setMessage(ErrorCodeEnum.UAC10011039.getMsg());
			requestContext.setResponseBody(JSONObject.toJSONString(result));
			requestContext.getResponse().setContentType("text/html;charset=UTF-8");

		}else{
			if(StringUtils.isEmpty(user.getAuthority())){
				requestContext.setSendZuulResponse(false);
				requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
				result.setCode(ErrorCodeEnum.GL99990401.getCode());
				result.setMessage(ErrorCodeEnum.GL99990401.getMsg());
				requestContext.setResponseBody(JSONObject.toJSONString(result));
				requestContext.getResponse().setContentType("text/html;charset=UTF-8");

			}else{
				StringBuffer sb = new StringBuffer();
				String path = request.getServletPath();
				String[] pathArr =  path.split("/");

				if(pathArr.length<2){
					requestContext.setSendZuulResponse(false);
					requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
					result.setCode(ErrorCodeEnum.GL99990401.getCode());
					result.setMessage(ErrorCodeEnum.GL99990401.getMsg());
					requestContext.setResponseBody(JSONObject.toJSONString(result));
					requestContext.getResponse().setContentType("text/html;charset=UTF-8");
				}
				//去除服务名，匹配权限URL
				for(int i=2;i<pathArr.length;i++){
					sb.append("/").append(pathArr[i]);
				}
				Set<String> authority = user.getAuthority();
				for(String authorityUrl :authority){
					if(authorityUrl.equals(sb.toString())){
						flag = true;
						break;
					}
				}
			}
		}
		if(!flag){
			requestContext.setSendZuulResponse(false);
			requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
			result.setCode(ErrorCodeEnum.GL99990401.getCode());
			result.setMessage(ErrorCodeEnum.GL99990401.getMsg());
			requestContext.setResponseBody(JSONObject.toJSONString(result));
			requestContext.getResponse().setContentType("text/html;charset=UTF-8");
		}else{
			requestContext.setSendZuulResponse(true);// 对该请求进行路由
			requestContext.setResponseStatusCode(HttpStatus.OK.value());
		}
		return null;
	}



}
