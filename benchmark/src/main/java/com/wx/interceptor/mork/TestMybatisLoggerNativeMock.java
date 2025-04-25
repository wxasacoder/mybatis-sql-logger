package com.wx.interceptor.mork;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

/**
 * @author wuxin
 * @date 2025/04/25 12:19:51
 */
public class TestMybatisLoggerNativeMock {
    private static SqlSessionFactory sqlSessionFactory;

    public static void main(String[] args) {
        try (Reader reader = Resources.getResourceAsReader("mybatis-config-native.xml")) {
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            sqlSessionFactory.getConfiguration().setLogPrefix("mybatis.logger");
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
