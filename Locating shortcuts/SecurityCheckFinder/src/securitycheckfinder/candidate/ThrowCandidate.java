package securitycheckfinder.candidate;

import soot.SootMethodRef;

public class ThrowCandidate extends Candidate {

    private final String srcClass;
    private final SootMethodRef srcMethod;
    private final String destClass;

    public String getDestClass() {
        return destClass;
    }

    @Override
    public void printXML() {
        String output = "<throw>";
        output += "<srcClass>" + srcClass + "</srcClass>";
        output += getXMLMethodDescription(srcMethod, "src");
        output += "<destClass>" + destClass + "</destClass>";
        output += "</throw>";
        System.out.println(output);
    }

    public ThrowCandidate(String srcClass, SootMethodRef srcMethod, String destClass) {
        this.srcClass = srcClass;
        this.srcMethod = srcMethod;
        this.destClass = destClass;
    }

    @Override
    public void printRaw() {
        String output = "throw;";
        output += srcClass + ";";
        output += srcMethod.name() + ";";
        output += srcMethod.getSignature() + ";";
        output += destClass + ";";
        System.out.println(output);
    }
}
