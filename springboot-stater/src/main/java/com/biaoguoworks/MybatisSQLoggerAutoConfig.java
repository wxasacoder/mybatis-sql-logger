package com.biaoguoworks;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.biaoguoworks.chain.Chain;
import com.biaoguoworks.config.Config;
import com.biaoguoworks.predicate.CainFactory;
import com.biaoguoworks.predicate.IsPrinterLogContext;
import com.biaoguoworks.refresh.AbsConfigRefresh;
import com.biaoguoworks.refresher.RefresherType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.Arrays;
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

    private ObjectProvider<CustomPredicateHandlerSupplier> handlerSupplier;

    public MybatisSQLoggerAutoConfig(ObjectProvider<CustomPredicateHandlerSupplier> handlerSupplier) {
        this.handlerSupplier = handlerSupplier;
    }

    private String refresher = RefresherType.NACOS.name();

    private String prefix = "sql.parma.log";

    Config config = new Config();

    @Bean
    public ConfigurationCustomizer parmaSet2SqlconfigurationCustomizer() {
        return configuration ->{
            config.setConfiguration(configuration);
            config.setPrinterLogPredictChain(printerSqlLogPredicateChain());
            config.setLogger(LoggerFactory.getLogger(prefix));
            configuration.addInterceptor(new ParameterizedSqlLoggingInterceptor(config));
        };
    }

    @Bean
    public Chain<IsPrinterLogContext> printerSqlLogPredicateChain(){
        Chain<IsPrinterLogContext> defaultChain = CainFactory.createDefaultChain();
        Optional.ofNullable(handlerSupplier.getIfAvailable())
                .filter(e -> Objects.nonNull(e))
                .map(CustomPredicateHandlerSupplier::get)
                .filter(Objects::nonNull)
                .filter(e -> e.size() > 0)
                .ifPresent(e-> e.forEach(handler->{
                    defaultChain.add(handler);
                }));
        return defaultChain;
    }

    @Bean
    public AbsConfigRefresh nacosLoggerConfigRefresher() {
        RefresherType refresherType;
        try {
            refresherType = RefresherType.valueOf(this.refresher);
        }catch (Exception e ){
            refresherType  = RefresherType.NACOS;
        }
        return  DefaultObjectFactory.create(refresherType.getRefresherClass(),
                Arrays.asList(Config.class),
                Arrays.asList(config));
    }



    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        Optional.ofNullable(event.getApplicationContext().getBean(SqlSessionFactory.class).getConfiguration()).ifPresent(e->{
            String mybatisInterceptorChain = e.getInterceptors().stream().map(Interceptor::getClass).map(Class::toString).collect(Collectors.joining("\n"));
            config.getLogger().info("mybatis 初始化完成 拦截器的顺序为: {}",mybatisInterceptorChain);
        });
    }



}
