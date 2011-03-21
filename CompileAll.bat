@echo off
set CLASSPATH=.;KvjCompiler\build\classes
set in=C:\Users\Kevin\Java Projects\KiniroMS\wz
set out=C:\Users\Kevin\Java Projects\KvjBin\out
set log=wzlog.txt
java kvjcompiler.WzCompiler -i "%in%" -o "%out%" -l "%log%" -f String.wz -f Skill.wz -f Reactor.wz -f Mob.wz -f Item.wz -f Character.wz -f Map.wz
pause