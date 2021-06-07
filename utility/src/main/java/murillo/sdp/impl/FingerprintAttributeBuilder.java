/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package murillo.sdp.impl;

import murillo.abnf.Rule$fingerprint_attribute;
import murillo.abnf.Rule$hash_func;
import murillo.abnf.Rule$fingerprint;
import murillo.sdp.FingerprintAttribute;

/**
 *
 * @author Sergio
 */
class FingerprintAttributeBuilder extends Builder {

    private FingerprintAttribute fingerprint;

    @Override
    public Object visit(Rule$fingerprint_attribute rule) {
        //New attr
        fingerprint = new FingerprintAttribute();
        //Generate it
        super.visit(rule);
        //Return it
        return fingerprint;
    }

    @Override
    public Object visit(Rule$hash_func rule) {
        //Get hash func
        String value = rule.toString();
        //Set type
        fingerprint.setHashFunc(value);
        //Return it
        return value;
    }
    
    @Override
    public Object visit(Rule$fingerprint rule) {
        //Get fingerprint
        String value = rule.toString();
        //Set type
        fingerprint.setFingerprint(value);
        //Return it
        return value;
    }

    
}
