#!/usr/bin/env bash

LIB=target/CatFrameNet-1.0-SNAPSHOT-jar-with-dependencies.jar
FRAMENET=vua-resources/frRelation.xml
INPUT=annotated_files
EXTENSION=".xml"

java  -Xmx4000m -cp $LIB MatchCatFrameNetFiles --folder $INPUT --extension $EXTENSION--framenet $FRAMENET
