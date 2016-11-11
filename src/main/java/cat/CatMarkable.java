package cat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;

/**
 * Created by piek on 06/11/2016.
 */
public class CatMarkable {
    /*
    <ENTITY_MENTION m_id="35">
      <token_anchor t_id="478"/>
    </ENTITY_MENTION>
     */
    private String name;
    private String id;
    private ArrayList<String> tokenAnchors;

    public CatMarkable() {
        this.name = "";
        this.id = "";
        this.tokenAnchors = new ArrayList<String>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getTokenAnchors() {
        return tokenAnchors;
    }

    public void setTokenAnchors(ArrayList<String> tokenAnchors) {
        this.tokenAnchors = tokenAnchors;
    }

    public Element toCafXML(Document xmldoc)
    {
        Element root = xmldoc.createElement(name);
        if (!id.isEmpty()) root.setAttribute("m_id", id);
        for (int i = 0; i < tokenAnchors.size(); i++) {
            String a = tokenAnchors.get(i);
            Element anchor = xmldoc.createElement("token_anchor");
            anchor.setAttribute("t_id", a);
            root.appendChild(anchor);
        }
        return root;
    }

}
