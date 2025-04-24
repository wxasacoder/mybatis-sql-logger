package com.biaoguoworks.chain;

import java.util.Objects;

/**
 * @author wuxin
 * @date 2024/11/06 20:53:23
 */
public abstract class Chain<T> {

    protected AbsHandler<T> head;

    protected AbsHandler<T> tail;

    public Chain<T> execChain(T t) throws Exception {
        if(Objects.isNull(head)){
            return this;
        }
        doExecChain(t);
        return this;
    }

    protected abstract void doExecChain(T t) throws Exception;


    public Chain<T> add(AbsHandler<T> absHandler) {
        if(Objects.isNull(this.tail)){
            this.head = absHandler;
        }else {
            this.tail.setNext(absHandler);
        }
        this.tail = absHandler;
        return this;
    }

}
