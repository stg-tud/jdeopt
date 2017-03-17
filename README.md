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

<h3>Contents</h3>

* Java application that searches class files for calls, conditionals, and throw-statements (Location: [Locating shortcuts/SecurityCheckFinder](../master/Locating%20shortcuts/SecurityCheckFinder))
* List of candidate methods (Location: [Locating shortcuts/methods.html](../master/Locating%20shortcuts/methods.html))
* Modified source code (Location: [Removing shortcuts/modified](../master/Removing%20shortcuts/modified))
* Instrumentation tool (Location: [Instrumentation](../master/Instrumentation))
* Micro benchmarking tool (Location: [Performance evaluation/PerformanceTest](../master/Performance%20evaluation/PerformanceTest))
* Custom callback class for DaCapo (Location: [Performance evaluation/Callback](../master/Performance%20evaluation/Callback))
* Results of DaCapo benchmark runs (Location: [Performance evaluation/Results](../master/Performance%20evaluation/Results))
* Java application that counts method calls (Location: [Call statistics/CallCounter](../master/Call%20statistics/CallCounter))
* Results of counting method calls (Location: [Call statistics/Results/callcounts.pdf](../master/Call%20statistics/Results/callcounts.pdf))


## Steps for Reproduction

### 0 Setup

Download [openjdk-8-src-b132-03_mar_2014.zip](http://download.java.net/openjdk/jdk8/).

Setup two identical build environments for OpenJDK. 
One of the two builds serves as the unmodified reference implementation, the other one we use for modifications\n On our machine, we used 
`C:\OpenJDK\openjdk` for the original code, and `C:\OpenJDK\openjdkmod` for the other one.

Build the two copies of OpenJDK (run configure and `make all` for each of the two). 
In the following, we will refer to the build output directories as BUILDPATH, and MODBUILDPATH, respectively. 
On our machine, the paths are `C:\OpenJDK\openjdk\build\windows-x86_64-normal-server-release` and `C:\OpenJDK\openjdkmod\build\windows-x86_64-normal-server-release`.


### 1 Locating shortcuts

Extract `BUILDPATH\images\j2re-image\lib\rt.jar` to a folder of your choice. We will refer to this folder as `RTPATH`.
Build SecurityCheckFinder.
Run SecurityCheckFinder by providing `RTPATH` as command line argument and store its output. In the configuration we provided for SecurityCheckFinder, it will simply read all .class files in `RTPATH` and search them for method calls, conditionals, and throw-statements. Its output contains all findings in an XML structure.
Search XML structure for calls to `sun.reflect.Reflection.getCallerClass` and `java.lang.SecurityManager.getClassContext` to get a list of candidate methods that may or may not contain a shortcut. 
We used the following XQuery:

```
let $calls := for $i in entries/call
where $i/destClass eq "sun.reflect.Reflection" and $i/destMethodName eq "getCallerClass" or $i/destClass eq "java.lang.SecurityManager" and $i/destMethodName eq "getClassContext"
return $i

let $methods := for $i in $calls
return $i/srcClass || "." || $i/srcMethodName || $i/srcMethodParams

let $methods := distinct-values($methods)

let $methodlist := for $method at $i in $methods
return <tr><td>{$i}</td><td>{$method}</td></tr>

return <html><body><table>{$candidates}</table></body></html>
```

The list of candidate methods comprises 86 methods, we provide a full list in methods.html.
Manually review the 86 candidate methods to identify those methods that contain a shortcut. 
A candidate method contains a shortcut if it contains a permission check (i.e., a call to `SecurityManager.check*`) that is guarded by a conditional that involves information about the caller/callstack. 
We marked such methods in the list of candidates in methods.html.


### 2 Removing shortcuts

Replace the relevant source files under `jdk\src\share\classes` (on our machine, the absolute path was `C:\OpenJDK\openjdkmod\jdk\src\share\classes`) with the modified code we provide under `Removing shortcuts/modified`.
Rebuild the modified OpenJDK by running `make all`.


### 3 Instrumentation

Build instrumentation tool.
Extract `MODBUILDPATH\images\j2sdk-image\jre\lib\rt.jar` to a folder of your choice. 
We will refer to this folder as `INPUTPATH`.
Run instrumentation tool by providing `INPUTPATH` as a first command line argument, and another path to an empty output folder as a second argument. The instrumentation tool will read in all class files contained in `INPUTPATH`, modify callers as necessary, and store processed class files in the output folder.
Replace the class files in `MODBUILDPATH\images\j2sdk-image\jre\lib\rt.jar` with the modified ones in the output folder.


### 4 Performance evaluation

Perform micro benchmark:

Build PerformanceTest, it contains a test class TestCollection that will be used for micro benchmarking. 
You can configure the number of iterations per run and whether a security manager is to be set or not using the constants in TestCollection.
Run TestCollection as a JUnit test to perform micro benchmark, make sure you run the test with the JRE whose performance you would like to measure. On our machine, the modified JRE was located in folder `C:\OpenJDK\openjdkmod\build\windows-x86_64-normal-server-release\images\j2sdk-image\jre`.


Run DaCapo benchmarks:

Build CustomCallback.
Download [DaCapo benchmark suite in version 9.12-bach](http://sourceforge.net/project/showfiles.php?group_id=172498).
Run DaCapo using command line `java -Xcomp -XX:CompileThreshold=1 -server -Xmx2g -Xms2g -Xbatch -cp ".;./mathlib.jar;./dacapo.jar" Harness -t 1 -c callback benchmarkname`.

`java` should point to the java binary of the JRE whose performance you would like to measure.
`mathlib.jar` should point to the Apache Commons Math library, which is a dependency of CustomCallback.
`callback` should point to compiled CustomCallback.
`benchmarkname` should be the name of the DaCapo benchmark whose performance you would like to measure, e.g., avrora.
When running jython benchmark, make sure to not use -Xcomp due to a [known bug](http://sourceforge.net/p/dacapobench/bugs/80/).

The results of our DaCapo benchmark runs can be found in `Performance evaluation/Results`.


### 5 Call statistics

Build CallCounter.
Run CallCounter with the JRE that you want to use for counting method calls to methods that were modified in the previous steps. 
It will instrument all system classes that contain a modified method and store the instrumented classes as .class files in folder `output`. 
Besides these modified system classes, it will also store helper classes in the same folder.
Run the DaCapo benchmarks with the class files in the `output` directory on the bootclasspath. For this, we used the following script:

```
set javapath=C:\OpenJDK\openjdk\build\windows-x86_64-normal-server-release\images\j2sdk-image\jre\bin\java
set systemclasspath=../CallCounter/output
set dacapopath=dacapo-9.12-bach.jar
set cmd=%javapath% -Xcomp -XX:CompileThreshold=1 -server -Xmx2g -Xms2g -Xbatch -Xbatch -Xbootclasspath/p:%systemclasspath% -jar %dacapopath% -t 1
set jythoncmd=%javapath% -XX:CompileThreshold=1 -server -Xmx2g -Xms2g -Xbatch -Xbatch -Xbootclasspath/p:%systemclasspath% -jar %dacapopath% -t 1
%cmd% avrora > results/avrora-results.txt
%cmd% fop > results/fop-results.txt
%cmd% h2 > results/h2-results.txt
%jythoncmd% jython > results/jython-results.txt
%cmd% luindex > results/luindex-results.txt
%cmd% lusearch > results/lusearch-results.txt
%cmd% pmd > results/pmd-results.txt
%cmd% sunflow > results/sunflow-results.txt
%cmd% tomcat > results/tomcat-results.txt
%cmd% tradebeans > results/tradebeans-results.txt
%cmd% tradesoap > results/tradesoap-results.txt
%cmd% xalan > results/xalan-results.txt
```

The helper classes will add a shutdown hook that will output call counts at the end of each benchmark run. 
In callcounts.pdf, we provide the results that we measured on our machine. 
Note, however, that the call counts for some of the benchmarks may slightly vary from run to run.

