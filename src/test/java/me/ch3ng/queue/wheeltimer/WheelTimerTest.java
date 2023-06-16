package me.ch3ng.queue.wheeltimer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class WheelTimerTest {

    @Test
    public void test()  {

        WheelTimer wheelTimer = new WheelTimer(2,12, TimeUnit.SECONDS);

        TimerTask timeout = new TimerTask() {
            @Override
            public void d0() {
                System.out.println("5秒时间到了，开始执行");
            }
        };


        wheelTimer.newTimerTask(5, TimeUnit.SECONDS,timeout);
        wheelTimer.newTimerTask(8, TimeUnit.SECONDS, new TimerTask() {
            @Override
            public void d0() {
                wheelTimer.stop();
            }
        });


        wheelTimer.start();
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
