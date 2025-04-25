package com.biaoguoworks.utils;

import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;

/**
 * @author wuxin
 * @date 2025/04/25 15:47:08
 */
public class ReflectiveUtils {

    static Field statementHandlerInRoutingHandler = null;
    static Field mappedStatementFiledInBaseStatementHandler = null;

    static {
        try {
            for (Field filedInRouting : RoutingStatementHandler.class.getDeclaredFields()) {
                Type genericType = filedInRouting.getGenericType();
                if (genericType instanceof Class){
                    Class delegate = (Class) genericType;
                    if(delegate.isAssignableFrom(StatementHandler.class)){
                        statementHandlerInRoutingHandler = filedInRouting;
                        statementHandlerInRoutingHandler.setAccessible(true);
                        break;
                    }
                }
            }
            for (Field baseHandlerFiled : BaseStatementHandler.class.getDeclaredFields()) {
                Type genericType = baseHandlerFiled.getGenericType();
                if(genericType instanceof Class){
                    Class classedFiled = (Class) genericType;
                    if(classedFiled.isAssignableFrom(MappedStatement.class)){
                        mappedStatementFiledInBaseStatementHandler = baseHandlerFiled;
                        mappedStatementFiledInBaseStatementHandler.setAccessible(true);
                        break;
                    }
                }
            }
        }catch (Exception e){

        }
    }

    public static Optional<MappedStatement> getMappedStatementFromCacheFiled(StatementHandler statementHandler){
        try {
            if(statementHandlerInRoutingHandler == null){
                return  Optional.empty();
            }
            if(mappedStatementFiledInBaseStatementHandler == null){
                return  Optional.empty();
            }
            if(statementHandler == null) {
                return Optional.empty();
            }
            Object o = statementHandlerInRoutingHandler.get(statementHandler);
            return Optional.ofNullable((MappedStatement)mappedStatementFiledInBaseStatementHandler.get(o));
        }catch (Exception e){
            return Optional.empty();
        }
    }


    public static Optional<MappedStatement> getMappedStatement(StatementHandler ms) {
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
            return Optional.empty();
        }
    }

    public static Optional<MappedStatement> getMappedStatementMaxEffort(StatementHandler ms) {
        Optional<MappedStatement> mappedStatementFromCacheFiled = getMappedStatementFromCacheFiled(ms);
        if(mappedStatementFromCacheFiled.isPresent()){
            return mappedStatementFromCacheFiled;
        }
        Optional<MappedStatement> mappedStatement = getMappedStatement(ms);
        return mappedStatement;
    }



}
