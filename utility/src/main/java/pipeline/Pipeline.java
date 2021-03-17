package pipeline;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.UnaryOperator;

/**
 *
 * @author kangmoo Heo
 */
public class Pipeline<T> {
    private final Map<Integer, UnaryOperator<T>> pipelineSteps = new TreeMap<>();

    public void addStep(int priority, UnaryOperator<T>step){
        pipelineSteps.put(priority, step);
    }

    public T execute(T o){
        for(UnaryOperator<T> step : pipelineSteps.values()){
            o = step.apply(o);
        }
        return o;
    }
}
