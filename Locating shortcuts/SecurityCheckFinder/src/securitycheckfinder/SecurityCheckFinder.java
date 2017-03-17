package securitycheckfinder;

import securitycheckfinder.util.Logger;
import securitycheckfinder.candidate.Candidate;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import securitycheckfinder.util.Globals;
import soot.G;
import soot.Main;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.options.Options;

public class SecurityCheckFinder {

    /**
     *
     * @param args Path to directory that contains class files.
     */
    public static void main(String... args) {
        if (args.length == 0) {
            Logger.error("no directory specified!");
        }
        File dir = new File(args[0]);
        if (dir.exists() == false) {
            Logger.error("directory not found!");
        }
        if (dir.isDirectory() == false) {
            Logger.error("not a directory!");
        }

        Logger.comment("VERBOSE=" + Globals.VERBOSE);
        Logger.comment("REPORTALL=" + Globals.REPORTALL);
        Logger.comment("PP=" + Globals.PP);
        Logger.comment("CHUNKSIZE=" + Globals.CHUNKSIZE);
        Logger.comment("searching class files in directory '" + dir.getAbsolutePath() + "' ...");
        ArrayList<String> classNames = getClasses(dir, dir);
        Logger.comment("found " + classNames.size() + " class files");

        // sort list of class names to make sure that files will
        // always be processed in the same order
        Collections.sort(classNames);

        // start analysis
        processClasses(dir.getAbsolutePath(), classNames);
    }

    /**
     * Reinitializes Soot
     *
     * @param baseDir Path to base directory that contains all classes
     * @param classNames List of classes to be loaded
     */
    protected static void initSoot(String baseDir, ArrayList<String> classNames) {
        G.reset();
        OutputStream out = new ByteArrayOutputStream();
        G.v().out = new PrintStream(out);

        // construct command line arguments for Soot
        String pp = "";
        if (Globals.PP == true) {
            pp = "-pp ";
        }
        String cmd = "-allow-phantom-refs " + pp + "-cp ";
        cmd += baseDir;
        cmd += " -keep-line-number -f n";

        StringBuilder classNameList = new StringBuilder();
        for (String className : classNames) {
            classNameList.append(" ").append(className);
        }
        cmd += classNameList.toString();

        String cmdLine[] = cmd.split(" ");
        Options.v().parse(cmdLine);
        Main.v().autoSetOptions();

        Logger.comment("loading necessary classes...");
        Scene.v().loadNecessaryClasses();
    }

    /**
     * Performs analysis of class files
     *
     * @param dir Base directory where all class files reside
     * @param completeList List of fully-qualifed Java class names
     */
    private static void processClasses(String dir, ArrayList<String> completeList) {
    	System.out.println("<entries>");
        processClasses(dir, completeList, 0, completeList.size());
        System.out.println("</entries>");
    }

    /**
     * Performs analysis of class files.
     *
     * Large numbers of class files cannot be processed all at once. If
     * required, this method splits the entire set of class files into smaller
     * chunks and processes them one after another.
     *
     * @param dir Base directory where all class files reside.
     * @param completeList List of fully-qualified Java class names.
     * @param index Index of the first element of completeList within the entire
     * set of class names.
     * @param total Number of class names in the entire set.
     */
    private static void processClasses(String dir, ArrayList<String> completeList, int index, int total) {
        ArrayList<String> classNames;
        ArrayList<String> remainingClassNames;

        int chunkSize = Globals.CHUNKSIZE;

        // split class files into smaller chunks if needed
        if (completeList.size() > chunkSize) {
            classNames = new ArrayList(completeList.subList(0, chunkSize));
            remainingClassNames = new ArrayList(completeList.subList(chunkSize, completeList.size()));
        } else {
            classNames = completeList;
            remainingClassNames = null;
        }

        initSoot(dir, classNames);

        for (SootClass sootClass : Scene.v().getApplicationClasses()) {
            index++;
            Logger.comment("processsing " + index + " of " + total + ": " + sootClass.getName());
            for (SootMethod method : sootClass.getMethods()) {
                if (method.isNative() == false) {
                    if (method.isAbstract() == false) {
                        MethodProcessor methodProcessor = new MethodProcessor(sootClass.getName());
                        ArrayList<Candidate> candidates;
                        // retrieve results from MethodProcessor
                        candidates = methodProcessor.processMethod(method.retrieveActiveBody());
                        // output results
                        for (Candidate candidate : candidates) {
                            candidate.printXML();
                        }
                    }
                }
            }
        }

        if (remainingClassNames != null) {
            // recursive call to process class files that weren't processed in this run
            processClasses(dir, remainingClassNames, index, total);
        }
    }

    /**
     * Searches a directory to retrieve a list of fully-qualifed Java class
     * names.
     *
     * @param baseDir Base directory that contains all class files.
     * @param searchDir Subdirectory of baseDir to search for class files.
     * @return List of fully-qualified Java class names.
     */
    protected static ArrayList<String> getClasses(File baseDir, File searchDir) {
        ArrayList<String> fileList = new ArrayList();

        if (searchDir.isDirectory()) {
            File[] files = searchDir.listFiles();
            for (File file : files) {
                fileList.addAll(getClasses(baseDir, file));
            }
        } else {
            if (searchDir.getAbsolutePath().endsWith(".class")) {
                fileList.add(getClassName(baseDir, searchDir));
            }
        }
        return fileList;
    }

    /**
     * Given a base directory and a Java class file, this method returns the
     * Java class file's fully-qualifed name.
     *
     * @param baseDir Base directory where class file resides.
     * @param classFile Java class file.
     * @return Fully-qualified name of the Java class file.
     */
    private static String getClassName(File baseDir, File classFile) {
        String className = classFile.getAbsolutePath().substring(baseDir.getAbsolutePath().length());
        className = className.replace("\\", ".");
        className = className.replace("/", ".");
        if (className.startsWith(".")) {
            className = className.substring(1);
        }
        if (className.endsWith(".class")) {
            className = className.substring(0, className.length() - ".class".length());
        }
        return className;
    }
}
