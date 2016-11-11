package cat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;

/**
 * Created by piek on 06/11/2016.
 */
public class CatFrameMismatch {
    private String name;
    private String id;
    private String anno1Value;
    private String anno2Value;
    private ArrayList<String> tokenAnchors;

    public CatFrameMismatch() {
        this.name = "";
        this.id = "";
        this.anno1Value = "";
        this.anno2Value = "";
        this.tokenAnchors = new ArrayList<String>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getTokenAnchors() {
        return tokenAnchors;
    }

    public void setTokenAnchors(ArrayList<String> tokenAnchors) {
        this.tokenAnchors = tokenAnchors;
    }

    public String getAnno1Value() {
        return anno1Value;
    }

    public void setAnno1Value(String anno1Value) {
        this.anno1Value = anno1Value;
    }

    public String getAnno2Value() {
        return anno2Value;
    }

    public void setAnno2Value(String anno2Value) {
        this.anno2Value = anno2Value;
    }

    public Element toCafXML(Document xmldoc)
    {
        Element root = xmldoc.createElement(name);
        if (!id.isEmpty()) root.setAttribute("m_id", id);
        if (!anno1Value.isEmpty()) root.setAttribute("anno1-value", anno1Value);
        if (!anno2Value.isEmpty()) root.setAttribute("anno2-value", anno2Value);
        for (int i = 0; i < tokenAnchors.size(); i++) {
            String a = tokenAnchors.get(i);
            Element anchor = xmldoc.createElement("token_anchor");
            anchor.setAttribute("t_id", a);
            root.appendChild(anchor);
        }
        return root;
    }
}
