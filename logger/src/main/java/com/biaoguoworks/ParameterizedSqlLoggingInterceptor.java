package com.biaoguoworks;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.biaoguoworks.config.Config;
import com.biaoguoworks.predict.IsPrinterLogContext;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wuxin
 * @date 2025/04/19 12:09:54
 */
@Intercepts(value = {
        @Signature(type = StatementHandler.class, method = "query", args = { Statement.class, ResultHandler.class }),
        @Signature(type = StatementHandler.class, method = "update", args = { Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = { Statement.class}),
        @Signature(type = StatementHandler.class, method = "queryCursor", args = { Statement.class}),
})
public class ParameterizedSqlLoggingInterceptor implements Interceptor {

    private Config config;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ParameterizedSqlLoggingInterceptor(Config config) {
        this.config = config;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            StatementHandler target = PluginUtils.realTarget(invocation.getTarget());
            Optional<MappedStatement> mappedStatement = getMappedStatement(target);
            IsPrinterLogContext isPrinterLogContext = new IsPrinterLogContext().setConfig(config).setMappedStatement(mappedStatement);
            config.getPrinterLogPredictChain().execChain(isPrinterLogContext);
            if(Objects.nonNull(isPrinterLogContext.getPrinterLog()) && isPrinterLogContext.getPrinterLog()){
                String pretty = pretty(parseSql(target));
                config.getLogger().debug("{} parsed ==> {}", mappedStatement.map(MappedStatement::getId).orElse(invocation.getMethod().getName()), pretty);
            }
        }catch (Exception e){
            config.getLogger().error("解析SQL 失败:{}", e);
        }
        return invocation.proceed();
    }


    private String parseSql(StatementHandler target){
        BoundSql boundSql = target.getBoundSql();
        String sql = boundSql.getSql();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings == null || parameterMappings.isEmpty()) {
            return sql;
        }
        Object parameterObject = boundSql.getParameterObject();

        TypeHandlerRegistry typeHandlerRegistry = config.getConfiguration().getTypeHandlerRegistry();
        MetaObject metaObject = parameterObject == null ? null : config.getConfiguration().newMetaObject(parameterObject);

        StringBuffer result = new StringBuffer();
        String regex = "\\?";
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(sql);
        int index = 0;
        while (matcher.find()){
            String valueAsString = "";
            if(index < parameterMappings.size()){
                Object value;
                ParameterMapping parameterMapping = parameterMappings.get(index);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    String propertyName = parameterMapping.getProperty();
                    if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else if (metaObject != null) {
                        value = metaObject.getValue(propertyName);
                    } else {
                        value = null;
                    }
                    valueAsString = format2String(value);
                }
            }
            matcher.appendReplacement(result, Matcher.quoteReplacement(valueAsString));
            index++;
        }
        matcher.appendTail(result);
        return result.toString();
    }

    private String format2String(Object e){
        if (Objects.isNull(e)) {
            return "";
        }
        if (e instanceof String) {
            String str = (String) e;
            str = str.replace("'", "''");
            return "'" + str + "'";
        }
        if (e instanceof LocalDateTime) {
            return "'" + ((LocalDateTime) e).format(DATE_TIME_FORMATTER) + "'";
        }
        if (e instanceof LocalDate) {
            return "'" + ((LocalDate) e).format(DATE_FORMATTER) + "'";
        }
        if (e instanceof java.util.Date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return "'" + sdf.format((Date) e) + "'";
        }
        return e.toString();
    }

    private String pretty(String sql){
        if(Objects.isNull(sql)){
            return null;
        }
        return sql.replaceAll("\n"," ").replaceAll("\\s+", " ");
    }

    @Override
    public Object plugin(Object target) {
        return target instanceof StatementHandler ? Plugin.wrap(target, this) : target;
    }

    @Override
    public void setProperties(Properties properties) {
    }


    private Optional<MappedStatement> getMappedStatement(StatementHandler ms) {
        try {
            return Optional.ofNullable(ms).map(e->{
                Field[] declaredFields = ms.getClass().getDeclaredFields();
                if(Objects.isNull(declaredFields)){
                    return null;
                }
                for (Field routingDelegate : declaredFields) {
                    routingDelegate.setAccessible(true);
                    try {
                        Object o = routingDelegate.get(ms);
                        if(o instanceof BaseStatementHandler){
                            for (Field field : o.getClass().getSuperclass().getDeclaredFields()) {
                                field.setAccessible(true);
                                Object fieldInBaseHandler = field.get(o);
                                if(fieldInBaseHandler instanceof MappedStatement){
                                    return  (MappedStatement) fieldInBaseHandler;
                                }
                            }
                        }
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                return null;
            });
        }catch (Exception e){
            config.getLogger().error("获取 statementId 失败:{}！", e);
        }
        return Optional.empty();
    }



}
