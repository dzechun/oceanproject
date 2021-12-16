package com.fantechs.provider.restapi.mulinsen.config;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    String value() default "primary";
}
