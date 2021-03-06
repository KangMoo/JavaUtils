/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package murillo.sdp.impl;

import murillo.abnf.Rule$origin_field;
import murillo.abnf.Rule$addrtype;
import murillo.abnf.Rule$nettype;
import murillo.abnf.Rule$sess_id;
import murillo.abnf.Rule$sess_version;
import murillo.abnf.Rule$userinfo;
import murillo.abnf.Rule$username;
import murillo.abnf.Rule$unicast_address;
import murillo.sdp.Origin;

/**
 *
 * @author Sergio
 */
class OriginBuilder extends Builder {

    private Origin origin;

    @Override
    public Object visit(Rule$origin_field rule) {
        //Create object
        origin = new Origin();
        //Generate
        visitRules(rule.rules);
        //Return it
        return origin;
    }

    @Override
    public Object visit(Rule$username rule) {
        //Generate
        String username = rule.toString();
        //Set it
        origin.setUsername(username);
        //Return it
        return username;
    }

    @Override
    public Object visit(Rule$sess_id rule) {
        //Generate
        Long sessId = Long.parseLong(rule.toString());
        //Set it
        origin.setSessId(sessId);
        //Return it
        return sessId;
    }

    @Override
    public Object visit(Rule$sess_version rule) {
        //Generate
        Long sessVersion = Long.parseLong(rule.toString());
        //Sset it
        origin.setSessVersion(sessVersion);
        //Return it
        return sessVersion;
    }

    @Override
    public Object visit(Rule$nettype rule) {
        //Generate
        String nettype = rule.toString();
        //Set it
        origin.setNettype(nettype);
        //Return it
        return nettype;
    }

    @Override
    public Object visit(Rule$addrtype rule) {
        //Generate
        String addrtype = rule.toString();
        //Set
        origin.setAddrtype(addrtype);
        //Return it
        return addrtype;
    }

    @Override
    public Object visit(Rule$unicast_address rule) {
        //Generate
        String address = rule.toString();
        //Set it
        origin.setAddress(address);
        //Return it
        return address;
    }
}
