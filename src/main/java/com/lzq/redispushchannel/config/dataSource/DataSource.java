package com.lzq.redispushchannel.config.dataSource;

import java.lang.annotation.*;
/**
 * @author lzq
 * @version 1.0
 * @date 2021/11/9 14:03 自定义数据源选择注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    String name() default "";
}

