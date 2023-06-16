package me.ch3ng.queue.wheeltimer;

import java.lang.Thread.State;
import java.util.concurrent.TimeUnit;

/**
 * @author ch3ng
 * @date 2023/6/14 17:36
 * @description:
 */
public class WheelTimer {


    /**
     * the wheel bucket time
     */
    private long tickDuration = 10;

    /**
     * the bucket size
     */
    private int bucketSize = 128;

    /**
     * the wheel timer a round duration
     */
    private long wheelDuration = tickDuration * bucketSize;

    /**
     * the wheel timer time unit
     */
    private TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    private WheelBucket[] wheelBuckets;

    private Worker worker;
    private Thread workerThread;

    public WheelTimer(long tickDuration, int bucketSize, TimeUnit timeUnit) {
        this.tickDuration = tickDuration;
        this.bucketSize = bucketSize;
        this.wheelDuration = tickDuration * bucketSize;
        this.timeUnit = timeUnit;
        this.wheelBuckets = new WheelBucket[bucketSize];
        for (int i = 0; i < wheelBuckets.length; i ++) {
            wheelBuckets[i] = new WheelBucket();
        }
        this.worker = new Worker(this.tickDuration,this.bucketSize,this.timeUnit,this.wheelBuckets);

        this.workerThread = new Thread(this.worker);

    }

    public WheelTimer() {
        this.wheelBuckets = new WheelBucket[bucketSize];
        for (int i = 0; i < wheelBuckets.length; i ++) {
            wheelBuckets[i] = new WheelBucket();
        }
        this.worker = new Worker(this.tickDuration,this.bucketSize,this.timeUnit,this.wheelBuckets);

        this.workerThread = new Thread(this.worker);
    }

    public void start(){
        if(State.NEW == this.workerThread.getState()){
            this.workerThread.start();
        }
    }

    public void stop(){
        this.worker.stop();
    }

    public void newTimerTask(long delay,TimeUnit delayTimeUnit,TimerTask timerTask){

        this.worker.addTask(delay,delayTimeUnit,timerTask);

        if(State.NEW == workerThread.getState()){
            this.workerThread.start();
        }
    }


}
