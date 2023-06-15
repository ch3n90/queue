package me.ch3ng.queue.schedule;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskContainer<T extends AbstractTask>{

    private final Map<ITimeType,AbstractTask> container = new ConcurrentHashMap();

    private static final Lock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();

    private ThreadPoolExecutor workExecutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
            Short.MAX_VALUE,60,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>());

    private ExecutorService bossExecutor = Executors.newSingleThreadExecutor();

    private static final int LENGTH = 10;

    private static final int RANGE = 1000;

    public void put(ITimeType key,T task){
        boolean is0 = container.size() == 0 ? true : false;
        container.put(key,task);
        if(is0){
            lock.lock();
            condition.signalAll();
            lock.unlock();
        }
    }

    protected AbstractTask get(ITimeType timeType){
        return container.get(timeType);
    }

    private TaskContainer(){
        bossExecutor.execute(new Runnable() {
            @Override
            public void run() {
                l00p();
            }
        });
    }

    private static class Instance{
        private static  TaskContainer instance = new TaskContainer();
    }

    public static TaskContainer INSTANCE(){
        return Instance.instance;
    }

    private void l00p()  {
        try {
            lock.lock();
            while (true) {
                if (container.size() == 0) {
                    condition.await();
                }else {
                    long mark1 = System.currentTimeMillis();
                    int times = 1;
                    do {
                        container.entrySet().stream().forEach(entry -> {
                            ITimeType key = entry.getKey();
                            Result result = key.j();
                            if (result.isRun) {
                                workExecutor.execute(entry.getValue());
                                if(result.isRemove){
                                    container.remove(key);
                                }
                            }
                        });
                        times++;
                    } while (times <= LENGTH);
                    long val = System.currentTimeMillis() - mark1;
                    if (val <= RANGE) {
                        TimeUnit.MILLISECONDS.sleep(RANGE - val);
                    }
                }
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }
}
