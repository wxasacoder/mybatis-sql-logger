package com.biaoguoworks.db.mork.driver;

/**
 * @author wuxin
 * @date 2025/04/26 23:33:14
 */
public class UtilityElf {

    public static  void  quietlySleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public static  void quietlySleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
