package com.biaoguoworks;

import com.biaoguoworks.config.Config;
import com.biaoguoworks.predict.CainFactory;
import com.biaoguoworks.db.mork.UserDao;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
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
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author wuxin
 * @date 2025/04/25 14:40:22
 */
public class CustomVsNativeLogger4mybatis {


    @Benchmark()
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void execQueryWithNativeLog(Blackhole blackhole, TestData testData, MybatisNativeQueryWithNativeLog nativeQueryWithNativeLog) {
        try (SqlSession sqlSession = nativeQueryWithNativeLog.getSqlSessionFactory().openSession()) {
            UserDao mapper = sqlSession.getMapper(UserDao.class);
            blackhole.consume(mapper.selectNameAndIds(testData.getName(), testData.getIds()));
        }
    }

    @Benchmark()
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void execQueryWithCustomLog(Blackhole blackhole, TestData testData, MybatisQueryWithCustomLog customLog) {
        try (SqlSession sqlSession = customLog.getSqlSessionFactory().openSession()) {
            UserDao mapper = sqlSession.getMapper(UserDao.class);
            blackhole.consume(mapper.selectNameAndIds(testData.getName(), testData.getIds()));
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options build = new OptionsBuilder().include(CustomVsNativeLogger4mybatis.class.getSimpleName())
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

    @State(Scope.Benchmark)
    public static class MybatisQueryWithCustomLog{
        SqlSessionFactory sqlSessionFactory;
        public  MybatisQueryWithCustomLog() {
            try (Reader reader = Resources.getResourceAsReader("mybatis-config-custom.xml")) {
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
                Configuration configuration = sqlSessionFactory.getConfiguration();
                // Interceptor config
                Config config = new Config();
                config.setAllOpen(true);
                config.setConfiguration(configuration);
                config.setPrinterLogPredictChain(CainFactory.createDefaultChain());
                config.setLogger(LoggerFactory.getLogger("wx.logger"));
                configuration.addInterceptor(new ParameterizedSqlLoggingInterceptor(config));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public SqlSessionFactory getSqlSessionFactory() {
            return sqlSessionFactory;
        }
    }


}
