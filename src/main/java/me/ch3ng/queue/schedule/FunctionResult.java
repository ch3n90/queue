package me.ch3ng.queue.schedule;

public class FunctionResult extends Result{

    protected long timestamp;

    public FunctionResult(boolean isRun) {
        super(isRun);
    }

    public FunctionResult(boolean isRun, long timestamp) {
        super(isRun);
        this.timestamp = timestamp;
    }
}
