package com.lzq.redispushchannel.config.dataSource;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Statement;
import java.util.Properties;

/**
 * @author lzq
 * @version 1.0
 * @date 2021/11/9 14:38
 */
@Component
@Intercepts({@Signature(method = "query", type = StatementHandler.class, args = {Statement.class, ResultHandler.class})})
public class MybatisDataSourceInterceptor implements Interceptor {

    private final Logger logger = LoggerFactory.getLogger(MybatisDataSourceInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        // 获取指定对象的元信息
        MetaObject metaObject = MetaObject.forObject(
                statementHandler,
                SystemMetaObject.DEFAULT_OBJECT_FACTORY,
                SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY,
                new DefaultReflectorFactory()
        );

        // 然后就可以通过MetaObject获取对象的属性
        // 获取RoutingStatementHandler->PrepareStatementHandler->BaseStatementHandler中的mappedStatement
        // mappedStatement 包含了Sql的信息
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        // 获取statement id
        String statementId = mappedStatement.getId();
        String className = statementId.substring(0, statementId.lastIndexOf("."));
        Class<?> aClass = Class.forName(className);
        DataSource annotation = aClass.getAnnotation(DataSource.class);
        if(annotation == null){
            DynamicDataSource.setDataSource("master");
            logger.debug("set datasource is master");
        }else {
            DynamicDataSource.setDataSource(annotation.name());
            System.out.println("set datasource is " + annotation.name());
        }

        try {
            return invocation.proceed();
        } finally {
            DynamicDataSource.clearDataSource();
            logger.debug("clean datasource");
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
