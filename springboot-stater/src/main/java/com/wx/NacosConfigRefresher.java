package com.wx;

import com.biaoguoworks.config.Config;
import com.biaoguoworks.predict.PredictType;
import com.biaoguoworks.refresh.AbsRefresh;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wuxin
 * @date 2025/04/24 21:25:41
 */
@ConfigurationProperties(prefix = "mybatis.param.set.sql.log.predict")
@RefreshScope
public class NacosConfigRefresher extends AbsRefresh {

    private static final Logger log = LoggerFactory.getLogger(NacosConfigRefresher.class);

    private String predictType = PredictType.OR.name();

    private Boolean allOpen = false;

    private Set<String> sqlIds = new HashSet<>();

    public NacosConfigRefresher(Config config) {
        super(config);
    }

    public void setPredictType(String predictType) {
        log.info("set predict type : {}", predictType);
        super.setPredictType(PredictType.valueOf(predictType));
    }

    public void setAllOpen(Boolean allOpen) {
        log.info("set all open : {}", allOpen);
        super.setAllOpen(allOpen);
    }

    @Override
    public void setSqlIds(Set<String> sqlIds) {
        log.info("set sqlIds : {}", sqlIds);
        super.setSqlIds(sqlIds);
    }
}
