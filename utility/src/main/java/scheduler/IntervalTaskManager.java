package scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Session Manager
 * 주기적으로 세션을 필터링하고 처리 (Default 1초)
 * @author Kangmoo Heo
 */
public class IntervalTaskManager {
    private static final Logger log = LoggerFactory.getLogger(IntervalTaskManager.class);
    private static IntervalTaskManager instance = new IntervalTaskManager();
    private final Map<String, IntervalTaskUnit> jobs = new HashMap<>();
    private final List<Thread> runners = new ArrayList<>();

    private IntervalTaskManager() {}

    public static IntervalTaskManager getInstance() {
        if (instance == null) {
            instance = new IntervalTaskManager();
        }
        return instance;
    }

    public void start() {
        for(IntervalTaskUnit runner : jobs.values()){
            runners.add(new Thread(()->{
                while(true){
                    try{
                        runner.run();
                        Thread.sleep(runner.getInterval());
                    } catch(Exception e){
                        log.warn("Scheduler Err", e);
                    }
                }
            }));
        }
        runners.forEach(Thread::start);
        log.debug("() () () Timeout Msg Handler Start");
    }

    public void stop() {
        for(Thread thread : runners){
            thread.interrupt();
        }
        runners.clear();
        log.debug("() () () Session Manager Stop");
    }

    public void addJob(String name, IntervalTaskUnit runner){
        if(jobs.get(name) != null){
            log.warn("() () () Hashmap Key duplication");
            return;
        }
        log.debug("() () () Add Runner [{}]",name);
        jobs.put(name, runner);
    }
}
