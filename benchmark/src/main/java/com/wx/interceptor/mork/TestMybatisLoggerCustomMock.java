package com.wx.interceptor.mork;

import com.biaoguoworks.ParameterizedSqlLoggingInterceptor;
import com.biaoguoworks.config.Config;
import com.biaoguoworks.predict.CainFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

/**
 * @author wuxin
 * @date 2025/04/25 12:19:51
 */
public class TestMybatisLoggerCustomMock {
    private static SqlSessionFactory sqlSessionFactory;

    public static void main(String[] args) {
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            UserDao mapper = sqlSession.getMapper(UserDao.class);
            String s = mapper.selectNameAndIds("hello", new ArrayList<Long>() {{
                add(1L);
                add(1L);
            }});
            System.out.println(s);
        }

    }


}
