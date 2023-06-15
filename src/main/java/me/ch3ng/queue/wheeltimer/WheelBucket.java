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


    private List<Timeout> timeouts = new LinkedList<>();


    public void push(Timeout timeout){
        this.timeouts.add(timeout);
    }


    public List<Timeout> poll(){
        List<Timeout> collect = timeouts.stream().filter(timeout -> timeout.round == 0).collect(Collectors.toList());
        timeouts.removeAll(collect);
        timeouts.forEach(timeout -> timeout.round--);
        return collect;
    }


}
