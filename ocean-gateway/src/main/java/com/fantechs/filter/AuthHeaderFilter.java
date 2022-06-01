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
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.RestTemplateUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.common.base.utils.TokenUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 * The class Auth header filter.
 *
 * @author paascloud.net @gmail.com
 */
@Slf4j
@Component
public class AuthHeaderFilter extends ZuulFilter {

	@Resource
	private RedisUtil redisUtil;

//	@Resource
//	private SecurityFeignApi securityFeignApi;

	@Value("${url.securityUrl}")
	private String securityUrl;

	@Value("${constant-base.orgId}")
	private Long orgId;


	//排除过滤的 uri 地址
	private static final String LOGIN_URI = "/ocean-auth/meslogin,/ocean-auth/loginByOrgCode,/ocean-auth/refreshtoken,/ocean-auth/clientGetToken," +
			"/ocean-auth/userinfo,/ocean-auth/logout,/ocean-fileserver/file/download,/ocean-fileserver/file/multipleFileBase64" +
			",/ocean-fileserver/file/uploadToSVG,/ocean-imes-materialapi/material/api" +
			",/ocean-auth/sysRole/findList,/ocean-auth/sysUser/saveByApi" +
			",/ocean-auth/sysSpecItem/findList,/ocean-exhibition-client/RCSAPI/agvCallback,/ocean-base/baseOrganization/findList"+
			",/ocean-imes-materialapi/material/workOrder,/ocean-imes-materialapi/material/purchaseOrder,/ocean-esop/esopNews/findList" +
			",/ocean-imes-materialapi/material/ChkLogUserInfo,/ocean-imes-materialapi/material/ChkSnRouting,/ocean-imes-materialapi/material/SnDataTransfer" +
			",/ocean-auth/eamlogin,/ocean-base/baseFile/findList,/ocean-base/baseFile/batchAddFile,/ocean-base/baseFile/add,/ocean-guest-callagv/callAgvBarcode/add" +
			",/ocean-guest-callagv/callAgvBarcode/findList,/ocean-guest-callagv/callAgvBarcode/findList,/ocean-ureport/proLineBoard/findList," +
			"/ocean-chinafivering-api/webServiceImport/getVendor,/ocean-chinafivering-api/webServiceImport/getIssueDetails,/ocean-eam/eamEquipment/findList,"+
			"/ocean-chinafivering-api/webServiceImport/getPoDetails,/ocean-chinafivering-api/webServiceImport/getPartNoInfo,/ocean-chinafivering-api/webServiceImport/getVendorUserNameAndPwd,"+
			"/ocean-chinafivering-api/webServiceImport/getShelvesNo,/ocean-chinafivering-api/webServiceImport/getSubcontractor,"+
			"/ocean-chinafivering-api/webServiceImport/getReqDetails,"+
			"/ocean-fileserver/file/uploadsFiles";
	private static final String SWAGGER_URI = "/v2/api-docs";

	private static final String CLIENT_URI = "/ocean-client/createPtlJobOrder,/ocean-client/cancelPtrlJobOrder,/ocean-esop/esopEquipmentStatus/closeThird," +
			"/ocean-wanbao-api/wanbaoSyncData/syncMaterialData,/ocean-wanbao-api/wanbaoSyncData/syncBarcodeData," +
			"/ocean-wanbao-api/wanbaoSyncData/syncOrderData,/ocean-wanbao-api/wanbaoSyncData/syncSaleOrderData,/ocean-wanbao-api/wanbaoSyncData/syncOutDeliveryData," +
			"/ocean-chinafivering-api/webServiceImport/getVendor,/ocean-chinafivering-api/webServiceImport/getIssueDetails,"+
			"/ocean-chinafivering-api/webServiceImport/getPoDetails,/ocean-chinafivering-api/webServiceImport/getPartNoInfo,"+
			"/ocean-chinafivering-api/webServiceImport/getShelvesNo,/ocean-chinafivering-api/webServiceImport/getSubcontractor,"+
			"/ocean-chinafivering-api/webServiceImport/getReqDetails,/ocean-chinafivering-api/webServiceImport/getVendorUserNameAndPwd,/ocean-qms/qmsInspectionOrder/autoAdd,"+
			"/ocean-mes-sfc/mesSfcScanBarcode/chkLogUserInfo,/ocean-mes-sfc/mesSfcScanBarcode/chkSnRouting,/ocean-mes-sfc/mesSfcScanBarcode/snDataTransfer,"+
			"/ocean-auth/sysUser/findList,/ocean-auth/sysRole/findList,/ocean-auth/sysUser/saveByApi,"+
			"/ocean-guest-callagv/RCSAPI/agvCallback,/ocean-imes-materialapi/productBomApi,/ocean-esop-baseapi/getIssue/getAllIssue,/ocean-esop-baseapi/getWorkOrder/getAllWorkOrder";

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
		if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
			log.info("OPTIONS请求不做拦截操作");
			return false;
		}
		if (request.getRequestURI().contains(SWAGGER_URI)|| LOGIN_URI.contains(request.getRequestURI())) {
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
		Boolean tokenBoolean = false;
		if (StringUtils.isEmpty(token) && CLIENT_URI.contains(request.getRequestURI())) {
			tokenBoolean = true;
			Set<String> set = redisUtil.keys("client_token*");
			if (set.size() > 0) {
				for (String str : set) {
					token = str;
					break;
				}
			} else {
				token = RestTemplateUtil.getForStringNoJson(securityUrl + "?orgId=" + orgId);
				log.info("---------为客户端赋予一个可访问的token : " + token + "---------------");
			}
		}
		boolean flag =false;
		ResponseEntity<String> result = new ResponseEntity<>();
		if (StringUtils.isEmpty(token)) {
			requestContext.setSendZuulResponse(false); //对该请求不进行路由
			requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
			result.setCode(ErrorCodeEnum.UAC10010002.getCode());
			result.setMessage(ErrorCodeEnum.UAC10010002.getMsg());
			requestContext.setResponseBody(JSONObject.toJSONString(result));
			requestContext.getResponse().setContentType("text/html;charset=UTF-8");
			return requestContext;

		}
		SysUser user = TokenUtil.load(token);
		if(StringUtils.isEmpty(user)){
			requestContext.setSendZuulResponse(false);
			requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
			result.setCode(ErrorCodeEnum.UAC10011039.getCode());
			result.setMessage(ErrorCodeEnum.UAC10011039.getMsg());
			requestContext.setResponseBody(JSONObject.toJSONString(result));
			requestContext.getResponse().setContentType("text/html;charset=UTF-8");
			return requestContext;
		}else{
			if (StringUtils.isEmpty(user.getAuthority()) && tokenBoolean) {
				Set<String> permsSet = new HashSet<>();
				String[] clientUris = CLIENT_URI.split(",");
				for (String clientUri : clientUris) {
					//去除服务名，获取权限URL
					String[] pathArr =  clientUri.split("/");
					StringBuffer sb = new StringBuffer();
					for(int i=2;i<pathArr.length;i++){
						sb.append("/").append(pathArr[i]);
					}
					String url =sb.toString();
					permsSet.add(url);
				}
				log.info("--------------设置可访问的菜单地址-----------------");
				user.setAuthority(permsSet);
			}
			if(StringUtils.isNotEmpty(user.getAuthority())){
				StringBuffer sb = new StringBuffer();
				String path = request.getServletPath();
				String[] pathArr =  path.split("/");
				if(pathArr.length>2){
					//去除服务名，匹配权限URL
					for(int i=2;i<pathArr.length;i++){
						sb.append("/").append(pathArr[i]);
					}
					String url =sb.toString();
					Set<String> authority = user.getAuthority();
					if(authority.contains(url)){
						flag = true;
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
			requestContext.addZuulRequestHeader("token", token);
			requestContext.addZuulRequestHeader("user-agent", request.getHeader("user-agent"));
			requestContext.setSendZuulResponse(true);// 对该请求进行路由
			requestContext.setResponseStatusCode(HttpStatus.OK.value());

		}
		return requestContext;
	}



}
