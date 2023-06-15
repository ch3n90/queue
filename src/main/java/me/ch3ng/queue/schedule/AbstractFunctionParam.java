package me.ch3ng.queue.schedule;

public abstract class AbstractFunctionParam{
    /**
     * current run times
     * default 0
     */
    protected int index = 0;

    /**
     * total run count
     * default 3
     */
    protected int count = 3;

    protected long timestamp = System.currentTimeMillis();


}