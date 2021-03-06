package jminusminus;

import static jminusminus.CLConstants.*;

/**
 * This abstract base class   is the AST node for a conditional base  expression. 
 * A conditional expression has two  op1 and tow  op2: lhs  and rhs
 */


abstract  class JTernaryExpression  extends JExpression{

    /** operator1  */
    protected String op1;

    /** operator2 */
    protected String op2;

    /**The left hand side operand */
    protected JExpression lhs;

    /**The middle hand side operand */
    protected JExpression mhs;

    /**The righg hand side operand */
    protected JExpression rhs;

     /**
     * Constructs an AST node for a binary expression given its line number, the
     * binary operator, and lhs and rhs operands.
     * 
     * @param line
     *            line in which the binary expression occurs in the source file.
     * @param op1, op2
     *            the conditional  operator.
     * @param lhs
     *            the lhs operand.
     * @param mhs 
     *            the middle hand side oprand
     * @param rhs
     *            the rhs operand.
     */

    
    protected JTernaryExpression(int line, String op1, String op2,
				 JExpression lhs, JExpression mhs,JExpression rhs) {
        super(line);
        this.op1 = op1;
	this.op2 =op2;
        this.lhs = lhs;
	this.mhs = mhs;
        this.rhs = rhs;
    }

     /**
     * {@inheritDoc}
     */

    public void writeToStdOut(PrettyPrinter p) {
        p.printf("<JConditionalBaseExpression line=\"%d\" type=\"%s\" "
                + "operator1=\"%s\">\n" +  "operator1=\"%s\">\n"
		 , line(), ((type == null) ? "" : type.toString()),
		 Util.escapeSpecialXMLChars(op1),  Util.escapeSpecialXMLChars(op2));
        p.indentRight();
        p.printf("<Lhs>\n");
        p.indentRight();
        lhs.writeToStdOut(p);
        p.indentLeft();
        p.printf("</Lhs>\n");

	p.indentRight();
        p.printf("<Mhs>\n");
        p.indentRight();
        mhs.writeToStdOut(p);
        p.indentLeft();
        p.printf("</Mhs>\n");
	
        p.printf("<Rhs>\n");
        p.indentRight();
        rhs.writeToStdOut(p);
        p.indentLeft();
        p.printf("</Rhs>\n");
        p.indentLeft();
        p.printf("</JTernaryExpression>\n");
    }
    
    
}


/**
 * The AST node for a conditional (? :) expression.
 */

class JConditionalOp extends JTernaryExpression {

    /**
     * Constructs an AST node for a conditional  expression given its line number,
     * and the lhs and rhs operands.
     * 
     * @param line
     *            line in which the conditional  expression occurs in the source
     *            file.
     * @param lhs
     *            the lhs operand.
     * @param rhs
     *            the rhs operand.
     */

    public JConditionalOp(int line, JExpression lhs, JExpression mhs, JExpression rhs) {
        super(line, "?", ":",lhs, mhs, rhs);
    }

    /*
     *check lhs (condition) is boolean
     * check mhs and rhs as expression
     * @param context
     * @return analyzed AST subtree
     */
    public JExpression analyze(Context context){
        lhs = (JExpression) lhs.analyze(context);
	lhs.type().mustMatchExpected(line(),Type.BOOLEAN);
	mhs = (JExpression)mhs.analyze(context);
	rhs = (JExpression)rhs.analyze(context);
	mhs.type().mustMatchExpected(line(), rhs.type());
	this.type = mhs.type();
	return this;
    }
    
    public void codegen(CLEmitter output){
		String elseLabel = output.createLabel();
	String endLabel = output.createLabel();
	lhs.codegen(output, elseLabel, false);
	mhs.codegen(output);
	output.addBranchInstruction(GOTO, endLabel);
	output.addLabel(elseLabel);
	rhs.codegen(output);
	output.addLabel(endLabel);
	
    }

}
