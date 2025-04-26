package com.biaoguoworks.predicate;

import com.biaoguoworks.config.Config;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Optional;

/**
 * @author wuxin
 * @date 2025/04/24 17:25:11
 */
@Data
@Accessors(chain = true)
public class IsPrinterLogContext {

    // ----------------- config ------------------
    private Optional<MappedStatement> mappedStatement;
    private Config config;

    // ------------------ result ------------------
    // log count
    private int predictCount = 0;
    // printer log or not
    private Boolean printerLog;

    public boolean addResult(boolean singleResult){
        this.printerLog  = Optional.ofNullable(this.printerLog).map(exist->{
            if(config.getPredictType() == PredictType.OR){
                return exist || singleResult;
            }
            if(config.getPredictType() == PredictType.AND){
                return exist && singleResult;
            }
            return false;
        }).orElse(singleResult);
        return this.printerLog;
    }

    public void  addPredictCount(int predictCount){
        this.predictCount+=predictCount;
    }


}
