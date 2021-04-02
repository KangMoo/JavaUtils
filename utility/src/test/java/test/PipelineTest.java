package test;

import org.junit.Test;
import pipeline.Pipeline;

import java.util.function.UnaryOperator;

/**
 *
 * @author kangmoo Heo
 */
public class PipelineTest {
    @Test
    public void pipelineTest() {
        Pipeline<Integer> pipeline = new Pipeline<>();
        UnaryOperator<Integer> add100 = input -> input += 100;
        UnaryOperator<Integer> add10 = input -> input += 10;
        UnaryOperator<Integer> mul = input -> input *= 2;
        pipeline.addStep(1, add100);    // 2nd step
        pipeline.addStep(2, add10);     // 3rd step
        pipeline.addStep(3, mul);       // 4th step
        pipeline.addStep(4, add10);     // 5th step
        pipeline.addStep(0, add100);    // 1st step
        // result = (((((0 + 100) + 100) + 10) * 2) + 10) = 430

        System.out.println("pipeline.execute(10) = " + pipeline.execute(0));

    }
}
