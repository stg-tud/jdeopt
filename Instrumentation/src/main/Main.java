package main;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import processors.*;
import util.Logger;

/**
 *
 * @author Philipp Holzinger <philipp.holzinger@sit.fraunhofer.de>
 */
public class Main {

    private static File inputFolder;
    private static File outputFolder;
    private static ClassManager classManager;

    public static void main(String... args) throws Exception {
        Logger.println("Main", "Current time is " + getTime());
        parseArgs(args);
        Logger.println("Main", "Input: " + inputFolder);
        Logger.println("Main", "Output: " + outputFolder);

        ArrayList<String> classFilePaths = getClassFilePaths(inputFolder);
        classManager = new ClassManager(inputFolder, outputFolder);
        ArrayList<ClassProcessor> classProcessors = createProcessors();

        copyPatchClasses(classProcessors, classFilePaths);
        processClasses(classProcessors, classFilePaths);
        outputResults(classProcessors);
    }

    public static ClassManager getClassManager() {
        return classManager;
    }

    private static void outputResults(ArrayList<ClassProcessor> classProcessors) {
        for (ClassProcessor processor : classProcessors) {
            Logger.println("Main", "Result (" + processor.getProcessorName() + "): number of replaced calls:" + processor.getNumberOfReplacedCalls());
        }
    }

    private static ArrayList<ClassProcessor> createProcessors() {
        ArrayList<ClassProcessor> processors = new ArrayList();
        Class[] processorClasses = {
            GetEnclosingMethodProcessor.class,
            GetEnclosingConstructorProcessor.class,
            GetDeclaringClassProcessor.class,
            GetEnclosingClassProcessor.class,
            GetFieldsProcessor.class,
            GetMethodsProcessor.class,
            GetConstructorsProcessor.class,
            GetFieldProcessor.class,
            GetMethodProcessor.class,
            GetConstructorProcessor.class,
            GetDeclaredClassesProcessor.class,
            GetDeclaredFieldsProcessor.class,
            GetDeclaredMethodsProcessor.class,
            GetDeclaredConstructorsProcessor.class,
            GetDeclaredMethodProcessor.class,
            GetDeclaredConstructorProcessor.class,
            GetClassLoaderProcessor.class,
            ForNameProcessor.class,
            GetTypeProcessor.class,
            GetParentProcessor.class,
            GetSystemClassLoaderProcessor.class,
            ForClassProcessor.class,
            GetContextClassLoaderProcessor.class,
            GetProxyClassProcessor.class,
            NewProxyInstanceProcessor.class,
            GetInvocationHandlerProcessor.class
        };
        for (Class klass : processorClasses) {
            try {
                processors.add((ClassProcessor) klass.newInstance());
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.printErr(ex.getMessage());
            }
        }
        return processors;
    }

    private static void copyPatchClasses(ArrayList<ClassProcessor> processors, ArrayList<String> classFilePaths) throws Exception {
        for (String classFilePath : classFilePaths) {
            String className = getClassName(inputFolder, new File(classFilePath));
            for (ClassProcessor processor : processors) {
                if (processor.isPatchClass(className)) {
                    Logger.println("Main", "Copying patch class " + className);
                    classManager.writeClassFile(className);
                }
            }
        }
    }

    private static void processClasses(ArrayList<ClassProcessor> processors, ArrayList<String> classFilePaths) throws Exception {
        for (String classFilePath : classFilePaths) {
            String className = getClassName(inputFolder, new File(classFilePath));

            boolean isPatchClass = false;
            for (ClassProcessor processor : processors) {
                if (processor.isPatchClass(className)) {
                    isPatchClass = true;
                }
            }

            if (!isPatchClass) {
                for (ClassProcessor processor : processors) {
                    processor.instrumentMethods(className);
                }
                classManager.writeClassFile(className);
            }

        }
    }

    private static void parseArgs(String... args) {
        if (args.length != 2) {
            System.err.println("Error: Two arguments required.");
            System.exit(-1);
        } else {
            inputFolder = new File(args[0]);
            outputFolder = new File(args[1]);
        }
    }

    private static ArrayList<String> getClassFilePaths(File searchDir) {
        return getClassFilePaths(searchDir, searchDir);
    }

    private static ArrayList<String> getClassFilePaths(File baseDir, File searchDir) {
        ArrayList<String> fileList = new ArrayList();

        if (searchDir.isDirectory()) {
            File[] files = searchDir.listFiles();
            for (File file : files) {
                fileList.addAll(getClassFilePaths(baseDir, file));
            }
        } else {
            if (searchDir.getAbsolutePath().endsWith(".class")) {
                fileList.add(searchDir.getAbsolutePath());
            }
        }
        return fileList;
    }

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

    private static String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = Calendar.getInstance().getTime();
        return dateFormat.format(date);
    }
}
