/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package murillo.sdp.impl;

import murillo.abnf.Rule$rtpmap_attr;
import murillo.abnf.Rule$fmt;
import murillo.abnf.Rule$name;
import murillo.abnf.Rule$rate;
import murillo.abnf.Rule$parameters;
import murillo.sdp.RTPMapAttribute;

/**
 *
 * @author Sergio
 */
public class RTPMapAttributeBuilder extends Builder {
    private RTPMapAttribute attr;

    @Override
    public Object visit(Rule$rtpmap_attr rule) {
        //New attr
        attr = new RTPMapAttribute();
        //Generate it
        super.visit(rule);
        //Return it
        return attr;
    }

    @Override
    public Object visit(Rule$fmt rule) {
        //Get type
        Integer fmt = Integer.parseInt(rule.toString());
        //Set type
        attr.setFormat(fmt);
        //Return it
        return fmt;
    }

    @Override
    public Object visit(Rule$name rule) {
        //Get type
        String name = rule.toString();
        //Set type
        attr.setName(name);
        //Return it
        return name;
    }

    @Override
    public Object visit(Rule$rate rule) {
        //Get type
        Integer rate = Integer.parseInt(rule.toString());
        //Set type
        attr.setRate(rate);
        //Return it
        return rate;
    }

    @Override
    public Object visit(Rule$parameters rule) {
        //Get type
        String parameters = rule.toString();
        //Set type
        attr.setParameters(parameters);
        //Return it
        return parameters;
    }
}
