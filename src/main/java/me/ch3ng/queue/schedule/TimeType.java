package me.ch3ng.queue.schedule;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

public class TimeType extends AbstractTimeType implements ITimeType<Long>{

    private int minute;

    public TimeType(int minute,int times) {
        if(times == 0){
            throw new IllegalArgumentException("times can not be zero");
        }
        this.minute = ChronoField.MINUTE_OF_HOUR.checkValidIntValue(minute);
        this.times = times;
    }

    @Override
    public Result j() {
        LocalDateTime now = LocalDateTime.now();
        int minute = now.getMinute();
        boolean result = minute == this.minute;
        boolean result2 = lastTime.getYear() == now.getYear()
                && lastTime.getMonthValue() == now.getMonthValue()
                && lastTime.getDayOfMonth() == now.getDayOfMonth()
                && lastTime.getHour() == now.getHour();
        if(result && (this.index < this.times || this.times < 0) && !result2){
           return buildRunResult(now);
        }
        return new Result(false);
    }
}
