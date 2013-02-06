::
:: KvJ Compiler for XML WZ data files
:: Copyright (C) 2010-2013  GoldenKevin
::
:: This program is free software: you can redistribute it and/or modify
:: it under the terms of the GNU Affero General Public License as
:: published by the Free Software Foundation, either version 3 of the
:: License, or (at your option) any later version.
::
:: This program is distributed in the hope that it will be useful,
:: but WITHOUT ANY WARRANTY; without even the implied warranty of
:: MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
:: GNU Affero General Public License for more details.
::
:: You should have received a copy of the GNU Affero General Public License
:: along with this program.  If not, see <http://www.gnu.org/licenses/>.
::

@echo off
set CLASSPATH=.;bin;dist\KvjBin.jar
set in=wz\\
set out=out\\
set log=wzlog.txt
java kvjcompiler.WzCompiler -i "%in%" -o "%out%" -l "%log%" -f String.wz -f Quest.wz -f Skill.wz -f Reactor.wz -f Npc.wz -f Mob.wz -f Item.wz -f Character.wz -f Map.wz -d custom_drops.txt -m no_mesos.txt -q quest_drops.txt
pause
