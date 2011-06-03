#!/bin/sh
#
# Test the code quickly without using the clumsy ant script.

javac src/ToursFinder.java -classpath build -d build &&
java -cp build ads1ss11.pa2.Main
