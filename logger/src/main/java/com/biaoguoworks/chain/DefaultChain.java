package com.biaoguoworks.chain;

/**
 * @author wuxin
 * @date 2024/11/07 11:16:45
 */
public class DefaultChain<T> extends Chain<T>{

    @Override
    protected void doExecChain(T tHandler) throws Exception {
        super.head.handle(tHandler);
    }
}
