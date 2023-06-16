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

        WheelTimer wheelTimer = new WheelTimer(1,12, TimeUnit.SECONDS);


        wheelTimer.newTimerTask(6, TimeUnit.SECONDS,new TimerTask() {
            @Override
            public void d0() {
                System.out.println("6秒时间到了，开始执行");
            }
        });
        wheelTimer.newTimerTask(8, TimeUnit.SECONDS, new TimerTask() {
            @Override
            public void d0() {
                System.out.println("8秒时间到了，开始执行");
            }
        });

        wheelTimer.newTimerTask(10, TimeUnit.SECONDS, new TimerTask() {
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
