package me.ch3ng.queue.wheeltimer;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ch3ng
 * @date 2023/6/14 17:48
 * @description:
 */
public class WheelBucket {


    private List<TimerTask> timerTasks = new LinkedList<>();


    protected void push(TimerTask timerTask){
        this.timerTasks.add(timerTask);
    }


    protected List<TimerTask> poll(){
        List<TimerTask> collect = timerTasks.stream().filter(timeout -> timeout.round == 0).collect(Collectors.toList());
        timerTasks.removeAll(collect);
        timerTasks.forEach(timerTask -> timerTask.round--);
        return collect;
    }

    protected List<TimerTask> processCanceledTimerTask(){
        List<TimerTask> collect = timerTasks.stream().filter(timerTask -> timerTask.status == TimerTaskStatus.CANCELED).collect(Collectors.toList());
        timerTasks.removeAll(collect);
        return collect;
    }


}
