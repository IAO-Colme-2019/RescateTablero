set PATH=C:\Java\jdk1.8.0_*\bin;C:\Program Files\Java\jdk1.8.0_*\bin;%PATH%

set CLASSPATH=".;^
lib/commons-codec/commons-codec-1.3.jar;^
lib/bcel.jar;^
lib/GraphLayout.jar;^
lib/jadex_examples.jar;^
lib/jadex_rt.jar;^
lib/jadex_standalone.jar;^
lib/jadex_tools.jar;^
lib/janino.jar;^
lib/jhall.jar;^
lib/jibx-bind.jar;^
lib/jibx-extras.jar;^
lib/jibx-run.jar;^
lib/nuggets.jar;^
lib/xmlpull_1_1_4.jar;^
lib/xpp3.jar"

java jadex.adapter.standalone.Platform
