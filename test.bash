#!/usr/bin/env bash

gw clean build

java -cp build/libs/dependencies/*:build/libs/drools_playground.jar cz.skalicky.drools.playground.Main
