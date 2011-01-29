@echo off
set CLASSPATH=.;KvjCompiler\build\classes
set in=C:\Users\Kevin\Java Projects\KiniroMS\wz
set out=C:\Users\Kevin\Java Projects\KvjBin\out
set log=wzlog.txt
echo Compiling String.wz
java kvjcompiler.WzCompiler -in "%in%" -out "%out%" -log "%log%" -file String.wz 
echo.
echo Compiling Skill.wz
java kvjcompiler.WzCompiler -in "%in%" -out "%out%" -log "%log%" -file Skill.wz 
echo.
echo Compiling Reactor.wz
java kvjcompiler.WzCompiler -in "%in%" -out "%out%" -log "%log%" -file Reactor.wz 
echo.
echo Compiling Mob.wz
java kvjcompiler.WzCompiler -in "%in%" -out "%out%" -log "%log%" -file Mob.wz 
echo.
echo Compiling Item.wz
java kvjcompiler.WzCompiler -in "%in%" -out "%out%" -log "%log%" -file Item.wz 
echo.
echo Compiling Character.wz
java kvjcompiler.WzCompiler -in "%in%" -out "%out%" -log "%log%" -file Character.wz 
echo.
echo Compiling Map.wz
java kvjcompiler.WzCompiler -in "%in%" -out "%out%" -log "%log%" -file Map.wz 
echo.
pause