package com.wx;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.biaoguoworks.ParameterizedSqlLoggingInterceptor;
import com.biaoguoworks.config.Config;
import com.biaoguoworks.predict.CainFactory;
import com.biaoguoworks.refresh.AbsRefresh;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author wuxin
 * @date 2025/04/24 18:57:42
 */
@Configuration
@ConditionalOnProperty(
        name = "mybatis.param.set.sql.log.open",
        havingValue = "true"
)
@ConfigurationProperties(prefix = "mybatis.param.set.sql.log.config")
public class MybatisSQLoggerAutoConfig {

    private String refresher = "NACOS";

    private String prefix = "sql.parma.log";

    Config config = new Config();

    @Bean
    public ConfigurationCustomizer parmaSet2SqlconfigurationCustomizer() {
        return configuration ->{
            config.setConfiguration(configuration);
            config.setPrinterLogPredictChain(CainFactory.createDefaultChain());
            config.setLogger(LoggerFactory.getLogger(prefix));
            configuration.addInterceptor(new ParameterizedSqlLoggingInterceptor(config));
        };
    }

    @Bean
    public AbsRefresh nacosConfigRefresher() {
        return Optional.ofNullable(this.refresher)
                .filter(e-> Objects.nonNull(e) && e.length() > 0)
                .map(e->{
                    if(this.refresher.toUpperCase().equals("NACOS")){
                        return new NacosConfigRefresher(config);
                    }
                    return new NacosConfigRefresher(config);
                }).orElse(new NacosConfigRefresher(config));
    }



    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        Optional.ofNullable(event.getApplicationContext().getBean(SqlSessionFactory.class).getConfiguration()).ifPresent(e->{
            String mybatisInterceptorChain = e.getInterceptors().stream().map(Interceptor::getClass).map(Class::toString).collect(Collectors.joining("\n"));
            config.getLogger().info("mybatis 初始化完成 拦截器的顺序为: {}",mybatisInterceptorChain);
        });
    }



}
