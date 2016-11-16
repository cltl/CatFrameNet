/**
 * Created by piek on 06/11/2016.
 */

import cat.CatFile;
import cat.CatFrameMismatch;
import cat.CatFrameRelation;
import vu.wntools.framenet.FrameNetFrameReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MatchCatFrameNetFiles {

    static public class Count {
        private String name;
        private int freq;

        public Count() {
            this.name = "";
            this.freq = 0;
        }

        public int getFreq() {
            return freq;
        }

        public void setFreq(int freq) {
            this.freq = freq;
        }

        public void incrFreq() {
            this.freq++;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static ArrayList<String> frames = new ArrayList<String>();
    static HashMap<String, Integer> frameCounts1 = new HashMap<String, Integer>();
    static HashMap<String, Integer> frameCounts2 = new HashMap<String, Integer>();
    static HashMap<String, ArrayList<Count>> confusionMatrix = new HashMap<String, ArrayList<Count>>();
    static HashMap<String, ArrayList<Count>> relatedMatrix = new HashMap<String, ArrayList<Count>>();
    static HashMap<String, Integer> matchMatrix = new HashMap<String, Integer>();
    static FrameNetFrameReader frameNetFrameReader;

    static public void main(String[] args) {
        String pathToFrameNet = "/Code/vu/newsreader/vua-resources/frRelation.xml";
        String inputFolder = "/Users/piek/Desktop/FrameNet/annotated_files";
        String extension = ".xml";
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equalsIgnoreCase("--folder") && args.length>(i+1)) {
                inputFolder = args[i+1];
            }
            else if (arg.equalsIgnoreCase("--framenet") && args.length>(i+1)) {
                pathToFrameNet = args[i+1];
            }
            else if (arg.equalsIgnoreCase("--extension") && args.length>(i+1)) {
                extension = args[i+1];
            }
        }
        ArrayList<File> files = makeRecursiveFileList(new File(inputFolder), extension);
        frameNetFrameReader = new FrameNetFrameReader();
        frameNetFrameReader.parseFile(pathToFrameNet);
        for (int i = 0; i < files.size(); i++) {
            File file1 = files.get(i);
            for (int j = i+1; j < files.size(); j++) {
                File file2 =  files.get(j);
                if (file1.getName().equals(file2.getName())) {
                    compareCatFiles(file1, file2);
                }
            }
        }


        try {
            Collections.sort(frames);
            String str = "";
            OutputStream fos = new FileOutputStream(inputFolder+"/"+"cat.log");
/*            str += "\t";
            for (int i = 0; i < frames.size(); i++) {
                String frame = frames.get(i);
                str += frame+"\t\t";
            }
            str += "\n";
            fos.write(str.getBytes());

            for (int i = 0; i < frames.size(); i++) {
                String frame = frames.get(i);
                str = frame;
                ArrayList<Count> relatedCounts = relatedMatrix.get(frame);
                ArrayList<Count> mismatchCounts = confusionMatrix.get(frame);
                for (int j = 0; j < frames.size(); j++) {
                    String frame2 = frames.get(j);
                    if (frame.equals(frame2)) {
                        Integer cnt = matchMatrix.get(frame);
                        if (cnt!=null) {
                            str += "\t-\t"+cnt;
                        }
                        else {
                            str += "\t-\t";
                        }
                    }
                    else {
                        Integer related = 0;
                        Integer mismatch = 0;
                        if (relatedCounts!=null) {
                            for (int k = 0; k < relatedCounts.size(); k++) {
                                Count count = relatedCounts.get(k);
                                if (count.getName().equals(frame2)) {
                                    related = count.getFreq();
                                    System.out.println("related = " + related);
                                    break;
                                }
                            }
                        }
                        if (mismatchCounts!=null) {
                            for (int k = 0; k < mismatchCounts.size(); k++) {
                                Count count = mismatchCounts.get(k);
                                if (count.getName().equals(frame2)) {
                                    mismatch = count.getFreq();
                                    System.out.println("mismatch = " + mismatch);
                                    break;
                                }
                            }
                        }
                        str += "\t";
                        if (mismatch>0) str +=mismatch;
                        str +="\t";
                        if (related>0) str += related;
                    }
                }
                str += "\n";
                fos.write(str.getBytes());
            }*/

            str += "Frames\tMentions1\tMentions2\tMatches\tMismatches\n";
            fos.write(str.getBytes());

            for (int i = 0; i < frames.size(); i++) {
                String frame = frames.get(i);
                str = frame;
                Integer frameCnt = frameCounts1.get(frame);
                if (frameCnt!=null) { str += "\t"+frameCnt;}
                else { str += "\t0"; }
                frameCnt = frameCounts2.get(frame);
                if (frameCnt!=null) { str += "\t"+frameCnt;}
                else { str += "\t0"; }
                Integer matchCnt = matchMatrix.get(frame);
                if (matchCnt!=null) { str += "\t"+matchCnt;}
                else { str += "\t0"; }
                ArrayList<Count> relatedCounts = relatedMatrix.get(frame);
                if (relatedCounts!=null) {
                    for (int k = 0; k < relatedCounts.size(); k++) {
                        Count count = relatedCounts.get(k);
                        str += "\tr-"+count.getName()+":"+count.getFreq();
                    }
                }
                ArrayList<Count> mismatchCounts = confusionMatrix.get(frame);
                if (mismatchCounts!=null) {
                    for (int k = 0; k < mismatchCounts.size(); k++) {
                        Count count = mismatchCounts.get(k);
                        str += "\tm-"+count.getName()+":"+count.getFreq();
                    }
                }
                str += "\n";
                fos.write(str.getBytes());
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void compareCatFiles (File catInputFile1, File catInputFile2) {
        ArrayList<String> mIds = new ArrayList<String>();
        CatFile catFile1 = new CatFile();
        CatFile catFile2 = new CatFile();
        System.out.println("catFile1 = " + catInputFile1.getAbsolutePath());
        catFile1.parseFile(catInputFile1.getAbsolutePath());
        catFile2.parseFile(catInputFile2.getAbsolutePath());
        ArrayList<CatFrameMismatch> catFrameMismatchArrayList = new ArrayList<CatFrameMismatch>();
        compareFile(catFile1, catFile2, catFrameMismatchArrayList, mIds, frameCounts1, frameCounts2);
        System.out.println("catFrameMismatchArrayList.size() = " + catFrameMismatchArrayList.size());
        compareFile(catFile2, catFile1, catFrameMismatchArrayList, mIds, frameCounts2, frameCounts1);
        System.out.println("catFrameMismatchArrayList.size() = " + catFrameMismatchArrayList.size());
        catFile1.catFrameMismatchesArrayList = catFrameMismatchArrayList;

        String ann1 = catInputFile1.getParentFile().getName();
        String ann2 = catInputFile2.getParentFile().getName();
        String filePath = catFile1.docName;
        int idx = filePath.lastIndexOf("/");
        if (idx>-1) filePath = filePath.substring(idx+1);
        catFile1.docName = ann1+":"+ann2+":"+filePath;

        try {
            OutputStream fos = new FileOutputStream(catInputFile1.getAbsolutePath()+".mismatch.xml");
            catFile1.writeCatMismatchToStream(fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void compareFile (CatFile catFile1, CatFile catFile2, ArrayList<CatFrameMismatch> catFrameMismatchArrayList, ArrayList<String> mIds, HashMap<String, Integer> frameCounts1, HashMap<String, Integer> frameCounts2) {
        for (int i = 0; i < catFile1.catFrameRelationArrayList.size(); i++) {
            CatFrameRelation catFrameRelation1 = catFile1.catFrameRelationArrayList.get(i);
            if (mIds.contains(catFrameRelation1.getSource())) {
               //// this source was already matched so we skip it
            }
            else {
                mIds.add(catFrameRelation1.getSource());
                boolean relationMatch = false;
                for (int j = 0; j < catFile2.catFrameRelationArrayList.size(); j++) {
                    CatFrameRelation catFrameRelation2 = catFile2.catFrameRelationArrayList.get(j);
                    if (catFrameRelation1.getId().equals(catFrameRelation2.getId())) {
                        relationMatch = true;
                        String anno1Value = catFrameRelation1.getFrame();
                        String anno2Value = catFrameRelation2.getFrame();
                        if (!frames.contains(anno1Value)) frames.add(anno1Value);
                        if (!frames.contains(anno2Value)) frames.add(anno2Value);
                        if (anno1Value.equals("Accompaniment")) {
                            System.out.println("catFile1.docName = " + catFile1.docName);
                        }if (anno2Value.equals("Accompaniment")) {
                            System.out.println("catFile2.docName = " + catFile2.docName);
                        }
                        if (frameCounts1.containsKey(anno1Value)) {
                            Integer cnt = frameCounts1.get(anno1Value);
                            cnt++;
                            frameCounts1.put(anno1Value, cnt);
                        } else {
                            frameCounts1.put(anno1Value, 1);
                        }
                        if (frameCounts2.containsKey(anno2Value)) {
                            Integer cnt = frameCounts2.get(anno2Value);
                            cnt++;
                            frameCounts2.put(anno2Value, cnt);
                        } else {
                            frameCounts2.put(anno2Value, 1);
                        }
                        if (anno1Value.equals(anno2Value)) {
                            //// we have a frame match for a markable!!!!
                            //System.out.println("anno1Value = " + anno1Value);
                            CatFrameMismatch catFrameMismatch = new CatFrameMismatch();
                            catFrameMismatch.setName("SAMEFRAME");
                            catFrameMismatch.setAnno1Value(anno1Value);
                            catFrameMismatch.setAnno2Value(anno2Value);
                            catFrameMismatch.setId(catFrameRelation1.getId());
                            catFrameMismatch.setTokenAnchors(catFile1.getTokensFromSourceMention(catFrameRelation1));
                            catFrameMismatchArrayList.add(catFrameMismatch);
                            if (matchMatrix.containsKey(anno1Value)) {
                                Integer cnt = matchMatrix.get(anno1Value);
                                cnt++;
                                matchMatrix.put(anno1Value, cnt);
                            } else {
                                matchMatrix.put(anno1Value, 1);
                            }
                        } else {
                            ///// there is a mismatch
                            if (frameNetFrameReader.frameNetConnected(anno1Value, anno2Value)) {
                                //RELATEDFRAME
                                CatFrameMismatch catFrameMismatch = new CatFrameMismatch();
                                catFrameMismatch.setName("RELATEDFRAME");
                                catFrameMismatch.setAnno1Value(anno1Value);
                                catFrameMismatch.setAnno2Value(anno2Value);
                                catFrameMismatch.setId(catFrameRelation1.getId());
                                catFrameMismatch.setTokenAnchors(catFile1.getTokensFromSourceMention(catFrameRelation1));
                                catFrameMismatchArrayList.add(catFrameMismatch);
                                if (relatedMatrix.containsKey(anno1Value)) {
                                    ArrayList<Count> m = relatedMatrix.get(anno1Value);
                                    boolean match = false;
                                    for (int k = 0; k < m.size(); k++) {
                                        Count count = m.get(k);
                                        if (count.getName().equals(anno2Value)) {
                                            count.incrFreq();
                                            match = true;
                                            break;
                                        }
                                    }
                                    if (!match) {
                                        Count count = new Count();
                                        count.setName(anno2Value);
                                        count.setFreq(1);
                                        m.add(count);
                                    }
                                    relatedMatrix.put(anno1Value, m);
                                } else {
                                    ArrayList<Count> m = new ArrayList<Count>();
                                    Count count = new Count();
                                    count.setName(anno2Value);
                                    count.setFreq(1);
                                    m.add(count);
                                    relatedMatrix.put(anno1Value, m);
                                }
                            } else {
                                //DIFFERENTFRAME
                                CatFrameMismatch catFrameMismatch = new CatFrameMismatch();
                                catFrameMismatch.setName("DIFFERENTFRAME");
                                catFrameMismatch.setAnno1Value(anno1Value);
                                catFrameMismatch.setAnno2Value(anno2Value);
                                catFrameMismatch.setId(catFrameRelation1.getId());
                                catFrameMismatch.setTokenAnchors(catFile1.getTokensFromSourceMention(catFrameRelation1));
                                catFrameMismatchArrayList.add(catFrameMismatch);
                                if (confusionMatrix.containsKey(anno1Value)) {
                                    ArrayList<Count> m = confusionMatrix.get(anno1Value);
                                    boolean match = false;
                                    for (int k = 0; k < m.size(); k++) {
                                        Count count = m.get(k);
                                        if (count.getName().equals(anno2Value)) {
                                            count.incrFreq();
                                            match = true;
                                            break;
                                        }
                                    }
                                    if (!match) {
                                        Count count = new Count();
                                        count.setName(anno2Value);
                                        count.setFreq(1);
                                        m.add(count);
                                    }
                                    confusionMatrix.put(anno1Value, m);
                                } else {
                                    ArrayList<Count> m = new ArrayList<Count>();
                                    Count count = new Count();
                                    count.setName(anno2Value);
                                    count.setFreq(1);
                                    m.add(count);
                                    confusionMatrix.put(anno1Value, m);
                                }
                            }
                        }
                        break;
                    }
                }
                if (!relationMatch) {
                    ///// there is a difference in annotations!!!!!
                    ///// we store this as a mismatch
                    String anno1Value = catFrameRelation1.getFrame();
                    String anno2Value = "NOFRAME";
                    if (!frames.contains(anno2Value)) frames.add(anno2Value);
                    if (!frames.contains(anno1Value)) frames.add(anno1Value);
                    if (frameCounts1.containsKey(anno1Value)) {
                        Integer cnt = frameCounts1.get(anno1Value);
                        cnt++;
                        frameCounts1.put(anno1Value, cnt);
                    } else {
                        frameCounts1.put(anno1Value, 1);
                    }
                    if (frameCounts2.containsKey(anno2Value)) {
                        Integer cnt = frameCounts2.get(anno2Value);
                        cnt++;
                        frameCounts2.put(anno2Value, cnt);
                    } else {
                        frameCounts2.put(anno2Value, 1);
                    }
                    //DIFFERENTFRAME
                    CatFrameMismatch catFrameMismatch = new CatFrameMismatch();
                    catFrameMismatch.setName("DIFFERENTFRAME");
                    catFrameMismatch.setAnno1Value(anno1Value);
                    catFrameMismatch.setAnno2Value(anno2Value);
                    catFrameMismatch.setId(catFrameRelation1.getId());
                    catFrameMismatch.setTokenAnchors(catFile1.getTokensFromSourceMention(catFrameRelation1));
                    catFrameMismatchArrayList.add(catFrameMismatch);
                    if (confusionMatrix.containsKey(anno1Value)) {
                        ArrayList<Count> m = confusionMatrix.get(anno1Value);
                        boolean match = false;
                        for (int k = 0; k < m.size(); k++) {
                            Count count = m.get(k);
                            if (count.getName().equals(anno2Value)) {
                                count.incrFreq();
                                match = true;
                                break;
                            }
                        }
                        if (!match) {
                            Count count = new Count();
                            count.setName(anno2Value);
                            count.setFreq(1);
                            m.add(count);
                        }
                        confusionMatrix.put(anno1Value, m);
                    } else {
                        ArrayList<Count> m = new ArrayList<Count>();
                        Count count = new Count();
                        count.setName(anno2Value);
                        count.setFreq(1);
                        m.add(count);
                        confusionMatrix.put(anno1Value, m);
                    }
                }
            }
        }
    }

    static public ArrayList<File> makeRecursiveFileList(File inputFile, String theFilter) {
        ArrayList<File> acceptedFileList = new ArrayList<File>();
        File[] theFileList = null;
        if ((inputFile.canRead())) {
            theFileList = inputFile.listFiles();
            for (int i = 0; i < theFileList.length; i++) {
                File newFile = theFileList[i];
                if (newFile.isDirectory()) {
                    ArrayList<File> nextFileList = makeRecursiveFileList(newFile, theFilter);
                    acceptedFileList.addAll(nextFileList);
                } else {
                    if (newFile.getName().endsWith(theFilter)) {
                        acceptedFileList.add(newFile);
                    }
                }
                // break;
            }
        } else {
            System.out.println("Cannot access file:" + inputFile + "#");
            if (!inputFile.exists()) {
                System.out.println("File/folder does not exist!");
            }
        }
        return acceptedFileList;
    }
}
