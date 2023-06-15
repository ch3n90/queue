package me.ch3ng.queue.schedule;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

public class WeekType extends AbstractTimeType implements ITimeType<DayOfWeek>{

    private int week;
    private int hour;
    private int minute;

    public WeekType(DayOfWeek week, int hour, int minute, int times) {
        if(times == 0){
            throw new IllegalArgumentException("times can not be zero");
        }
        this.hour = ChronoField.HOUR_OF_DAY.checkValidIntValue(hour);
        this.minute = ChronoField.MINUTE_OF_HOUR.checkValidIntValue(minute);
        this.week = week.getValue();
        this.times = times;
    }

    @Override
    public Result j() {
        LocalDateTime now = LocalDateTime.now();
        int week = now.getDayOfWeek().getValue();
        int hour = now.getHour();
        int minute = now.getMinute();
        boolean result = week == this.week && hour == this.hour && minute == this.minute;
        boolean result2 = lastTime.getYear() == now.getYear() && lastTime.getMonthValue() == now.getMonthValue() && lastTime.getDayOfMonth() == now.getDayOfMonth();
        if(result && (this.index < this.times || this.times < 0) && !result2){
            return buildRunResult(now);
        }
        return new Result(false);
    }
}
