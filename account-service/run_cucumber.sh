#!/bin/bash

# author Kasper (s141250)
# author Sebastian (s135243)

mvn exec:java                                  \
    -Dexec.classpathScope=test                 \
    -Dexec.mainClass=io.cucumber.core.cli.Main \
    -Dexec.args="./src/test/resources/cucumber/ --glue cucumber"
