# No more Shortcuts: Towards Faithful and Efficient Access Control in Java

## Authors 

* [Philipp Holzinger](http://www.ec-spride.tu-darmstadt.de/forschungsgruppen/secure-software-engineering/staff/philipp-holzinger/)
* [Ben Hermann](http://www.thewhitespace.de)
* [Johannes Lerch](http://www.stg.tu-darmstadt.de/staff/johannes_lerch/)
* [Eric Bodden](http://www.bodden.de)
* [Mira Mezini](http://www.stg.tu-darmstadt.de/staff/mira_mezini/)

## Abstract

While the Java runtime is installed on billions of devices and servers worldwide, it remains a primary attack vector for online criminals. 
As recent studies show, the majority of all exploited Java vulnerabilities comprise incorrect or insufficient implementations of access-control checks. This paper for the first time studies the problem in depth. 
As we find, attacks are enabled by shortcuts that short-circuit Java's general principle of stack-based access control.
These shortcuts, originally introduced for ease of use and to improve performance, cause Java to elevate the privileges of code implicitly. 
As we show, this creates many pitfalls for software maintenance, making it all too easy for maintainers of the runtime to introduce blatant confused-deputy vulnerabilities even by just applying normally semantics-preserving refactorings.

How can this problem be solved? 
Can one implement Java's access control without shortcuts, and if so, does this implementation remain usable and efficient? 
To answer those questions, we conducted a tool-assisted adaptation of the Java Class Library (JCL), avoiding (most) shortcuts and therefore moving to a fully explicit model of privilege elevation.
As we show, the proposed changes significantly harden the JCL against attacks: they effectively hinder the introduction of new confused-deputy vulnerabilities in future library versions, and successfully restrict the capabilities of attackers when exploiting certain existing vulnerabilities.

We discuss usability considerations, and through a set of large-scale experiments show that with current JVM technology such a faithful implementation of stack-based access control induces no observable performance loss.

## Artifact

This repository contains all available artifacts for the paper and links to the source code of the implementation.

These files are made available for academic purposes only. 
Do not redistribute these files. Do not distribute direct links to these files, link to this page instead.
