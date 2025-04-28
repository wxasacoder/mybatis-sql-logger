package com.biaoguoworks.predicate.handler;

import com.biaoguoworks.predicate.IsPrinterLogContext;

import java.util.Objects;

/**
 * @author wuxin
 * @date 2025/04/28 14:35:21
 */
public class SqIdPrefixSetPredicate extends AbsPrinterLogPredicateHandlerAdapter{
    @Override
    public boolean predict(IsPrinterLogContext context) {
        if(Objects.isNull(context.getConfig().getSqlIdPrefix()) || context.getConfig().getSqlIdPrefix().isEmpty()){
            return false;
        }
        if (!context.getMappedStatement().isPresent()) {
            return false;
        }
        String sqlId = context.getMappedStatement().get().getId();
        return context.getConfig().getSqlIdPrefix().stream().anyMatch(prefix -> sqlId.contains(prefix));
    }
}
