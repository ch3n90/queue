package me.ch3ng.queue.wheeltimer;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ch3ng
 * @date 2023/6/14 19:20
 * @description:
 *
 * wheel timer core
 * 时间轮内核
 */
public class Worker implements Runnable{
    private static final int MAX_TASK = 10000;
    private AtomicInteger currentTaskCount = new AtomicInteger(0);
    private long startTime = System.nanoTime();
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private TimeUnit timeUnit;
    private long tick = 0;
    private long tickDuration;
    private int bucketSize;
    private WheelTimerStatus status;
    private long wheelDuration;

    private WheelBucket[] wheelBuckets;


    public Worker(long tickDuration,int bucketSize,TimeUnit timeUnit,WheelBucket[] wheelBuckets){
        this.tickDuration = tickDuration;
        this.wheelBuckets = wheelBuckets;
        this.timeUnit = timeUnit;
        this.bucketSize = bucketSize;
        this.status = WheelTimerStatus.INIT;
        this.wheelDuration = this.tickDuration * this.bucketSize;
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

            timeouts.forEach(timeout -> {
                executorService.execute(timeout);
                currentTaskCount.decrementAndGet();
            });
            tick++;

        }while (this.status == WheelTimerStatus.RUNNING);

    }

    protected void addTask(long delay,TimeUnit delayTimeUnit,TimerTask timerTask){
        int count = this.currentTaskCount.get();
        if(MAX_TASK <= count){
            throw new IllegalStateException("Task too mush,current task count:" + count);
        }
        long delayNanos = delayTimeUnit.toNanos(delay);
        long wheelTimerDurationNanos = this.timeUnit.toNanos(this.wheelDuration);

        //计算时间轮round层数
        long round = delayNanos / wheelTimerDurationNanos;

        //计算bucket索引
        //从当前时针所在位置开始计算延迟后索引位置
        long tickDurationNanos = this.timeUnit.toNanos(this.tickDuration);
        //当前时间轮指针位置
        int idx = (int) (tick % bucketSize);
        long slotMod = delayNanos % tickDurationNanos;
        int needSlot = (int)(slotMod == 0 ? delayNanos / tickDurationNanos : delayNanos / tickDurationNanos + 1);
        //剩余slot的数量
        int hasSlot = this.bucketSize - idx;
        int bucketIdx;
        if(hasSlot >= needSlot){
            bucketIdx = needSlot - 1;
        } else {
            bucketIdx = (needSlot - hasSlot) % this.bucketSize - 1;
        }

        timerTask.bucketIdx = bucketIdx;
        timerTask.round = round;
        timerTask.status = TimerTaskStatus.PENDING;

        //命中 wheelBucket
        WheelBucket wheelBucket = this.wheelBuckets[bucketIdx];
        wheelBucket.push(timerTask);
        this.currentTaskCount.incrementAndGet();
    }

    private void waitForNextTick() {
        //TODO 如果时间轮中的无任务，或者跨度非常大，那么此时时间轮会进行空转浪费性能
        //TODO 针对这种情况可以阻塞该进程，释放cpu执行权，直到任务临近时，唤醒该进程，触发内核运行
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

}
