package com.biaoguoworks.refresh;

import com.biaoguoworks.config.Config;
import com.biaoguoworks.predict.PredictType;

import java.util.Set;

/**
 * @author wuxin
 * @date 2025/04/24 18:41:00
 */
public abstract class AbsRefresh {

    private Config config;

    public AbsRefresh(Config config) {
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


}
