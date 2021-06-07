/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package murillo.sdp.impl;

import murillo.abnf.Rule$attribute_field;
import murillo.abnf.Rule$mid_attr;
import murillo.abnf.Rule$group_attr;
import murillo.abnf.Rule$ssrc_attr;
import murillo.abnf.Rule$ssrc_group_attr;
import murillo.abnf.Rule$cname_attr;
import murillo.abnf.Rule$previous_ssrc_attr;
import murillo.abnf.Rule$rtpmap_attr;
import murillo.abnf.Rule$attribute;
import murillo.abnf.Rule$fmtp_attr;
import murillo.abnf.Rule$crypto_attribute;
import murillo.abnf.Rule$candidate_attribute;
import murillo.abnf.Rule$extmap_attribute;
import murillo.abnf.Rule$fingerprint_attribute;
import murillo.abnf.Rule$sctpmap_attr;
import murillo.sdp.Attribute;
import murillo.sdp.BaseAttribute;
import murillo.sdp.CNameAttribute;
import murillo.sdp.CandidateAttribute;
import murillo.sdp.CryptoAttribute;
import murillo.sdp.FormatAttribute;
import murillo.sdp.GroupAttribute;
import murillo.sdp.MidAttribute;
import murillo.sdp.PreviousSSRCAttribute;
import murillo.sdp.RTPMapAttribute;
import murillo.sdp.SSRCAttribute;
import murillo.sdp.SSRCGroupAttribute;
import murillo.sdp.ExtMapAttribute;
import murillo.sdp.FingerprintAttribute;
import murillo.sdp.SCTPMapAttribute;


/**
 *
 * @author Sergio
 */
class AttributeBuilder extends Builder {

    private Attribute attr;

    @Override
    public Object visit(Rule$attribute_field rule) {
        //Reset
        attr = null;
        //Generate
        super.visit(rule);
        //Return it
        return attr;
    }

    @Override
    public Object visit(Rule$mid_attr rule) {
        //Get builder
        MidAttributeBuilder builder = new MidAttributeBuilder();
        //build it
        attr = (MidAttribute)builder.visit(rule);
        //Return it
        return attr;
    }

    @Override
    public Object visit(Rule$group_attr rule) {
        //Get builder
        GroupAttributeBuilder builder = new GroupAttributeBuilder();
        //build it
        attr = (GroupAttribute)builder.visit(rule);
        //Return it
        return attr;
    }

    @Override
    public Object visit(Rule$ssrc_attr rule) {
        //Get builder
        SSRCAttributeBuilder builder = new SSRCAttributeBuilder();
        //build it
        attr = (SSRCAttribute)builder.visit(rule);
        //Return it
        return attr;
    }

    @Override
    public Object visit(Rule$rtpmap_attr rule) {
        //Get builder
        RTPMapAttributeBuilder builder = new RTPMapAttributeBuilder();
        //build it
        attr = (RTPMapAttribute)builder.visit(rule);
        //Return it
        return attr;
    }

    @Override
    public Object visit(Rule$fmtp_attr rule) {
        //Get builder
        FormatAttributeBuilder builder = new FormatAttributeBuilder();
        //build it
        attr = (FormatAttribute)builder.visit(rule);
        //Return it
        return attr;
    }

    @Override
    public Object visit(Rule$ssrc_group_attr rule) {
        //Get builder
        SSRCGroupAttributeBuilder builder = new SSRCGroupAttributeBuilder();
        //build it
        attr = (SSRCGroupAttribute)builder.visit(rule);
        //Return it
        return attr;
    }

    @Override
    public Object visit(Rule$cname_attr rule) {
        //Get builder
        CNameAttributeBuilder builder = new CNameAttributeBuilder();
        //build it
        attr = (CNameAttribute)builder.visit(rule);
        //Return it
        return attr;
    }

    @Override
    public Object visit(Rule$previous_ssrc_attr rule) {
        //Get builder
        PreviousSSRCAttributeBuilder builder = new PreviousSSRCAttributeBuilder();
        //build it
        attr = (PreviousSSRCAttribute)builder.visit(rule);
        //Return it
        return attr;
    }

    @Override
    public Object visit(Rule$attribute rule) {
        //Get builder
        BaseAttributeBuilder builder = new BaseAttributeBuilder();
        //build it
        attr = (BaseAttribute)builder.visit(rule);
        //Return it
        return attr;
    }

    @Override
    public Object visit(Rule$crypto_attribute rule) {
        //Get builder
        CryptoAttributeBuilder builder = new CryptoAttributeBuilder();
        //build it
        attr = (CryptoAttribute)builder.visit(rule);
        //Return it
        return attr;
    }

    @Override
    public Object visit(Rule$candidate_attribute rule) {
        //Get builder
        CandidateAttributeBuilder builder = new CandidateAttributeBuilder();
        //build it
        attr = (CandidateAttribute)builder.visit(rule);
        //Return it
        return attr;
    }
    
    @Override
    public Object visit(Rule$extmap_attribute rule) {
        //Get builder
        ExtMapAttributeBuilder builder = new ExtMapAttributeBuilder();
        //build it
        attr = (ExtMapAttribute)builder.visit(rule);
        //Return it
        return attr;
    }

    @Override
    public Object visit(Rule$fingerprint_attribute rule) {
	//Get builder
        FingerprintAttributeBuilder builder = new FingerprintAttributeBuilder();
        //build it
        attr = (FingerprintAttribute)builder.visit(rule);
        //Return it
        return attr;
    }
    
    @Override
    public Object visit(Rule$sctpmap_attr rule) {
	//Get builder
        SCTPMapAttributeBuilder builder = new SCTPMapAttributeBuilder();
        //build it
        attr = (SCTPMapAttribute)builder.visit(rule);
        //Return it
        return attr;
    }
}
