package me.ch3ng.queue.schedule;

import java.time.LocalDateTime;

public abstract class AbstractTimeType {

    protected LocalDateTime lastTime = LocalDateTime.of(1970,1,1,0,0);;
    protected int times;
    protected int index = 0;

    protected Result buildRunResult(LocalDateTime now){
        this.lastTime = now;
        this.index++;
        return new Result(true, !(this.index < this.times || this.times < 0));
    }
}
