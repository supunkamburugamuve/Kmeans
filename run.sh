#!/bin/sh 
if [ -z "$JAVA_HOME" ]; then
    echo "You must set the JAVA_HOME variable before running k-means."
    exit 1
fi

$JAVA_HOME/bin/java -cp target/k-means-1.0.jar b565.hw2.kmeans.Application



