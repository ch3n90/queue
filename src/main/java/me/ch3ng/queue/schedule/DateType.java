package me.ch3ng.queue.schedule;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

public class DateType extends AbstractTimeType implements ITimeType<Integer>{

    private int day;
    private int hour;
    private int minute;

    public DateType(int day, int hour, int minute, int times){
        if(times == 0){
            throw new IllegalArgumentException("times can not be zero");
        }
        this.day = ChronoField.DAY_OF_MONTH.checkValidIntValue(day);
        this.hour = ChronoField.HOUR_OF_DAY.checkValidIntValue(hour);
        this.minute = ChronoField.MINUTE_OF_HOUR.checkValidIntValue(minute);
        this.times = times;
    }

    @Override
    public Result j() {
        LocalDateTime now = LocalDateTime.now();
        int toDay = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();
        boolean result = toDay == this.day && hour == this.hour && minute == this.minute;
        boolean result2 = lastTime.getYear() == now.getYear()
                && lastTime.getMonthValue() == now.getMonthValue()
                && lastTime.getDayOfMonth() == now.getDayOfMonth();
        if(result && (this.index < this.times || this.times < 0) && !result2){
            return buildRunResult(now);
        }
        return new Result(false);
    }
}
