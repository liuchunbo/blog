package com.example.guava.concurrency;

import java.lang.annotation.*;

/**
 * 自定义注解  限流
 */
@Documented
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RLimit {

    /**
     *  key
     */
    String key() default "";
    /**
     *  备注
     */
    String description()  default "";
    /**
     *  限流的类型
     */
    LimitType limitType() default LimitType.RANDOM_KEY;

    enum LimitType {
        /**
         * 自定义key
         */
        RANDOM_KEY,
        /**
         * 根据请求者IP
         */
        IP
    }
}
