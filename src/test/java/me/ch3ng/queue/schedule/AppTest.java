package me.ch3ng.queue.schedule;

import org.junit.Test;

import java.io.IOException;
import java.time.DayOfWeek;

public class AppTest 
{

    @Test
    public void testWeekType() throws IOException {
        TaskContainer.INSTANCE().put(new WeekType(DayOfWeek.SUNDAY,15,24,2), new AbstractTask() {
            @Override
            public void job() {
                System.out.println("week type, start running");
            }
        });
        System.in.read();
    }

    @Test
    public void testTimeType() throws IOException {
        TaskContainer.INSTANCE().put(new TimeType(48,2), new AbstractTask() {
            @Override
            public void job() {
                System.out.println("time type, start running");
            }
        });
        System.in.read();
    }

    @Test
    public void testDelayType() throws IOException {
        TaskContainer.INSTANCE().put(new DelayType(20, TimeUnit.SECONDS,-1), new AbstractTask() {
            @Override
            public void job() {
                System.out.println("delay type, start running");
            }
        });
        System.in.read();
    }

    @Test
    public void testDateType() throws IOException {
        TaskContainer.INSTANCE().put(new DateType(7, 15,55,2), new AbstractTask() {
            @Override
            public void job() {
                System.out.println("date type, start running");
            }
        });
        System.in.read();
    }

    @Test
    public void testFunc() throws IOException {
        TaskContainer.INSTANCE().put(new FunctionType<FunctionParam>(new FunctionParam(5),
                rt -> {

                    if(rt.timestamp < System.currentTimeMillis()){
                        System.out.println("current index:" + rt.index);
                        return new FunctionResult(true,System.currentTimeMillis() + 1000 * 15);
                    }else {
                        return new FunctionResult(false);
                    }

                }),new AbstractTask() {
            @Override
            public void job() {
                System.out.println("func type, start running");
            }
        });
        System.in.read();
    }


}
