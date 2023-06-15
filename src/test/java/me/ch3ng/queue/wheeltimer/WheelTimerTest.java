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

        wheelTimer.newTimeout(26, TimeUnit.SECONDS, new Timeout() {
            @Override
            public void run() {
                System.out.println("延迟26s后执行了");
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
