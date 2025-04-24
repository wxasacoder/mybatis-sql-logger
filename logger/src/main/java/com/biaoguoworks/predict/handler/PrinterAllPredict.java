package com.biaoguoworks.predict.handler;

import com.biaoguoworks.predict.IsPrinterLogContext;

/**
 * @author wuxin
 * @date 2025/04/24 17:43:52
 */
public class PrinterAllPredict extends AbsPrinterLogPredictHandlerAdapter {


    @Override
    public boolean predict(IsPrinterLogContext context) {
        return context.getConfig().getAllOpen();
    }
}
