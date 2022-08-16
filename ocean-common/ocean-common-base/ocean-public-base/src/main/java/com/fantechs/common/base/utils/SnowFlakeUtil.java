package com.fantechs.common.base.utils;

import java.util.Random;

public class SnowFlakeUtil {
    /**
     * 单例
     */
    private static IdWorker snowFlake = new IdWorker(5, 6);


    /**
     * 随机数生成器
     */
    private static Random random = new Random();

    /**
     * 通过循环一次或两次解决毫秒数不连续导致的全为偶数问题
     * 时间停顿 第一次一定是偶数 第二次一定是基数 随机 实现奇数偶数混合
     *
     * @return
     */
    public static Long getUid() {
        Long uid = null;
        for (int i = 0; i < random.nextInt(2) + 1; i++) {
            uid = snowFlake.nextId();
        }
        return uid;
    }
}
