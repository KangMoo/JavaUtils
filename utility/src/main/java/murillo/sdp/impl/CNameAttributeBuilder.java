/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package murillo.sdp.impl;

import murillo.sdp.CNameAttribute;
import murillo.abnf.Rule$cname;
import murillo.abnf.Rule$cname_attr;

/**
 *
 * @author Sergio
 */
class CNameAttributeBuilder extends Builder {

    private CNameAttribute attr;

    @Override
    public Object visit(Rule$cname_attr rule) {
        //New attr
        attr = new CNameAttribute();
        //Generate it
        super.visit(rule);
        //Return it
        return attr;
    }

    @Override
    public Object visit(Rule$cname rule) {
        //Get value
        String cname = rule.toString();
        //Set value
        attr.setCName(cname);
        //Return it
        return cname;
    }
    
}
