/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package murillo.sdp.impl;
import  murillo.sdp.Bandwidth;
import  murillo.abnf.Rule$bandwidth;
import  murillo.abnf.Rule$bandwidth_field;
import  murillo.abnf.Rule$bwtype;

/**
 *
 * @author Sergio
 */
class BandwitdhBuilder extends Builder {

    private Bandwidth bandwidth;

    @Override
    public Object visit(Rule$bandwidth_field rule) {
        //Create object
        bandwidth = new Bandwidth();
        //Generate
        visitRules(rule.rules);
        //Return it
        return bandwidth;
    }


    @Override
    public Object visit(Rule$bandwidth rule) {
        //Generate
        String b = rule.toString();
        //Set it
        bandwidth.setBandwidth(b);
        //Return it
        return b;
    }

    @Override
    public Object visit(Rule$bwtype rule) {
        //Generate
        String type = rule.toString();
        //Set
        bandwidth.setType(type);;
        //Return it
        return type;
    }
}
