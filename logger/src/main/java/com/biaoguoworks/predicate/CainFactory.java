package com.biaoguoworks.predicate;

import com.biaoguoworks.chain.AbsHandler;
import com.biaoguoworks.chain.Chain;
import com.biaoguoworks.chain.DefaultChain;
import com.biaoguoworks.predicate.handler.LogAllSqlPredicate;
import com.biaoguoworks.predicate.handler.SqIdPermitSetPredicate;
import com.biaoguoworks.predicate.handler.SqIdPrefixSetPredicate;

/**
 * @author wuxin
 * @date 2025/04/24 18:31:58
 */
public class CainFactory {


    public static Chain<IsPrinterLogContext> createDefaultChain(){
        AbsHandler<IsPrinterLogContext> printerAllPredict = new LogAllSqlPredicate();
        AbsHandler<IsPrinterLogContext> sqlIdsPredict = new SqIdPermitSetPredicate();
        SqIdPrefixSetPredicate sqIdPrefixSetPredicate = new SqIdPrefixSetPredicate();
        return new DefaultChain<IsPrinterLogContext>()
                .add(printerAllPredict)
                .add(sqlIdsPredict)
                .add(sqIdPrefixSetPredicate)
                ;
    }


}
