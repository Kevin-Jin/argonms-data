#!/bin/bash

export CLASSPATH=".:KvjCompiler/build/classes"
IN="/home/kevin/KvjBin/wz/"
OUT="/home/kevin/KvjBin/out/"
LOG="wzlog.txt"
echo Compiling String.wz
java kvjcompiler.WzCompiler -in "$IN" -out "$OUT" -log "$LOG" -file String.wz
echo Compiling Reactor.wz
java kvjcompiler.WzCompiler -in "$IN" -out "$OUT" -log "$LOG" -file Skill.wz
echo Compiling Mob.wz
java kvjcompiler.WzCompiler -in "$IN" -out "$OUT" -log "$LOG" -file Reactor.wz
echo Compiling Skill.wz
java kvjcompiler.WzCompiler -in "$IN" -out "$OUT" -log "$LOG" -file Mob.wz
echo Compiling Item.wz
java kvjcompiler.WzCompiler -in "$IN" -out "$OUT" -log "$LOG" -file Item.wz
echo Compiling Map.wz
java kvjcompiler.WzCompiler -in "$IN" -out "$OUT" -log "$LOG" -file Map.wz
