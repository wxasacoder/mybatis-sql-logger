package com.biaoguoworks.predict.handler;

import com.biaoguoworks.chain.AbsHandler;
import com.biaoguoworks.predict.IsPrinterLogContext;
import com.biaoguoworks.predict.PredictType;

/**
 * @author wuxin
 * @date 2025/04/24 18:12:42
 */
public abstract class AbsPrinterLogPredictHandlerAdapter extends AbsHandler<IsPrinterLogContext> {

    @Override
    public void doHandler(IsPrinterLogContext isPrinterLogContext) throws Exception {
        isPrinterLogContext.addResult(predict(isPrinterLogContext));
        doNext(isPrinterLogContext);
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
