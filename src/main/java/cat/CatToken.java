package cat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Created by piek on 06/11/2016.
 */
public class CatToken {

    private String number;
    private String sentence;
    private String tokenId;
    private String value;
    /*
      <token number="0" sentence="1" t_id="1">Dirk</token>
     */
    public CatToken() {
        this.number = "";
        this.sentence = "";
        this.tokenId = "";
        this.value = "";
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Element toCafXML(Document xmldoc)
    {
        Element root = xmldoc.createElement("token");
        if (!tokenId.isEmpty()) root.setAttribute("t_id", tokenId);
        if (!number.isEmpty()) root.setAttribute("number", number);
        if (!sentence.isEmpty()) root.setAttribute("sentence", sentence);
        Node text = xmldoc.createTextNode(value);
        root.appendChild(text);
        return root;
    }

}
