package com.biaoguoworks.predicate.handler;

import com.biaoguoworks.chain.AbsHandler;
import com.biaoguoworks.predicate.IsPrinterLogContext;
import com.biaoguoworks.predicate.PredictType;

/**
 * @author wuxin
 * @date 2025/04/24 18:12:42
 */
public abstract class AbsPrinterLogPredicateHandlerAdapter extends AbsHandler<IsPrinterLogContext> {

    @Override
    public void doHandler(IsPrinterLogContext isPrinterLogContext) throws Exception {
        isPrinterLogContext.addResult(predict(isPrinterLogContext));
    }

    @Override
    public void doNext(IsPrinterLogContext context) throws Exception {
        if(PredictType.OR == context.getConfig().getPredictType() && context.getPrinterLog()){
            return;
        }
        if(PredictType.AND == context.getConfig().getPredictType() && !context.getPrinterLog()){
            return;
        }
        super.doNext(context);
    }

    public abstract boolean predict(IsPrinterLogContext context);

}
