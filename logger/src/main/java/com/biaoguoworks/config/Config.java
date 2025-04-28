package com.biaoguoworks.config;

import com.biaoguoworks.chain.Chain;
import com.biaoguoworks.chain.DefaultChain;
import com.biaoguoworks.predicate.IsPrinterLogContext;
import com.biaoguoworks.predicate.PredictType;
import com.biaoguoworks.predicate.handler.AbsPrinterLogPredicateHandlerAdapter;
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


    public void addPredictChainHandler(AbsPrinterLogPredicateHandlerAdapter absHandler){
        printerLogPredictChain.add(absHandler);
    }

    //////  参数 ////////
    PredictType predictType = PredictType.OR;
    private Boolean allOpen = false;
    private Set<String> sqlIds;
    private Set<String> sqlIdPrefix;

}
