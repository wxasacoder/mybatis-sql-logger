package com.biaoguoworks;

import com.biaoguoworks.config.Config;
import com.biaoguoworks.predicate.CainFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

/**
 * @author wuxin
 * @date 2025/04/27 00:06:50
 */
public class ExecSQL {

    private SqlSessionFactory sqlSessionFactory;

    private UserDao userDao;

    private SqlSession sqlSession;

    @Before
    public void before() {
        try (Reader reader = Resources.getResourceAsReader("mybatis-config-custom.xml")) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            Configuration configuration = sqlSessionFactory.getConfiguration();
            // Interceptor config
            Config config = new Config();
            config.setConfiguration(configuration);
            config.setPrinterLogPredictChain(CainFactory.createDefaultChain());
            config.setLogger(LoggerFactory.getLogger("wx.logger"));
            config.setAllOpen(true);
            configuration.addInterceptor(new ParameterizedSqlLoggingInterceptor(config));

            sqlSession = sqlSessionFactory.openSession();
            userDao = sqlSession.getMapper(UserDao.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void simpleQuery(){
        userDao.selectNameAndIds("wx", new ArrayList<>(){{
            add(1L);
            add(2L);
            add(3L);
        }});
    }

    @After
    public void afterAll(){
        sqlSession.close();
    }




}
