package com.biaoguoworks.predicate.handler;

import com.biaoguoworks.predicate.IsPrinterLogContext;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Objects;
import java.util.Set;

/**
 *
 * @author wuxin
 * @date 2025/04/24 18:24:58
 *
 */
public class SqIdPermitSetPredicate extends AbsPrinterLogPredicateHandlerAdapter {

    private static final String NEVER_DUPLICATED = "129097c6-b65f-43bf-ba30-d2874c17eda1";

    @Override
    public boolean predict(IsPrinterLogContext context) {
        Set<String> sqlIds = context.getConfig().getSqlIds();
        if(Objects.isNull(sqlIds) || sqlIds.size() == 0){
            return false;
        }
        return sqlIds.contains(context.getMappedStatement().map(MappedStatement::getId).orElse(NEVER_DUPLICATED));
    }
}
