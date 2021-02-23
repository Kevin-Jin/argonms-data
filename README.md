# kvjcompiler

If your XML WZ files are not in a folder named "wz" in your root directory, then
you will have to edit the batch file (on Windows), or the bash file (on Linux).
Likewise, you will have to edit those files if you want to change where the
directory to place the converted files or if you want to rename the wzlog.txt
file. Make sure that if you are using Windows and need to put backspaces in the
path that you escape them (just double each backspace) so that Java understands
that it is not an escape sequence. Unlike in earlier revisions, the paths do not
have to have a trailing directory delimiter, but you should place one anyway.

You may also run the compiler on the data directory using docker.

```bash
# Path to the XML WZ files
export DATA_DIR=../wz
export OUTPUT_DIR=../wz-kvj
docker-compose build
docker-compose run app
```

The environment variables may be put into a `.env` file at the root of the
directory.

No external libraries are necessary. The only API needed is StAX (Streaming API
for XML), which should be included with JRE 6 and later. An independent
LittleEndianWriter is used instead of the one in OdinMS, and the sample
WzInterpreter that is included uses its own LittleEndianReader. If you wish to
use the OdinMS classes instead of the included ones, some code may need to be
modified since they use 2-byte chars (UTF-8?) instead of 1-byte, and you have to
be careful to read/write null terminated strings instead of a MapleAsciiString
(which is basically just an ASCII encoding with a 2-byte prefix that indicates
the string's length).

If you wish to find an example on the usage/parsing of these files, just take a
look in the ArgonMS source files in the package and any subpackages of
argonms.loading. I removed KvjInterpreter, which was present in earlier
revisions, because it was outdated and it was pointless to maintain when there
is a better demonstration available.
