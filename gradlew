#!/usr/bin/env sh
##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

APP_NAME="Gradle"
PRG="$0"

# Resolve links - $0 may be a link
while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`"/$link"
  fi
done

SAVED="`pwd`"
PRGDIR=`dirname "$PRG"`
cd "$PRGDIR" >/dev/null
PRGDIR=`pwd -P`
cd "$SAVED" >/dev/null

# Setup default JVM options
if [ -z "$JAVA_HOME" ] ; then
  _RUN_JAVA="java"
else
  _RUN_JAVA="$JAVA_HOME/bin/java"
fi

# Allow running with spaces in the path
# Collection of all arguments and JVM options
RUN_JAVA_CMD="$_RUN_JAVA"

# Execute Gradle with the wrapper jar
exec "$RUN_JAVA_CMD" ${DEFAULT_JVM_OPTS} -jar "$PRGDIR/gradle/wrapper/gradle-wrapper.jar" "$@"
