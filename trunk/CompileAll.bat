@echo off
set CLASSPATH=.;build\classes
set in=wz\
set out=out\
set log=wzlog.txt
java kvjcompiler.WzCompiler -i "%in%" -o "%out%" -l "%log%" -f String.wz -f Skill.wz -f Reactor.wz -f Mob.wz -f Item.wz -f Character.wz -f Map.wz
pause