package com.biaoguoworks;

import com.biaoguoworks.predicate.handler.AbsPrinterLogPredicateHandlerAdapter;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author wuxin
 * @date 2025/04/25 23:48:19
 */
public interface CustomPredicateHandlerSupplier extends Supplier<List<AbsPrinterLogPredicateHandlerAdapter>> {

}
