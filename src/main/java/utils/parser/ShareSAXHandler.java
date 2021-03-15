package utils.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.logging.Level;

import utils.entities.OCShare;
import utils.log.Log;

public class ShareSAXHandler extends DefaultHandler {

    private OCShare share;
    private static String text = null;


    @Override
    public void startElement(String uri, String localName, String node, Attributes attributes)
            throws SAXException {
        if (node.equals("element")){
            share = new OCShare();
        }
    }

    @Override
    public void endElement(String uri, String localName, String node)
            throws SAXException {
        switch (node) {
            case ("id"):{
                Log.log(Level.FINE, "Id: " + text);
                share.setId(text);
                break;
            }
            case ("uid_owner"):{
                Log.log(Level.FINE, "uid: " + text);
                share.setOwner(text);
                break;
            }
            case ("share_type"):{
                Log.log(Level.FINE, "type: " + text);
                share.setType(text);
                break;
            }
            case ("share_with"):{
                Log.log(Level.FINE, "with: " + text);
                share.setShareeName(text);
                break;
            }
            case ("name"):{
                Log.log(Level.FINE, "name: " + text);
                share.setLinkName(text);
                break;
            }
            case ("itemName"):{
                Log.log(Level.FINE, "name: " + text);
                share.setItemName(text.substring(1, text.length()));
                break;
            }
            case ("permissions"):{
                Log.log(Level.FINE, "permission: " + text);
                share.setPermissions(text);
                break;
            }
            case ("expiration"):{
                Log.log(Level.FINE, "expiration: " + text);
                share.setExpiration(text);
                break;
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        text = String.copyValueOf(ch, start, length).trim();
    }

    public OCShare getShare(){
        return share;
    }

}
