package com.biaoguoworks.refresh;

import com.biaoguoworks.config.Config;
import com.biaoguoworks.predicate.PredictType;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wuxin
 * @date 2025/04/24 18:41:00
 */
public abstract class AbsConfigRefresh {

    private Config config;

    public AbsConfigRefresh(Config config) {
        this.config = config;
    }

    public void setPredictType(PredictType predictType){
        this.config.setPredictType(predictType);
    }
    public void setAllOpen(Boolean predictType){
        this.config.setAllOpen(predictType);
    }
    public void setSqlIds(Set<String> sqlIds){
        this.config.setSqlIds(sqlIds);
    }

    public void setSqlIdPrefix(Set<String> sqlIdPrefix){
        if(Objects.nonNull(sqlIdPrefix)){
            sqlIdPrefix = sqlIdPrefix.stream()
                    .sorted(Comparator.comparing(a -> (a.length())))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        this.config.setSqlIdPrefix(sqlIdPrefix);
    }

}
