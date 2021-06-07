/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package murillo.sdp.impl;

import murillo.sdp.FormatAttribute;
import murillo.abnf.Rule$fmtp_attr;
import murillo.abnf.Rule$fmt;
import murillo.abnf.Rule$param_list;

/**
 *
 * @author Sergio
 */
public class FormatAttributeBuilder extends Builder {
    private FormatAttribute attr;

    @Override
    public Object visit(Rule$fmtp_attr rule) {
        //New attr
        attr = new FormatAttribute();
        //Generate it
        super.visit(rule);
        //Return it
        return attr;
    }

    @Override
    public Object visit(Rule$fmt rule) {
        //Get value
        Integer fmt = Integer.parseInt(rule.toString());
        //Set it
        attr.setFmt(fmt);
        //Return it
        return fmt;
    }

    @Override
    public Object visit(Rule$param_list rule) {
        //Get value
        String parameters = rule.toString();
        //Set it
        attr.setParameters(parameters);
        //Return it
        return parameters;
    }
}
