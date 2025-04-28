package com.biaoguoworks.refresher;

import com.biaoguoworks.config.Config;
import com.biaoguoworks.predicate.PredictType;
import com.biaoguoworks.refresh.AbsConfigRefresh;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wuxin
 * @date 2025/04/24 21:25:41
 */
@ConfigurationProperties(prefix = "mybatis.param.set.sql.log.predicate")
@RefreshScope
public class NacosConfigRefresher extends AbsConfigRefresh {

    private static final Logger log = LoggerFactory.getLogger(NacosConfigRefresher.class);

    private String predictType = PredictType.OR.name();

    private Boolean allOpen = false;

    private Set<String> sqlIds = new HashSet<>();

    public NacosConfigRefresher(Config config) {
        super(config);
    }

    public void setPredictType(String predictType) {
        log.info("set predicate type : {}", predictType);
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

    @Override
    public void setSqlIdPrefix(Set<String> sqlIdPrefix) {
        super.setSqlIdPrefix(sqlIdPrefix);
    }
}
