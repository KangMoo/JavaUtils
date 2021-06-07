/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package murillo.sdp.impl;

import murillo.sdp.Time;
import murillo.abnf.Rule$time_repeat_field;
import murillo.abnf.Rule$start_time;
import murillo.abnf.Rule$stop_time;

/**
 *
 * @author Sergio
 */
public class TimeBuilder extends Builder{

    private Time time;

    @Override
    public Object visit(Rule$time_repeat_field rule) {
        //Create object
        time = new Time();
        //Generate it
        super.visit(rule);
        //Return it
        return time;
    }

    @Override
    public Object visit(Rule$start_time rule) {
        //Get start
        Integer start = Integer.parseInt(rule.toString());
        //Set start
        time.setStart(start);
        //Return it
        return start;
    }

    @Override
    public Object visit(Rule$stop_time rule) {
        //Get stop
        Integer stop = Integer.parseInt(rule.toString());
        //Set start
        time.setStop(stop);
        //Return it
        return stop;
    }
}
