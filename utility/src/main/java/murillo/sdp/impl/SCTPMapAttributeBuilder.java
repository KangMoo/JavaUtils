/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package murillo.sdp.impl;

import murillo.abnf.Rule$app;
import murillo.abnf.Rule$sctpmap_attr;
import murillo.abnf.Rule$sctpmap_number;
import murillo.abnf.Rule$streams;
import murillo.sdp.SCTPMapAttribute;

/**
 *
 * @author Sergio
 */
class SCTPMapAttributeBuilder extends Builder {

	private SCTPMapAttribute sctpmap;

	@Override
	public Object visit(Rule$sctpmap_attr rule) {
		 //New attr
		sctpmap = new SCTPMapAttribute();
		//Generate it
		super.visit(rule);
		//Return it
		return sctpmap;
	}

	@Override
	public Object visit(Rule$sctpmap_number rule) {
		 //Get number
		Integer value = Integer.parseInt(rule.toString());
		//Set type
		sctpmap.setNumber(value);
		//Return it
		return value;
	}

	@Override
	public Object visit(Rule$app rule) {
		 //Get app
		String value = rule.toString();
		//Set type
		sctpmap.setApp(value);
		//Return it
		return value;
	}


	@Override
	public Object visit(Rule$streams rule) {
		 //Get number
		Integer value = Integer.parseInt(rule.toString());
		//Set type
		sctpmap.setStreams(value);
		//Return it
		return value;
	}
    
}
