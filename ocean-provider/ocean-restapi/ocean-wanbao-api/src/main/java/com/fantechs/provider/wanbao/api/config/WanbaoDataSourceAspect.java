package com.fantechs.provider.wanbao.api.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Component
@Aspect
public class WanbaoDataSourceAspect {

    //切入点在service层的方法上，配置aop的切入点
    @Pointcut("execution( * com.fantechs.provider.wanbao.api.service..*.*(..))")
    public void dataSourcePointCut() {
    }

    //切入点只对@Service注解的类上的@DataSource方法生效
//    @Pointcut(value="@within(org.springframework.stereotype.Service) && @annotation(dataSource)" )
//    public void dataSourcePointCut(DataSource dataSource) {
//    }

    @Before("dataSourcePointCut()")
    public void before(JoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        String method = joinPoint.getSignature().getName();
        Class<?> clazz = target.getClass();
        Class<?>[] clazzs = target.getClass().getInterfaces();
        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes();
        try {
            Method m = clazz.getMethod(method, parameterTypes);
            Method m1 = clazzs[0].getMethod(method, parameterTypes);
            //如果方法上存在切换数据源的注解，则根据注解内容进行数据源切换
            if (m != null && (m.isAnnotationPresent(DataSource.class) || m1.isAnnotationPresent(DataSource.class))) {
                DataSource annotation1 = m1.getAnnotation(DataSource.class);
                DataSource annotation = m.getAnnotation(DataSource.class);
                String dataSourceName = annotation1 == null?annotation.value():annotation1.value();
                DynamicDataSourceHolder.putDataSouce(dataSourceName);
            } else {

            }
        } catch (NoSuchMethodException e) {

        }
    }


    //执行完切面后，清空线程共享中的数据源名称
    @After("dataSourcePointCut()")
    public void after(JoinPoint joinPoint){
        DynamicDataSourceHolder.removeDataSource();
    }

}
