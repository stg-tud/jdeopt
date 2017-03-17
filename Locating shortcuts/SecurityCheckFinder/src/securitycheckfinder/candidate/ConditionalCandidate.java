package securitycheckfinder.candidate;

import soot.SootMethodRef;
import soot.Value;

public class ConditionalCandidate extends Candidate {

    private final String srcClass;
    private final SootMethodRef srcMethod;
    private final Value op1;
    private final Value op2;

    public String getOp1() {
        return op1.getType().toString();
    }

    public String getOp2() {
        return op2.getType().toString();
    }

    @Override
    public void printXML() {
        String output = "<conditional>";
        output += "<srcClass>" + srcClass + "</srcClass>";
        output += getXMLMethodDescription(srcMethod, "src");
        output += "<type1>" + op1.getType().toString() + "</type1>";
        output += "<type2>" + op2.getType().toString() + "</type2>";
        output += "</conditional>";
        System.out.println(output);
    }

    public ConditionalCandidate(String srcClass, SootMethodRef srcMethod, Value op1, Value op2) {
        this.srcClass = srcClass;
        this.srcMethod = srcMethod;
        this.op1 = op1;
        this.op2 = op2;
    }

    @Override
    public void printRaw() {
        String output = "conditional;";
        output += srcClass + ";";
        output += srcMethod.name() + ";";
        output += srcMethod.getSignature() + ";";
        output += op1.getType().toString() + ";";
        output += op2.getType().toString() + ";";
        System.out.println(output);
    }
}
