set PATH=C:\Java\jdk1.8.0_73\bin;C:\Java\jdk1.8.0_77\bin;C:\Program Files\Java\jdk1.8.0_191\bin;C:\Program Files\Java\jdk-9.0.4\bin;C:\Program Files\Java\jdk1.8.0_121\bin;%PATH%
set classpath=src\lib\Base64.jar;src\lib\GraphLayout.jar;src\lib\bcel.jar;\src\lib\http.jar;src\lib\iiop.jar;src\lib\jade.jar;src\lib\jadeTools.jar;src\lib\jadex_examples.jar;src\lib\jadex_jadeadapter.jar;src\lib\jadex_rt.jar;src\lib\jadex_standalone.jar;src\lib\jadex_tools.jar;src\lib\jhall.jar;src\lib\jibx-bind.jar;src\lib\jibx-extras.jar;src\lib\jibx-run.jar;src\lib\xpp3.jar;src;src\lib\commons-commons\commons-codec-1.3.jar

javac src\rescate\ontologia\acciones\*java
javac src\rescate\ontologia\conceptos\*java
javac src\rescate\ontologia\predicados\*java
javac src\rescate\tablero\planes\*java

javac src\rescate\jugador\planes\*java

pause
jar cvf RescateTablero * 

