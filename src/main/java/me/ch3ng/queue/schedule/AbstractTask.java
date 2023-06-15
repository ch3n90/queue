package me.ch3ng.queue.schedule;

public abstract class AbstractTask<T> implements Runnable{

    protected T data;

    @Override
    public void run() {
        job();
    }

    public abstract void job();

    public void setData(T data) {
        this.data = data;
    }
}
