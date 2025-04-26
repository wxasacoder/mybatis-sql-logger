package com.biaoguoworks;

import com.biaoguoworks.config.Config;
import org.apache.ibatis.reflection.ExceptionUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

/**
 * PreparedStatement proxy to add logging.
 *
 * @author Clinton Begin
 * @author Eduardo Macarron
 */
public final class StatementLogger implements InvocationHandler {

  private final Statement statement;
  private final Config config;
  private final String sqlId;

  protected static final Set<String> EXECUTE_METHODS = new HashSet();

  static {
    EXECUTE_METHODS.add("execute");
    EXECUTE_METHODS.add("executeUpdate");
    EXECUTE_METHODS.add("executeQuery");
    EXECUTE_METHODS.add("addBatch");
  }

  private StatementLogger(Statement stmt, Config config , String sqlId) {
    this.statement = stmt;
    this.config = config;
    this.sqlId = sqlId;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
    try {
      if (Object.class.equals(method.getDeclaringClass())) {
        return method.invoke(this, params);
      }
      if (EXECUTE_METHODS.contains(method.getName())) {
        if("executeQuery".equals(method.getName())){
          ResultSet rs = (ResultSet) method.invoke(statement, params);
          return rs == null ? null : ResultSetLogger.newInstance(rs, config, sqlId);
        }else {
          return method.invoke(statement, params);
        }
      }else if ("getResultSet".equals(method.getName())) {
        ResultSet rs = (ResultSet) method.invoke(statement, params);
        return rs == null ? null : ResultSetLogger.newInstance(rs, config, sqlId);
      } else if ("getUpdateCount".equals(method.getName())) {
        int updateCount = (Integer) method.invoke(statement, params);
        if (updateCount != -1) {
          config.getLogger().debug("{}.UpdateCount: {}", sqlId, updateCount);
        }
        return updateCount;
      } else {
        return method.invoke(statement, params);
      }
    } catch (Throwable t) {
      throw ExceptionUtil.unwrapThrowable(t);
    }
  }

  /**
   * Creates a logging version of a PreparedStatement.
   * @return - the proxy
   */
  public static Statement newInstance(Statement stmt, Config config, String sqlId) {
    InvocationHandler handler = new StatementLogger(stmt, config, sqlId);
    ClassLoader cl = Statement.class.getClassLoader();
    return (Statement) Proxy.newProxyInstance(cl,
        new Class[] { PreparedStatement.class, CallableStatement.class }, handler);
  }

  /**
   * Return the wrapped prepared statement.
   *
   * @return the PreparedStatement
   */
  public Statement getPreparedStatement() {
    return statement;
  }

}
