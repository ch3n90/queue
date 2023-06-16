package me.ch3ng.queue.wheeltimer;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author ch3ng
 * @date 2023/6/14 19:20
 * @description:
 *
 * wheel timer core
 * 时间轮内核
 */
public class Worker implements Runnable{
    private long startTime = System.nanoTime();
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private TimeUnit timeUnit;
    private long tick = 0;
    private long tickDuration;
    private int bucketSize;
    private WheelTimerStatus status;

    private WheelBucket[] wheelBuckets;


    public Worker(long tickDuration,int bucketSize,TimeUnit timeUnit,WheelBucket[] wheelBuckets){
        this.tickDuration = tickDuration;
        this.wheelBuckets = wheelBuckets;
        this.timeUnit = timeUnit;
        this.bucketSize = bucketSize;
        this.status = WheelTimerStatus.INIT;

    }

    @Override
    public void run() {
        this.status = WheelTimerStatus.RUNNING;

        do {
            waitForNextTick();
            int idx = (int) (tick % bucketSize);
            //get timeout task from wheel bucket
            WheelBucket wheelBucket = wheelBuckets[idx];

            // process got canceled timeout
            List<TimerTask> canceledTimerTask = wheelBucket.processCanceledTimerTask();

            //process timeout task
            List<TimerTask> timeouts = wheelBucket.poll();

            timeouts.forEach(timeout -> executorService.execute(timeout));

            tick++;

        }while (this.status == WheelTimerStatus.RUNNING);

    }

    private void waitForNextTick() {
        long deadline = timeUnit.toNanos(tickDuration) * (tick + 1);

        for (;;) {
            final long currentTime = System.nanoTime() - startTime;

            long sleepTimeMs = (deadline - currentTime + 999999) / 1000000;
            if (sleepTimeMs <= 0) {
               return;
            }

            try {
                Thread.sleep(sleepTimeMs);
//                System.out.println("wheel timer point mov ---> " + this.tick + " ----> " + sleepTimeMs);
            } catch (InterruptedException e) {
                e.fillInStackTrace();
            }
        }
    }

    protected void stop(){
        this.status = WheelTimerStatus.SHUTDOWN;
        executorService.shutdownNow();
    }

    protected int currentBucketIdx(){
        return (int) (tick % bucketSize);
    }
}
