package com.biaoguoworks.chain;

import java.util.Objects;

/**
 * @author wuxin
 * @date 2024/11/06 20:54:17
 */
public abstract class AbsHandler<T> {

    void handle(T t) throws Exception {
        try {
            doHandler(t);
        }catch (Exception e){
            onFailed(t, e);
        }
        doNext(t);
    }

    public void doNext(T t) throws Exception {
        if (Objects.nonNull(this.next)) {
            this.next.handle(t);
        }
    }


    public abstract void doHandler(T t) throws Exception;


    public void onFailed(T t, Exception e) throws Exception {
        throw  e;
    }

    public void onSuccess(T t) {
    }

    private AbsHandler<T> next;

    public final AbsHandler<T> setNext(AbsHandler<T> next) {
        this.next = next;
        return this;
    }
}
