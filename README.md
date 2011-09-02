Jitescript - Java API for Bytecode
==================================

This project is inspired by [BiteScript](https://github.com/headius/bitescript). The goal is to produce a Java library with a similar API
so that bytescript generation can be as nice in Java as BiteScript makes it in JRuby.

![cloudbees rocks!](http://static-www.cloudbees.com/images/badges/BuiltOnDEV.png)

Requirements
==================================

You must download asm-jar from http://forge.ow2.org/projects/asm/ version 4.0_RC1 and manually install with maven

> mvn install:install-file -Dfile=asm-all-4.0_RC1.jar -DgroupId=all -DartifactId=asm-all -Dversion=4.0_RC1 -Dpackaging=jar
