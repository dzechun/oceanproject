package com.fantechs.common.base.utils;

import com.fantechs.common.base.entity.security.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: bingo.ren
 * @Date: 2020/4/23 11:38
 * @Description: AOP 记录controller访问日志
 * @Version:1.0
 */
@Aspect
@Component
@Slf4j
public class AOP {
    @Pointcut("execution(public * *..controller..*.*(..))")
    public void pointCut(){}

    @Before("pointCut()")
    public void logBeforeController(JoinPoint joinPoint){

        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes
             .resolveReference(RequestAttributes.REFERENCE_REQUEST);

        String token =request .getHeader("token");
        StringBuffer stringBuffer=new StringBuffer("(");
        Object[] args = joinPoint.getArgs();
        String method =  joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();
        if (method.equals("com.fantechs.provider.mes.sfc.controller.MesSfcWorkOrderBarcodeController.add") || method.equals("com.fantechs.provider.mes.sfc.controller.MesSfcWorkOrderBarcodeController.print")){
            return;
        }
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if(!StringUtils.isEmpty(arg)) {
                stringBuffer.append(arg.toString());
            }else{
                stringBuffer.append("null");
            }
            if(i<args.length-1){
                stringBuffer.append(",");
            }
            if(i==args.length-1){
                stringBuffer.append(",请求地址=").append(TokenUtil.getIpAddress(request));
                SysUser user = TokenUtil.load(token);
                if(StringUtils.isNotEmpty(user)){
                    stringBuffer.append(",").append("请求人=").append(user.getUserName());
                }

            }
        }
        stringBuffer.append(")");
        log.info("【AOP日志记录】正在执行："+joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName()+"，传入参数:"+stringBuffer);
    }

    @AfterReturning(value = "pointCut()",returning = "result")
    public void logAfterController(JoinPoint joinPoint, Object result){
        String method =  joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();
        if (method.equals("com.fantechs.provider.mes.sfc.controller.MesSfcWorkOrderBarcodeController.add") || method.equals("com.fantechs.provider.mes.sfc.controller.MesSfcWorkOrderBarcodeController.print")){
            return;
        }
        log.info("【AOP日志记录】执行结束："+joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName()+"，返回结果:"+result);
    }
}
