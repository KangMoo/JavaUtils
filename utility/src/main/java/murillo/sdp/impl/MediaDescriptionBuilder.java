/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package murillo.sdp.impl;


import murillo.abnf.Rule$bandwidth_field;
import murillo.abnf.Rule$connection_field;
import murillo.abnf.Rule$information_field;
import murillo.abnf.Rule$key_field;
import murillo.abnf.Rule$media_description;
import murillo.abnf.Rule$attribute_field;
import murillo.abnf.Rule$proto;
import murillo.abnf.Rule$port;
import murillo.abnf.Rule$number_of_ports;
import murillo.abnf.Rule$fmt;
import murillo.abnf.Rule$media;
import murillo.sdp.Attribute;
import murillo.sdp.Bandwidth;
import murillo.sdp.Connection;
import murillo.sdp.Information;
import murillo.sdp.Key;
import murillo.sdp.MediaDescription;

/**
 *
 * @author Sergio
 */
class MediaDescriptionBuilder extends  Builder {

    public MediaDescription media;

    @Override
    public Object visit(Rule$media_description rule) {
        //New object
        media = new MediaDescription();
        //Parse it
        super.visit(rule);
        //return media
        return media;
    }

    @Override
    public Object visit(Rule$port rule) {
        //Get port
        Integer port = Integer.parseInt(rule.toString());
        //Set it
        media.setPort(port);
        //Return it
        return port;
    }

    @Override
    public Object visit(Rule$number_of_ports rule) {
        //Get port
        Integer number = Integer.parseInt(rule.toString());
        //Set it
        media.setNumberOfPorts(number);
        //Return it
        return number;
    }

    @Override
    public Object visit(Rule$proto rule) {
        //Get protocol
        String proto = rule.toString();
        //Set it
        media.setProtoString(proto);
        //Return it
        return proto;
    }

    @Override
    public Object visit(Rule$media rule) {
        //Get media
        String name = rule.toString();
        //Set it
        media.setMedia(name);
        //Return it
        return name;
    }

    @Override
    public Object visit(Rule$fmt rule) {
        //Get protocol
        String fmt = rule.toString();
        //Set it
        media.addFormat(fmt);
        //Return it
        return fmt;
    }

    @Override
    public Object visit(Rule$key_field rule) {
        //Create builder
        KeyBuilder builder = new KeyBuilder();
        //Generate it
        Key key = (Key)builder.visit(rule);
        //Add it
        media.setKey(key);
        //Return it
        return key;
    }

    @Override
    public Object visit(Rule$connection_field rule) {
        //Create builder
        ConnectionBuilder builder = new ConnectionBuilder();
        //Parse it
        Connection connection = (Connection)builder.visit(rule);
        //Set it
        media.addConnection(connection);
        //Return connection
        return connection;
    }

    @Override
    public Object visit(Rule$bandwidth_field rule) {
        //Create builder
        BandwitdhBuilder builder = new BandwitdhBuilder();
        //Generate it
        Bandwidth bandwidth = (Bandwidth)builder.visit(rule);
        //Add it
        media.addBandwidth(bandwidth);
        //Return it
        return bandwidth;
    }

    @Override
    public Object visit(Rule$information_field rule) {
        //Create new session name
        InformationBuilder builder = new InformationBuilder();
        //Generate
        Information info = (Information) builder.visit(rule);
        //Set it
        media.setInformation(info);
        //Return it
        return info;
    }

    @Override
    public Object visit(Rule$attribute_field rule) {
        //Create new session name
        AttributeBuilder builder = new AttributeBuilder();
        //Generate
        Attribute attr = (Attribute) builder.visit(rule);
        //Set it
        media.addAttribute(attr);
        //Return it
        return attr;
    }
}
