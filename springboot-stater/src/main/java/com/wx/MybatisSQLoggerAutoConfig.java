package com.wx;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.biaoguoworks.ParameterizedSqlLoggingInterceptor;
import com.biaoguoworks.config.Config;
import com.biaoguoworks.predict.CainFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wuxin
 * @date 2025/04/24 18:57:42
 */
@Configuration
@ConditionalOnProperty(prefix = "mybatis.param.set.sql.log.printer")
@ConfigurationProperties(prefix = "mybatis.param.set.sql.log.printer")
public class MybatisSQLoggerAutoConfig {

    @Bean
    @ConditionalOnProperty(
            name = "mybatis.param.set.sql.log.printer.open",
            havingValue = "true"
    )
    public ConfigurationCustomizer parmaSet2SqlconfigurationCustomizer() {
        return configuration ->{
            Config config = new Config();
            config.setConfiguration(configuration);
            config.setPrinterLogPredictChain(CainFactory.creatChain());
            configuration.addInterceptor(new ParameterizedSqlLoggingInterceptor(config));
        };
    }



}
