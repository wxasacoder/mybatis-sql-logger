package com.biaoguoworks.predict;

import com.biaoguoworks.chain.AbsHandler;
import com.biaoguoworks.chain.Chain;
import com.biaoguoworks.chain.DefaultChain;
import com.biaoguoworks.predict.handler.PrinterAllPredict;
import com.biaoguoworks.predict.handler.SqlIdsPredict;

/**
 * @author wuxin
 * @date 2025/04/24 18:31:58
 */
public class CainFactory {


    public static Chain<IsPrinterLogContext> createDefaultChain(){
        AbsHandler<IsPrinterLogContext> printerAllPredict = new PrinterAllPredict();
        AbsHandler<IsPrinterLogContext> sqlIdsPredict = new SqlIdsPredict();
        return new DefaultChain<IsPrinterLogContext>()
                .add(printerAllPredict)
                .add(sqlIdsPredict);
    }


}
