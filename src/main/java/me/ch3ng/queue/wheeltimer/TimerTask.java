package me.ch3ng.queue.wheeltimer;

/**
 * @author ch3ng
 * @date 2023/6/14 18:03
 * @description:
 */
public abstract class TimerTask implements Runnable{

    protected long round;
    protected int bucketIdx;
    protected TimerTaskStatus status;

    public void cancel(){
        this.status = TimerTaskStatus.CANCELED;
    }

    @Override
    public void run() {
        this.status = TimerTaskStatus.RUNNING;
        d0();
        this.status = TimerTaskStatus.TERMINATION;
    }

    public abstract void d0();

}
