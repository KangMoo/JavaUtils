/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package murillo.sdp.impl;

import murillo.abnf.Rule$crypto_attribute;
import murillo.abnf.Rule$tag;
import murillo.abnf.Rule$crypto_suite;
import murillo.abnf.Rule$session_param;
import murillo.abnf.Rule$key_param;
import murillo.abnf.Rule$key_method;
import murillo.abnf.Rule$key_info;
import murillo.sdp.CryptoAttribute;

/**
 *
 * @author Sergio
 */
class CryptoAttributeBuilder extends Builder {

    private CryptoAttribute crypto;
    private CryptoAttribute.KeyParam param;

    @Override
    public Object visit(Rule$crypto_attribute rule) {
        //New attr
        crypto = new CryptoAttribute();
        //Generate it
        super.visit(rule);
        //Return it
        return crypto;
    }

    @Override
    public Object visit(Rule$tag rule) {
        //Get type
        Integer tag = Integer.parseInt(rule.toString());
        //Set type
        crypto.setTag(tag);
        //Return it
        return tag;
    }
    
    @Override
    public Object visit(Rule$crypto_suite rule) {
        //Get suite
        String suite = rule.toString();
        //Set type
        crypto.setSuite(suite);
        //Return it
        return suite;
    }

    @Override
    public Object visit(Rule$session_param rule) {
        //Get type
        String sessionParam = rule.toString();
        //Set type
        crypto.setSessionParams(sessionParam);
        //Return it
        return sessionParam;
    }

    @Override
    public Object visit(Rule$key_param rule) {
        //New attr
        param = new CryptoAttribute.KeyParam();
        //Generate it
        super.visit(rule);
        //Add it
        crypto.addKeyParam(param);
        //Return it
        return param;
    }

    @Override
    public Object visit(Rule$key_method rule) {
        //Get method
        String method = rule.toString();
        //Set type
        param.setMethod(method);
        //Return it
        return method;
    }

    @Override
    public Object visit(Rule$key_info rule) {
        //Get info
        String info = rule.toString();
        //Set type
        param.setInfo(info);
        //Return it
        return info;
    }
}
