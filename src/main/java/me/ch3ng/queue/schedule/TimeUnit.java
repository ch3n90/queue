package me.ch3ng.queue.schedule;

public enum TimeUnit {

    MILLISECONDS(1),
    SECONDS(1000),
    MINUTES(60 * 1000),
    HOURS(60 * 60 * 1000),
    DAY(24 * 60 * 60 * 1000);

    private int unit;

    TimeUnit(int unit) {
        this.unit = unit;
    }

    public int val(){
       return this.unit;
    }

}
