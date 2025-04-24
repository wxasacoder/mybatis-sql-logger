package com.biaoguoworks.chain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * @author wuxin
 * @date 2024/11/07 11:20:10
 */
public class AsyncChain<T> extends Chain<T>{

    private static final Logger log = LoggerFactory.getLogger(AsyncChain.class);

    // forkJoinPool default ThreadFactory product demon thread
    private Executor executor = ForkJoinPool.commonPool();

    private CompletableFuture future;

    @Override
    protected void doExecChain(T t) throws Exception {
        future = CompletableFuture.runAsync(()->{
            try {
                log.info("exec chain by async via {}", executor.getClass().getName());
                super.head.handle(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public void join(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
        if(Objects.nonNull(future)){
            future.get(timeout, unit);
        }
    }

    public void join(){
        if(Objects.nonNull(future)){
            future.join();
        }
    }

    public AsyncChain setExecutor(Executor executor) {
        this.executor = executor;
        return this;
    }
}
