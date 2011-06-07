#!/bin/sh
#
# Test the code quickly without using the clumsy ant script.

test build/ads1ss11/pa2/ToursFinder.class -nt src/ToursFinder.java ||
javac src/ToursFinder.java -classpath build -d build &&
java -cp build ads1ss11.pa2.Main -d
