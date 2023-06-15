package me.ch3ng.queue.wheeltimer;

/**
 * @author ch3ng
 * @date 2023/6/14 18:03
 * @description:
 */
public abstract class Timeout implements Runnable{

    protected long round;
    protected int bucketIdx;

}
