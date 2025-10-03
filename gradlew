#!/bin/bash
# Simple gradle wrapper that uses installed gradle
if [ -z "$GRADLE_HOME" ]; then
    GRADLE_CMD="gradle"
else
    GRADLE_CMD="$GRADLE_HOME/bin/gradle"
fi

exec $GRADLE_CMD "$@"
