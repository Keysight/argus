
export PHANTOM_BIN=`dirname $0`/phantomjs-1.9.2-macosx/bin/phantomjs
java -DphantomBin=$PHANTOM_BIN -jar `dirname $0`/gxt-themebuilder.jar $@

