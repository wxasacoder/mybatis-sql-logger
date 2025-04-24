package com.wx;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

/**
 * @author wuxin
 * @date 2025/04/25 00:24:28
 */
public class ReflectiveVSNoneReflective {
    /*
        Benchmark                                                Mode  Cnt       Score      Error   Units
        ReflectiveVSNoneReflective.getWay                       thrpt    5  353977.418 ±  460.733  ops/ms
        ReflectiveVSNoneReflective.reflectWay                   thrpt    5   95759.272 ± 1046.421  ops/ms
        ReflectiveVSNoneReflective.reflectWayCacheField         thrpt    5  169423.298 ± 1890.430  ops/ms
        ReflectiveVSNoneReflective.getWayAvgTime                 avgt    5       2.826 ±    0.015   ns/op
        ReflectiveVSNoneReflective.reflectWayAvgTime             avgt    5      10.804 ±    0.698   ns/op
        ReflectiveVSNoneReflective.reflectWayCacheFieldAvgTime   avgt    5       5.754 ±    0.115   ns/op
     */

    @Benchmark()
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void getWayAvgTime(Blackhole blackhole,User user) {
        blackhole.consume(getRole(user));
    }

    @Benchmark()
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void getWay(Blackhole blackhole,User user) {
        blackhole.consume(getRole(user));
    }

    @Benchmark()
    @BenchmarkMode({Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void reflectWayAvgTime(Blackhole blackhole, User user) throws IllegalAccessException {
        blackhole.consume(getRoleByReflective(user));
    }

    @Benchmark()
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void reflectWay(Blackhole blackhole, User user) throws IllegalAccessException {
        blackhole.consume(getRoleByReflective(user));
    }

    @Benchmark()
    @BenchmarkMode({Mode.AverageTime})
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void reflectWayCacheFieldAvgTime(Blackhole blackhole, User user, SharedState sharedState) throws IllegalAccessException {
        blackhole.consume(getRoleByReflectiveCacheField(sharedState, user));
    }

    @Benchmark()
    @BenchmarkMode({Mode.Throughput})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void reflectWayCacheField(Blackhole blackhole, User user, SharedState sharedState) throws IllegalAccessException {
        blackhole.consume(getRoleByReflectiveCacheField(sharedState, user));
    }


    public static void main(String[] args) throws RunnerException {
        Options build = new OptionsBuilder().include(ReflectiveVSNoneReflective.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(build).run();
    }

    public final static String getRole(User user){
        return user.getRole().getName();
    }


    public final static String getRoleByReflective(User user) throws IllegalAccessException {
        Class<? extends User> userClass = user.getClass();
        for (Field declaredField : userClass.getDeclaredFields()) {
            declaredField.setAccessible(true);
            Object o = declaredField.get(user);
            if(o instanceof Role){
                Role o1 = (Role) o;
                for (Field field : o1.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    if(field.getName().equals("name")){
                        return (String)field.get(o1);
                    }
                }
            }
        }
        return null;
    }


    public final static String getRoleByReflectiveCacheField(SharedState sharedState, User user) throws IllegalAccessException {
        Role role = (Role) sharedState.getRoleFile().get(user);
        return (String)sharedState.getNameFiled().get(role);
    }




    @State(Scope.Benchmark)
    public static class SharedState {
        private Field roleFile;
        private Field nameFiled;

        @Setup(Level.Trial)
        public void setup() throws NoSuchFieldException {
            // 初始化反射字段
            roleFile = User.class.getDeclaredField("role");
            roleFile.setAccessible(true);

            nameFiled = Role.class.getDeclaredField("name");
            nameFiled.setAccessible(true);
        }

        public Field getRoleFile() {
            return roleFile;
        }

        public Field getNameFiled() {
            return nameFiled;
        }
    }

    @State(Scope.Benchmark)
    public static class User {



        private int value = 42;
        private Role  role = new Role();

        public int getValue() {
            return value;
        }

        public Role getRole() {
            return role;
        }
    }

    public static class Role{


        private String name = "master";


        public String getName() {
            return name;
        }
    }
}