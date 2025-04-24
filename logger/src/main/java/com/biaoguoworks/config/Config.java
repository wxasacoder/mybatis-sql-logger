package com.biaoguoworks.config;

import com.biaoguoworks.chain.Chain;
import com.biaoguoworks.predict.IsPrinterLogContext;
import com.biaoguoworks.predict.PredictType;
import lombok.Data;
import org.apache.ibatis.session.Configuration;
import org.apache.logging.log4j.Logger;

import java.util.Set;

/**
 * @author wuxin
 * @date 2025/04/24 17:04:28
 */
@Data
public class Config {

    private Logger logger;

    private Configuration configuration;

    private Chain<IsPrinterLogContext> printerLogPredictChain;

    //////  参数 ////////
    PredictType predictType;
    private Boolean allOpen = false;
    private Set<String> sqlIds;

}
