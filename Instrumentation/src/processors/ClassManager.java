package processors;

import java.io.File;
import java.io.IOException;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

/**
 *
 * @author Philipp Holzinger <philipp.holzinger@sit.fraunhofer.de>
 */
public class ClassManager {

    final private File outputFolder;

    public ClassManager(File inputFolder, File outputFolder) throws NotFoundException {
        ClassPool.getDefault().insertClassPath(inputFolder.getAbsolutePath());
        this.outputFolder = outputFolder;
    }

    public void writeClassFile(String className) throws NotFoundException, CannotCompileException, IOException {
        CtClass klass = getClassByName(className);
        klass.writeFile(outputFolder.getAbsolutePath());
    }

    public CtClass getClassByName(String className) throws NotFoundException {
        return ClassPool.getDefault().get(className);
    }
}
