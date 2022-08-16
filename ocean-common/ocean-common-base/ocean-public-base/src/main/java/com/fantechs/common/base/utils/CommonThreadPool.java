package com.fantechs.common.base.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 线程池
 * @author leifengzhi
 *
 */
public class CommonThreadPool {

	private static Executor executor;
	private static int THREAD_NUM = 20;
	
	private CommonThreadPool(){}
	
	
	private static Executor getExecutor(){
		if (executor == null){
			synchronized (CommonThreadPool.class) {
				if (executor == null){
					//控制线程最大并发数，超出的线程会在队列中等待
					executor = Executors.newFixedThreadPool(THREAD_NUM);
				}
			}
		}
		return executor;
	}
	
	public static void runTask(Runnable task){
		getExecutor().execute(task);
	}
	
	public static void runFtpTask(Runnable task){
		getExecutor().execute(task);
	}
}

