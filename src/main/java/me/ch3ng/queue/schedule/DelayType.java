package me.ch3ng.queue.schedule;

public class DelayType extends AbstractTimeType implements ITimeType<Integer>{

    private long lastTime = System.currentTimeMillis();

    private int delay;

    private TimeUnit timeUnit;

    public DelayType(int delay, TimeUnit timeUnit,int times) {
        if(times == 0){
            throw new IllegalArgumentException("times can not be zero");
        }
        this.delay = delay;
        this.timeUnit = timeUnit;
        this.times = times;
    }


    @Override
    public Result j() {
        long now =  System.currentTimeMillis();
        if(delay * timeUnit.val() + lastTime <= now && (this.index < this.times || this.times < 0)){
            lastTime = now;
            this.index++;
            return new Result(true, !(this.index < this.times || this.times < 0));
        }
        return new Result(false);
    }
}
