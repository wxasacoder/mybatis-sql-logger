package com.biaoguoworks.predicate.handler;

import com.biaoguoworks.predicate.IsPrinterLogContext;

/**
 * @author wuxin
 * @date 2025/04/24 17:43:52
 */
public class LogAllSqlPredicate extends AbsPrinterLogPredicateHandlerAdapter {


    @Override
    public boolean predict(IsPrinterLogContext context) {
        return context.getConfig().getAllOpen();
    }
}
