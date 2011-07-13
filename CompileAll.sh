#!/bin/bash

export CLASSPATH=".:bin:dist/KvjBin.jar"
IN="wz/"
OUT="out/"
LOG="wzlog.txt"
java kvjcompiler.WzCompiler -i "$IN" -o "$OUT" -l "$LOG" -f String.wz -f Quest.wz -f Skill.wz -f Reactor.wz -f Npc.wz -f Mob.wz -f Item.wz -f Character.wz -f Map.wz
