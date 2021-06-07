/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package murillo.sdp.impl;
import murillo.abnf.Rule$ssrc_group_attr;
import murillo.abnf.Rule$ssrc_id;
import murillo.abnf.Rule$ssrc_group_semantics;
import murillo.sdp.SSRCGroupAttribute;


/**
 *
 * @author Sergio
 */
class SSRCGroupAttributeBuilder extends Builder {

    private SSRCGroupAttribute group;

     @Override
    public Object visit(Rule$ssrc_group_attr rule) {
        //New attr
        group = new SSRCGroupAttribute();
        //Generate it
        super.visit(rule);
        //Return it
        return group;
    }

    @Override
    public Object visit(Rule$ssrc_id rule) {
         //Get type
        Long ssrcId = Long.parseLong(rule.toString());
        //Set type
        group.addSSRCId(ssrcId);
        //Return it
        return ssrcId;
    }
    @Override
    public Object visit(Rule$ssrc_group_semantics rule) {
        //Get type
        String semantics = rule.toString();
        //Set type
        group.setSemantics(semantics);
        //Return it
        return semantics;
    }
}
