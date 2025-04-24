package com.biaoguoworks.refresh;

import com.biaoguoworks.config.Config;
import com.biaoguoworks.predict.PredictType;

/**
 * @author wuxin
 * @date 2025/04/24 18:41:00
 */
public abstract  class AbsRefresh {

    Config config;

    abstract void setPredictType(PredictType predictType);
    abstract void setAllOpen(PredictType predictType);
    abstract void setSqlIds(PredictType predictType);


}
