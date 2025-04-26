package com.biaoguoworks;

import com.biaoguoworks.config.Config;
import org.apache.ibatis.reflection.ExceptionUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;

public final class ResultSetLogger implements InvocationHandler {
    private int rows;
    private String sqlId;
    private final ResultSet rs;
    private final Config config;

    private ResultSetLogger(ResultSet rs, String sqlId, Config config) {
        this.rs = rs;
        this.sqlId = sqlId;
        this.config = config;
    }

    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
        try {
            if (Object.class.equals(method.getDeclaringClass())) {
                return method.invoke(this, params);
            } else {
                Object o = method.invoke(this.rs, params);
                if ("next".equals(method.getName())) {
                    if ((Boolean)o) {
                        ++this.rows;
                    } else {
                        this.config.getLogger().debug("{}.QueryCount: {}",sqlId, this.rows);
                    }
                }
                return o;
            }
        } catch (Throwable var7) {
            Throwable t = var7;
            throw ExceptionUtil.unwrapThrowable(t);
        }
    }


    public static ResultSet newInstance(ResultSet rs,Config config, String sqlId) {
        InvocationHandler handler = new ResultSetLogger(rs, sqlId, config);
        ClassLoader cl = ResultSet.class.getClassLoader();
        return (ResultSet)Proxy.newProxyInstance(cl, new Class[]{ResultSet.class}, handler);
    }

    public ResultSet getRs() {
        return this.rs;
    }
}
