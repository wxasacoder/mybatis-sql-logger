package com.biaoguoworks.config;

import com.biaoguoworks.chain.Chain;
import com.biaoguoworks.chain.DefaultChain;
import com.biaoguoworks.predict.IsPrinterLogContext;
import com.biaoguoworks.predict.PredictType;
import com.biaoguoworks.predict.handler.AbsPrinterLogPredictHandlerAdapter;
import lombok.Data;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;

import java.util.Set;

/**
 * @author wuxin
 * @date 2025/04/24 17:04:28
 */
@Data
public class Config {

    private Logger logger;

    private Configuration configuration;

    private Chain<IsPrinterLogContext> printerLogPredictChain = new DefaultChain<>();


    public void addPredictChainHandler(AbsPrinterLogPredictHandlerAdapter absHandler){
        printerLogPredictChain.add(absHandler);
    }

    //////  参数 ////////
    PredictType predictType;
    private Boolean allOpen = false;
    private Set<String> sqlIds;

}
