package securitycheckfinder.candidate;

import soot.SootMethodRef;

public abstract class Candidate {

    public abstract void printXML();

    public abstract void printRaw();

    /**
     * Sanitizes a class name such that it can be used in XML.
     *
     * In particular, this concerns constructors (init) and static initializers
     * (clinit)
     *
     * @param method Method whose name is to be sanitized.
     * @return Sanitized name of method.
     */
    protected String getSanitizedName(SootMethodRef method) {
        String name = method.name();
        name = name.replace("<", "$");
        name = name.replace(">", "");
        return name;
    }

    /**
     * Constructs an XML string that contains a method's name and parameters.
     *
     * @param method Method whose XML string representation is to be
     * constructed.
     * @param prefix Prefix to be used in XML tags (e.g., src, dest).
     * @return
     */
    protected String getXMLMethodDescription(SootMethodRef method, String prefix) {
        String xml = "<" + prefix + "MethodName>" + getSanitizedName(method) + "</" + prefix + "MethodName>";
        xml += "<" + prefix + "MethodParams>(";
        for (int i = 0; i < method.parameterTypes().size(); i++) {
            if (i != 0) {
                xml += ",";
            }
            xml += method.parameterType(i);
        }

        xml += ")</" + prefix + "MethodParams>";
        return xml;
    }
}
