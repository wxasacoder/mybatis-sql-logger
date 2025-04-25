package com.wx.interceptor;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author wuxin
 * @date 2025/04/25 14:40:22
 */
public class HasInterceptorCompare {


    @Benchmark()
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void execQueryWithNativeLog(Blackhole blackhole, TestData testData, MybatisNativeQueryWithNativeLog nativeQueryWithNativeLog) {
        try (SqlSession sqlSession = nativeQueryWithNativeLog.getSqlSessionFactory().openSession()) {
            UserDao mapper = sqlSession.getMapper(UserDao.class);
            blackhole.consume(mapper.selectNameAndIds(testData.getName(), testData.getIds()));
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options build = new OptionsBuilder().include(HasInterceptorCompare.class.getSimpleName())
                .forks(1)
                .result("./benchmark-mybatis-with-logger.txt")
                .resultFormat(ResultFormatType.TEXT)
                .build();
        new Runner(build).run();
    }


    @State(Scope.Benchmark)
    public static class TestData{
        private String name = "wx";
        private List<Long> ids = new ArrayList<Long>(){{
            add(1L);
            add(2L);
            add(3L);
            add(4L);
            add(5L);
            add(6L);
            add(11L);
            add(12L);
            add(13L);
            add(14L);
            add(15L);
            add(16L);
        }};

        public String getName() {
            return name;
        }

        public List<Long> getIds() {
            return ids;
        }
    }

    @State(Scope.Benchmark)
    public static class MybatisNativeQueryWithNativeLog{
        SqlSessionFactory sqlSessionFactory;
        public  MybatisNativeQueryWithNativeLog() {
            try (Reader reader = Resources.getResourceAsReader("mybatis-config-native.xml")) {
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
                sqlSessionFactory.getConfiguration().setLogPrefix("mybatis.logger");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public SqlSessionFactory getSqlSessionFactory() {
            return sqlSessionFactory;
        }
    }


}
