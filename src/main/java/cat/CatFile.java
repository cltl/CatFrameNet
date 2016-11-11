package cat;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by piek on 06/11/2016.
 */
public class CatFile extends DefaultHandler{

        String value = "";
        public String docName = "";
        CatToken catToken;
        CatMarkable catMarkable;
        CatFrameRelation catFrameRelation;
        ArrayList<String> tokenAnchors;
        public ArrayList<CatToken> catTokenArrayList;
        public ArrayList<CatMarkable> catMarkableArrayList;
        public ArrayList<CatFrameRelation> catFrameRelationArrayList;
        public ArrayList<CatFrameMismatch> catFrameMismatchesArrayList;


        void init() {
            value = "";
            docName = "";
            catToken = new CatToken();
            catMarkable = new CatMarkable();
            tokenAnchors = new ArrayList<String>();
            catFrameRelation = new CatFrameRelation();
            catTokenArrayList = new ArrayList<CatToken>();
            catFrameRelationArrayList = new ArrayList<CatFrameRelation>();
            catMarkableArrayList = new ArrayList<CatMarkable>();
            catFrameMismatchesArrayList = new ArrayList<CatFrameMismatch>();
        }

        public void parseFile(String filePath) {
            String myerror = "";
            init();
            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                factory.setValidating(false);
                SAXParser parser = factory.newSAXParser();
                InputSource inp = new InputSource (new FileReader(filePath));
                parser.parse(inp, this);
            } catch (SAXParseException err) {
                myerror = "\n** Parsing error" + ", line " + err.getLineNumber()
                        + ", uri " + err.getSystemId();
                myerror += "\n" + err.getMessage();
                System.out.println("myerror = " + myerror);
            } catch (SAXException e) {
                Exception x = e;
                if (e.getException() != null)
                    x = e.getException();
                myerror += "\nSAXException --" + x.getMessage();
                System.out.println("myerror = " + myerror);
            } catch (Exception eee) {
                eee.printStackTrace();
                myerror += "\nException --" + eee.getMessage();
                System.out.println("myerror = " + myerror);
            }
            //System.out.println("myerror = " + myerror);
        }//--c



        public void startElement(String uri, String localName,
                                 String qName, Attributes attributes)
                throws SAXException {

            if (qName.equalsIgnoreCase("Document")) {
                docName = attributes.getValue("doc_name");
            }
            else if (qName.equalsIgnoreCase("token")) {
                catToken = new CatToken();
                catToken.setTokenId(attributes.getValue("t_id"));
                catToken.setNumber(attributes.getValue("number"));
                catToken.setSentence(attributes.getValue("sentence"));
            }
            else if (qName.equalsIgnoreCase("EVENT_MENTION")) {
                catMarkable = new CatMarkable();
                catMarkable.setName(qName);
                catMarkable.setId(attributes.getValue("m_id"));
                tokenAnchors = new ArrayList<String>();
            }
            else if (qName.equalsIgnoreCase("ENTITY_MENTION")) {
                catMarkable = new CatMarkable();
                catMarkable.setName(qName);
                catMarkable.setId(attributes.getValue("m_id"));
                tokenAnchors = new ArrayList<String>();
            }
            else if (qName.equalsIgnoreCase("token_anchor")) {
                tokenAnchors.add(attributes.getValue("t_id"));
            }
            else if (qName.equalsIgnoreCase("source")) {
                catFrameRelation.setSource(attributes.getValue("m_id"));
            }
            else if (qName.equalsIgnoreCase("target")) {
                catFrameRelation.setTarget(attributes.getValue("m_id"));
            }
            else if (qName.equalsIgnoreCase("HAS_PARTICIPANT")) {
                catFrameRelation = new CatFrameRelation();
                catFrameRelation.setId(attributes.getValue("r_id"));
                catFrameRelation.setSemRole(attributes.getValue("sem_role"));
                catFrameRelation.setFrame(attributes.getValue("frame"));
                catFrameRelation.setFrameElement(attributes.getValue("frame_element"));
                catFrameRelation.setConfidenceFrame(attributes.getValue("confidence_frame"));
                catFrameRelation.setConfidenceRole(attributes.getValue("confidence_role"));
            }

            value = "";
        }//--startElement


        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            if (qName.equalsIgnoreCase("token")) {
                catToken.setValue(value.trim());
                catTokenArrayList.add(catToken);
                catToken = new CatToken();
            }
            else if (qName.equalsIgnoreCase("EVENT_MENTION")) {
                catMarkable.setTokenAnchors(tokenAnchors);
                catMarkableArrayList.add(catMarkable);
                tokenAnchors = new ArrayList<String>();
                catMarkable = new CatMarkable ();
            }
            else if (qName.equalsIgnoreCase("HAS_PARTICIPANT")) {
                catFrameRelationArrayList.add(catFrameRelation);
                catFrameRelation = new CatFrameRelation();
            }
        }

        public void characters(char ch[], int start, int length)
                throws SAXException {
            value += new String(ch, start, length);
            // System.out.println("tagValue:"+value);
        }

        static public void main (String[] args) {
            String folder = "";
            String pathToCatFile = "";
            String fileExtension = "";
            CatFile cat = new CatFile();
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.equalsIgnoreCase("--cat-file") && args.length > (i + 1)) {
                    pathToCatFile = args[i + 1];
                } else if (arg.equalsIgnoreCase("--folder") && args.length > (i + 1)) {
                    folder = args[i + 1];
                } else if (arg.equalsIgnoreCase("--file-extension") && args.length > (i + 1)) {
                    fileExtension = args[i + 1];
                }
            }
            System.out.println("fileExtension = " + fileExtension);
            System.out.println("folder = " + folder);
            if (!pathToCatFile.isEmpty()) {
                cat.parseFile(pathToCatFile);
                OutputStream fos = null;
                try {
                    fos = new FileOutputStream(pathToCatFile + ".cat");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                String fileName = new File(pathToCatFile).getName();
                int idx = fileName.indexOf(".");
                if (idx > -1) {
                    fileName = fileName.substring(0, idx);
                }

                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    public void writeCatToStream(OutputStream stream)
    {
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation impl = builder.getDOMImplementation();

            Document xmldoc = impl.createDocument(null, "Document", null);
            xmldoc.setXmlStandalone(false);
            Element root = xmldoc.getDocumentElement();
            root.setAttribute("doc_name", docName);

            if (this.catTokenArrayList.size()>0) {
                for (int i = 0; i < catTokenArrayList.size(); i++) {
                    CatToken token = catTokenArrayList.get(i);
                    root.appendChild(token.toCafXML(xmldoc));
                }
            }
            if (this.catMarkableArrayList.size()>0) {
                Element markables = xmldoc.createElement("Markables");
                for (int i = 0; i < catMarkableArrayList.size(); i++) {
                    CatMarkable markable = catMarkableArrayList.get(i);
                    markables.appendChild(markable.toCafXML(xmldoc));
                }
                root.appendChild(markables);
            }
            if (this.catFrameRelationArrayList.size()>0) {
                Element relations = xmldoc.createElement("Relations");
                for (int i = 0; i < catFrameRelationArrayList.size(); i++) {
                    CatFrameRelation frameRelation = catFrameRelationArrayList.get(i);
                    relations.appendChild(frameRelation.toCafXML(xmldoc));
                }
                root.appendChild(relations);
            }

            // Serialisation through Tranform.
            DOMSource domSource = new DOMSource(xmldoc);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.INDENT,"yes");
            serializer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            StreamResult streamResult = null;
            streamResult = new StreamResult(new OutputStreamWriter(stream));
            serializer.transform(domSource, streamResult);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void writeCatMismatchToStream(OutputStream stream)
    {
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation impl = builder.getDOMImplementation();

            Document xmldoc = impl.createDocument(null, "Document", null);
            xmldoc.setXmlStandalone(false);
            Element root = xmldoc.getDocumentElement();
            root.setAttribute("doc_name", docName);

            if (this.catTokenArrayList.size()>0) {
                for (int i = 0; i < catTokenArrayList.size(); i++) {
                    CatToken token = catTokenArrayList.get(i);
                    root.appendChild(token.toCafXML(xmldoc));
                }
            }/*
            if (this.catMarkableArrayList.size()>0) {
                Element markables = xmldoc.createElement("Markables");
                for (int i = 0; i < catMarkableArrayList.size(); i++) {
                    CatMarkable markable = catMarkableArrayList.get(i);
                    markables.appendChild(markable.toCafXML(xmldoc));
                }
                root.appendChild(markables);
            }*/

            if (this.catFrameMismatchesArrayList.size()>0) {
                Element markables = xmldoc.createElement("Markables");
                for (int i = 0; i < catFrameMismatchesArrayList.size(); i++) {
                    CatFrameMismatch catFrameMismatch = catFrameMismatchesArrayList.get(i);
                    markables.appendChild(catFrameMismatch.toCafXML(xmldoc));
                }
                root.appendChild(markables);
            }
            Element relations = xmldoc.createElement("Relations");
            root.appendChild(relations);

            // Serialisation through Tranform.
            DOMSource domSource = new DOMSource(xmldoc);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.INDENT,"yes");
            serializer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            StreamResult streamResult = null;
            streamResult = new StreamResult(new OutputStreamWriter(stream));
            serializer.transform(domSource, streamResult);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getTokensFromSourceMention (CatFrameRelation catFrameRelation) {
        ArrayList<String> tokens = new ArrayList<String>();
        for (int i = 0; i < catMarkableArrayList.size(); i++) {
            CatMarkable markable = catMarkableArrayList.get(i);
            if (markable.getId().equals(catFrameRelation.getSource())) {
                tokens = markable.getTokenAnchors();
                break;
            }
        }
        return tokens;
    }


}
