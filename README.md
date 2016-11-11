# CatFrameNet

This program reads all CAT files recursively from a specified folder. It checks the file names to find a pair of CAT files to compare that the FrameNet Frame annotations. It creates a new CAT file with mismatches. The new file has the same name as the first CAT file but adds ".mismatch.xml" as an extension. In the mismatch file, you find markables for each mismatch. Mismatches can be different frames or related frames.

    <DIFFERENTFRAME anno1-value="Containing" anno2-value="Quantity" m_id="99">
      <token_anchor t_id="631"/>
    </DIFFERENTFRAME>
    <DIFFERENTFRAME anno1-value="Existence" anno2-value="Locating" m_id="102">
      <token_anchor t_id="685"/>
    </DIFFERENTFRAME>
    <RELATEDFRAME anno1-value="Existence" anno2-value="State_of_entity" m_id="106">
      <token_anchor t_id="747"/>
    </RELATEDFRAME>

Related frames share a parent frame in FrameNet.

The program also creates a cat.log file in the folder that is given as input. This cat.log file provides an overview of the comparison that looks as follows:

Frames	Mentions1	Mentions2	Matches	Mismatches
Accompaniment	0	1	0
Accomplishment	4	4	3	m-Delivery:1
Activity_start	1	1	1
Activity_stop	0	1	0
Adopt_selection	1	0	0	m-None:1
Amounting_to	0	2	0
Arriving	1	0	0	m-Progress:1
Attending	0	2	0
Avoiding	1	0	0	m-Separating:1
Awareness	1	0	0	m-Judgment:1
Be_subset_of	1	0	0	m-Being_in_control:1
Becoming	1	0	0	m-WrongRelation:1
etc...

It contains a list of the detected frames with the number of mentions by each annotator (Mentions1 and Mentions2), the number of matches across these mentions, and the mismatches. The mismatches are represented as a list of frames with counts and the prefix m- for mismatch and r- for related.

Full Inter-Annotator-Agreement means equal number of mentions and matches and no mismatches. If there are more mentions than matches + mismatches this means that there is a difference in coverage across the annotators.

Installation:

Clone this project with git and run the install.sh script. For installation, you need to install maven (http://maven.apache.org/install.html) locally.
If maven is installed do:

> git clone https://github.com/cltl/CatFrameNet.git

> cd CatFrameNet

> ./install.sh

Running software:

You can use the matchFrameNetFrames.sh script to run the software. Make sure you adapt the script to point to the right input directory and provide the correct extension.
