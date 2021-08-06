@echo off

for %%F in (%0) do set dirname=%%~dpF

set PHANTOM_BIN=%dirname%\phantomjs-1.9.2-windows\phantomjs.exe
"%JAVA_HOME%\bin\java" -DphantomBin="%PHANTOM_BIN%" -jar "%dirname%\gxt-themebuilder.jar" %*
