package securitycheckfinder.candidate;

import soot.SootMethodRef;

public class CallCandidate extends Candidate {

    private final String srcClass;
    private final SootMethodRef srcMethod;
    private final String destClass;
    private final SootMethodRef destMethod;

    public CallCandidate(String srcClass, SootMethodRef srcMethod, String destClass, SootMethodRef destMethod) {
        this.srcClass = srcClass;
        this.srcMethod = srcMethod;
        this.destClass = destClass;
        this.destMethod = destMethod;
    }

    public String getSrcClass() {
        return srcClass;
    }

    public SootMethodRef getSrcMethod() {
        return srcMethod;
    }

    public String getDestClass() {
        return destClass;
    }

    public SootMethodRef getDestMethod() {
        return destMethod;
    }

    @Override
    public void printRaw() {
        String output = "call;";
        output += srcClass + ";";
        output += srcMethod.name() + ";";
        output += srcMethod.getSignature() + ";";
        output += destClass + ";";
        output += destMethod.name() + ";";
        output += destMethod.getSignature() + ";";
        System.out.println(output);
    }

    @Override
    public void printXML() {
        String output = "<call>";
        output += "<srcClass>" + srcClass + "</srcClass>";
        output += getXMLMethodDescription(srcMethod, "src");
        output += "<destClass>" + destClass + "</destClass>";
        output += getXMLMethodDescription(destMethod, "dest");
        output += "</call>";
        System.out.println(output);
    }
}
