#!/bin/bash
# The entrypoint for the docker container. This is a copy of the CompileAll.sh
# script with modifications to run via maven.

IN=${DATA_DIR:-wz/}
OUT=${OUTPUT_DIR:-out/}
LOG="wzlog.txt"

mvn exec:java -Dexec.mainClass="kvjcompiler.WzCompiler" \
    -Dexec.args="-i "$IN" -o "$OUT" -l "$LOG" \
        -f String.wz \
        -f Quest.wz \
        -f Skill.wz \
        -f Reactor.wz \
        -f Npc.wz \
        -f Mob.wz \
        -f Item.wz \
        -f Character.wz \
        -f Map.wz \
        -d custom_drops.txt \
        -m no_mesos.txt \
        -q quest_drops.txt \
    "
