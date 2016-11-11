package cat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by piek on 06/11/2016.
 */
public class CatFrameRelation {

    /*
      HAS_PARTICIPANT r_id="36"
      sem_role="ArgM-MNR"
      frame="Becoming"
      frame_element="Final_state"
      confidence_frame="2"
      confidence_role="3">
      <source m_id="1"/>
      <target m_id="2"/>
    </HAS_PARTICIPANT>
     */

    private String name;
    private String id;
    private String semRole;
    private String frame;
    private String frameElement;
    private String confidenceFrame;
    private String confidenceRole;
    private String source;
    private String target;

    public CatFrameRelation() {
        this.name = "";
        this.id = "";
        this.semRole = "";
        this.frame = "";
        this.frameElement = "";
        this.confidenceFrame = "";
        this.confidenceRole = "";
        this.source = "";
        this.target = "";
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

    public String getSemRole() {
        return semRole;
    }

    public void setSemRole(String semRole) {
        this.semRole = semRole;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public String getFrameElement() {
        return frameElement;
    }

    public void setFrameElement(String frameElement) {
        this.frameElement = frameElement;
    }

    public String getConfidenceFrame() {
        return confidenceFrame;
    }

    public void setConfidenceFrame(String confidenceFrame) {
        this.confidenceFrame = confidenceFrame;
    }

    public String getConfidenceRole() {
        return confidenceRole;
    }

    public void setConfidenceRole(String confidenceRole) {
        this.confidenceRole = confidenceRole;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }


    public Element toCafXML(Document xmldoc)
    {
        Element root = xmldoc.createElement("HAS_PARTICIPANT");
        if (!id.isEmpty()) root.setAttribute("r_id", id);
        if (!semRole.isEmpty()) root.setAttribute("sem_role", semRole);
        if (!frame.isEmpty()) root.setAttribute("frame", frame);
        if (!frameElement.isEmpty()) root.setAttribute("frame_element", frameElement);
        if (!confidenceFrame.isEmpty()) root.setAttribute("confidence_frame", confidenceFrame);
        if (!confidenceRole.isEmpty()) root.setAttribute("confidence_role", confidenceRole);

        if (!source.isEmpty()) {
            Element sourceEl = xmldoc.createElement("source");
            sourceEl.setAttribute("m_id", source);
            root.appendChild(sourceEl);
        }
        if (!target.isEmpty()) {
            Element targetEl = xmldoc.createElement("target");
            targetEl.setAttribute("m_id", target);
            root.appendChild(targetEl);
        }
        return root;
    }


}
