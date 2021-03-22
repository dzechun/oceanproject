package com.fantechs.provider.log.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.dto.security.SysMenuInListDTO;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.entity.log.SmtEmpOperationLog;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Component
@Aspect
public class AopConfig {

    //切入点在controller层的方法上，配置aop的切入点
    @Pointcut("execution( * com.fantechs.*.*.controller.*.*(..))")
    public void pointCut() {
    }

    @Autowired
    private HttpServletRequest httpServletRequest;
    @Resource
    private RedisUtil redisUtil;

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object proceed = joinPoint.proceed(args);
        JSONObject jsonObject = JSON.parseObject(proceed.toString());
        System.out.println(getIpAddress(httpServletRequest));
        //判断增强方法是否执行成功
        if ("0".equals(jsonObject.get("code"))){
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();


            HashMap<String, String> operateType = new HashMap<>();
            operateType.put("add", "增加");
            operateType.put("delete", "删除");
            operateType.put("update", "修改");
            if (StringUtils.isNotEmpty(user)) {
                String uri = httpServletRequest.getRequestURI();
                Set<String> keys = operateType.keySet();
                for (String key : keys) {
                    if (uri.contains(key)) {
                        SmtEmpOperationLog smtEmpOperationLog = new SmtEmpOperationLog();
                        smtEmpOperationLog.setCreateUserId(user.getUserId());
                        smtEmpOperationLog.setCreateTime(new Date());
                        smtEmpOperationLog.setModifiedTime(new Date());
                        smtEmpOperationLog.setCreateUserId(user.getUserId());
                        smtEmpOperationLog.setHost(getIpAddress(httpServletRequest));
                        smtEmpOperationLog.setOperationType(operateType.get(key));

                        Object roleMenuList = redisUtil.get("roleMenuList");
                        List<SysMenuInListDTO> sysMenuInListDTOS = JSONArray.parseArray(JSON.toJSONString(roleMenuList), SysMenuInListDTO.class);
                        String menuName = find(uri, sysMenuInListDTOS);
                        smtEmpOperationLog.setFunctionMenu(menuName);

                        System.out.println(smtEmpOperationLog);
//                    if (uri.contains("smtOrder")) {
//                        Object o = args[0];
//                    } else if (uri.contains("mesSchedule")) {
//                        Object o = args[0];
//                    } else if (uri.contains("smtWorkOrder")) {
//                        Object o = args[0];
//                    } else if (uri.contains("wmsInFinishedProduct")) {
//                        Object o = args[0];
//                    } else if (uri.contains("wmsOutShippingNote")) {
//                        Object o = args[0];
//                    } else if (uri.contains("wmsOutDeliveryOrder")) {
//                        Object o = args[0];
//                    } else if (uri.contains("wmsOutOtherout")) {
//                        Object o = args[0];
//                    } else {
//
//                    }
                        break;
                    }
                }
            }
        }
        return proceed;
    }

    public String find(String uri,List<SysMenuInListDTO> list){
        if (StringUtils.isNotEmpty(list) && list.size() >0){
            for (SysMenuInListDTO sysMenuInListDTO : list) {
                if (uri.equals(sysMenuInListDTO.getSysMenuInfoDto().getUrl())){
                    return sysMenuInListDTO.getSysMenuInfoDto().getParentId().toString();
                }
                String menu = find(uri, sysMenuInListDTO.getSysMenuinList());
                if (StringUtils.isNotEmpty(menu) && menu.equals(sysMenuInListDTO.getSysMenuInfoDto().getMenuId().toString())){
                    return sysMenuInListDTO.getSysMenuInfoDto().getMenuName();
                }else if (StringUtils.isNotEmpty(menu)){
                    return menu;
                }
            }
        }
        return null;
    }

    /**
     * 获取用户真实ip地址
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


}
