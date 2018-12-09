// Copyright 2013 Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import static jminusminus.CLConstants.*;

/**
 * This abstract base class is the AST node for a binary expression. 
 * A binary expression has an operator and two operands: a lhs and a rhs.
 */

abstract class JBinaryExpression extends JExpression {

    /** The binary operator. */
    protected String operator;

    /** The lhs operand. */
    protected JExpression lhs;

    /** The rhs operand. */
    protected JExpression rhs;

    /**
     * Constructs an AST node for a binary expression given its line number, the
     * binary operator, and lhs and rhs operands.
     * 
     * @param line
     *            line in which the binary expression occurs in the source file.
     * @param operator
     *            the binary operator.
     * @param lhs
     *            the lhs operand.
     * @param rhs
     *            the rhs operand.
     */

    protected JBinaryExpression(int line, String operator, JExpression lhs,
            JExpression rhs) {
        super(line);
        this.operator = operator;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    /**
     * {@inheritDoc}
     */

    public void writeToStdOut(PrettyPrinter p) {
        p.printf("<JBinaryExpression line=\"%d\" type=\"%s\" "
                + "operator=\"%s\">\n", line(), ((type == null) ? "" : type
                .toString()), Util.escapeSpecialXMLChars(operator));
        p.indentRight();
        p.printf("<Lhs>\n");
        p.indentRight();
        lhs.writeToStdOut(p);
        p.indentLeft();
        p.printf("</Lhs>\n");
        p.printf("<Rhs>\n");
        p.indentRight();
        rhs.writeToStdOut(p);
        p.indentLeft();
        p.printf("</Rhs>\n");
        p.indentLeft();
        p.printf("</JBinaryExpression>\n");
    }

}

/**
 * The AST node for a plus (+) expression. In j--, as in Java, + is overloaded
 * to denote addition for numbers and concatenation for Strings.
 */

class JPlusOp extends JBinaryExpression {

    /**
     * Constructs an AST node for an addition expression given its line number,
     * and the lhs and rhs operands.
     * 
     * @param line
     *            line in which the addition expression occurs in the source
     *            file.
     * @param lhs
     *            the lhs operand.
     * @param rhs
     *            the rhs operand.
     */

    public JPlusOp(int line, JExpression lhs, JExpression rhs) {
        super(line, "+", lhs, rhs);
    }

    /**
     * Analysis involves first analyzing the operands. If this is a string
     * concatenation, we rewrite the subtree to make that explicit (and analyze
     * that). Otherwise we check the types of the addition operands and compute
     * the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    public JExpression analyze(Context context) {
        lhs = (JExpression) lhs.analyze(context);
        rhs = (JExpression) rhs.analyze(context);
        if (lhs.type() == Type.STRING || rhs.type() == Type.STRING) {
            return (new JStringConcatenationOp(line, lhs, rhs))
                    .analyze(context);
        } else if (lhs.type() == Type.INT && rhs.type() == Type.INT) {
            type = Type.INT;
        } else if (lhs.type() == Type.LONG && rhs.type() == Type.LONG) {
            type = Type.LONG;
        } else if (lhs.type() == Type.DOUBLE && rhs.type() == Type.DOUBLE) {
            type = Type.DOUBLE;
        }
	else {
            type = Type.ANY;
            JAST.compilationUnit.reportSemanticError(line(),
                    "Invalid operand types for +");
        }
        return this;
    }

    /**
     * Any string concatenation has been rewritten as a 
     * {@link JStringConcatenationOp} (in {@code analyze}), so code generation 
     * here involves simply generating code for loading the operands onto the 
     * stack and then generating the appropriate add instruction.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */

    public void codegen(CLEmitter output) {
	 lhs.codegen(output);
            rhs.codegen(output);
        if (type == Type.INT) {
           
            output.addNoArgInstruction(IADD);
        }
	else if (type == Type.LONG) {
           
            output.addNoArgInstruction(LADD);
        }
	else if (type == Type.DOUBLE) {
          
            output.addNoArgInstruction(DADD);
        }
    }

}

/**
 * The AST node for a subtraction (-) expression.
 */

class JSubtractOp extends JBinaryExpression {

    /**
     * Constructs an AST node for a subtraction expression given its line number,
     * and the lhs and rhs operands.
     * 
     * @param line
     *            line in which the subtraction expression occurs in the source
     *            file.
     * @param lhs
     *            the lhs operand.
     * @param rhs
     *            the rhs operand.
     */

    public JSubtractOp(int line, JExpression lhs, JExpression rhs) {
        super(line, "-", lhs, rhs);
    }

    /**
     * Analyzing the - operation involves analyzing its operands, checking
     * types, and determining the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    public JExpression analyze(Context context) {
        lhs = (JExpression) lhs.analyze(context);
        rhs = (JExpression) rhs.analyze(context);
        lhs.type().mustMatchExpected(line(), Type.INT);
        rhs.type().mustMatchExpected(line(), Type.INT);
        type = Type.INT;
        return this;
    }

    /**
     * Generating code for the - operation involves generating code for the two
     * operands, and then the subtraction instruction.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */

    public void codegen(CLEmitter output) {
        lhs.codegen(output);
        rhs.codegen(output);
        output.addNoArgInstruction(ISUB);
    }

}

/**
 * The AST node for a multiplication (*) expression.
 */

class JMultiplyOp extends JBinaryExpression {

    /**
     * Constructs an AST for a multiplication expression given its line number,
     * and the lhs and rhs operands.
     * 
     * @param line
     *            line in which the multiplication expression occurs in the
     *            source file.
     * @param lhs
     *            the lhs operand.
     * @param rhs
     *            the rhs operand.
     */

    public JMultiplyOp(int line, JExpression lhs, JExpression rhs) {
        super(line, "*", lhs, rhs);
    }

    /**
     * Analyzing the * operation involves analyzing its operands, checking
     * types, and determining the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    public JExpression analyze(Context context) {
        lhs = (JExpression) lhs.analyze(context);
        rhs = (JExpression) rhs.analyze(context);
	Type[] types = new Type [] {Type.INT,Type.LONG,Type.DOUBLE};
        lhs.type().mustMatchOneOf(line(), types);
        rhs.type().mustMatchOneOf(line(), types);
	if(lhs.type()==rhs.type())
	    {        type = lhs.type(); }//Type.INT;}
		else type = Type.INT;
        return this;
    }

    /**
     * Generating code for the * operation involves generating code for the two
     * operands, and then the multiplication instruction.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */

    public void codegen(CLEmitter output) {
        lhs.codegen(output);
        rhs.codegen(output);
	int typeMUL = IMUL;
	if (type == Type.INT)
	    typeMUL = IMUL;
	else if (type == Type.LONG)
	    typeMUL = LMUL;
	else if (type == Type.DOUBLE)
	    typeMUL = DMUL;
        output.addNoArgInstruction(typeMUL);
	    
    }

}

 /**
     * Constructs an AST for a devision  expression given its line number,
     * and the lhs and rhs operands.
     * 
     * @param line
     *            line in which the devision  expression occurs in the
     *            source file.
     * @param lhs
     *            the lhs operand.
     * @param rhs
     *            the rhs operand.
     */

class JDivideOp extends JBinaryExpression {
    public JDivideOp(int line, JExpression lhs, JExpression rhs) {
	super(line, "/", lhs, rhs);
    }

     /**
     * Analyzing the / operation involves analyzing its operands, checking
     * types, and determining the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    public JExpression analyze(Context context){
	lhs = (JExpression) lhs.analyze(context);
	rhs = (JExpression) rhs.analyze(context);
	lhs.type().mustMatchExpected(line(), Type.INT);
	rhs.type().mustMatchExpected(line(), Type.INT);
	type = Type.INT;
	return this;
    }

     /**
     * Generating code for the / operation involves generating code for the two
     * operands, and then the devision  instruction.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */
    public void codegen(CLEmitter output){
	lhs.codegen(output);
	rhs.codegen(output);
	output.addNoArgInstruction(IDIV);
    }
}


 /**
     * Constructs an AST for a remainder  expression given its line number,
     * and the lhs and rhs operands.
     * 
     * @param line
     *            line in which the remainder  expression occurs in the
     *            source file.
     * @param lhs
     *            the lhs operand.
     * @param rhs
     *            the rhs operand.
     */

class JRemainderOp extends JBinaryExpression {
    public JRemainderOp(int line, JExpression lhs, JExpression rhs) {
	super(line, "%", lhs, rhs);
    }

     /**
     * Analyzing the % operation involves analyzing its operands, checking
     * types, and determining the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */
    public JExpression analyze(Context context){
	lhs = (JExpression) lhs.analyze(context);
	rhs = (JExpression) rhs.analyze(context);
	lhs.type().mustMatchExpected(line(), Type.INT);
	rhs.type().mustMatchExpected(line(), Type.INT);
	type = Type.INT;
	return this;
    }

    /**
     * Generating code for the % operation involves generating code for the two
     * operands, and then the remainder  instruction.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */
    public void codegen(CLEmitter output){
	lhs.codegen(output);
	rhs.codegen(output);
	output.addNoArgInstruction(IREM);
    }
}

 /**
     * Constructs an AST for a Arithmetical Left Shift expression given its line number,
     * and the lhs and rhs operands.
     * 
     * @param line
     *            line in which the ALS  expression occurs in the
     *            source file.
     * @param lhs
     *            the lhs operand.
     * @param rhs
     *            the rhs operand.
     */
class JArithLeftShiftOp extends JBinaryExpression {
    public JArithLeftShiftOp(int line, JExpression lhs, JExpression rhs) {
	super(line, "<<", lhs, rhs);
    }

     /**
     * Analyzing the "<<" operation involves analyzing its operands, checking
     * types, and determining the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    public JExpression analyze(Context context){
	lhs = (JExpression) lhs.analyze(context);
	rhs = (JExpression) rhs.analyze(context);
	lhs.type().mustMatchExpected(line(), Type.INT);
	rhs.type().mustMatchExpected(line(), Type.INT);
	type = Type.INT;
	return this;
    }

    /**
     * Generating code for the "<<" operation involves generating code for the two
     * operands, and then the ASL instruction.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */


    public void codegen(CLEmitter output){
	lhs.codegen(output);
	rhs.codegen(output);
	output.addNoArgInstruction(ISHL);
    }
}

/**
     * Constructs an AST for a Arithmetical Right Shift  expression given its line number,
     * and the lhs and rhs operands.
     * 
     * @param line
     *            line in which the ARS expression occurs in the
     *            source file.
     * @param lhs
     *            the lhs operand.
     * @param rhs
     *            the rhs operand.
     */
class JArithRightShiftOp extends JBinaryExpression {
    public JArithRightShiftOp(int line, JExpression lhs, JExpression rhs) {
	super(line, ">>", lhs, rhs);
    }

     /**
     * Analyzing the >> operation involves analyzing its operands, checking
     * types, and determining the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */
    public JExpression analyze(Context context){
	lhs = (JExpression) lhs.analyze(context);
	rhs = (JExpression) rhs.analyze(context);
	lhs.type().mustMatchExpected(line(), Type.INT);
	rhs.type().mustMatchExpected(line(), Type.INT);
	type = Type.INT;
	return this;
    }

    /**
     * Generating code for the >> operation involves generating code for the two
     * operands, and then the ARS instruction.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */

    public void codegen(CLEmitter output){
	lhs.codegen(output);
	rhs.codegen(output);
	output.addNoArgInstruction(ISHR);
    }
}

 /**
     * Constructs an AST for a Logical Rigth Shift expression given its line number,
     * and the lhs and rhs operands.
     * 
     * @param line
     *            line in which the LRS  expression occurs in the
     *            source file.
     * @param lhs
     *            the lhs operand.
     * @param rhs
     *            the rhs operand.
     */

class JLogicRightShiftOp extends JBinaryExpression {
    public JLogicRightShiftOp(int line, JExpression lhs, JExpression rhs) {
	super(line, ">>>", lhs, rhs);
    }

    /**
     * Analyzing the >>> operation involves analyzing its operands, checking
     * types, and determining the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    /**
     * Generating code for the >>> operation involves generating code for the two
     * operands, and then the LRS  instruction.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */
    public JExpression analyze(Context context){
	lhs = (JExpression) lhs.analyze(context);
	rhs = (JExpression) rhs.analyze(context);
	lhs.type().mustMatchExpected(line(), Type.INT);
	rhs.type().mustMatchExpected(line(), Type.INT);
	type = Type.INT;
	return this;
    }

    public void codegen(CLEmitter output){
	lhs.codegen(output);
	rhs.codegen(output);
	output.addNoArgInstruction(IUSHR);
    }
}
 /**
     * Constructs an AST for a Bitwise Or  expression given its line number,
     * and the lhs and rhs operands.
     * 
     * @param line
     *            line in which the Bitwise Or expression occurs in the
     *            source file.
     * @param lhs
     *            the lhs operand.
     * @param rhs
     *            the rhs operand.
     */
class JBitOrOp extends JBinaryExpression {
    public JBitOrOp(int line, JExpression lhs, JExpression rhs) {
	super(line, "|", lhs, rhs);
    }

    
    /**
     * Analyzing the | operation involves analyzing its operands, checking
     * types, and determining the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */    
    public JExpression analyze(Context context){
	lhs = (JExpression) lhs.analyze(context);
	rhs = (JExpression) rhs.analyze(context);
	lhs.type().mustMatchExpected(line(), Type.INT);
	rhs.type().mustMatchExpected(line(), Type.INT);
	type = Type.INT;
	return this;
    }

    /**
     * Generating code for the | operation involves generating code for the two
     * operands, and then the Bitwse Or  instruction.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */
    public void codegen(CLEmitter output){
	lhs.codegen(output);
	rhs.codegen(output);
	output.addNoArgInstruction(IOR);
    }
}

/**
     * Constructs an AST for a Bitwise XOR expression given its line number,
     * and the lhs and rhs operands.
     * 
     * @param line
     *            line in which the Bitwise XOR expression occurs in the
     *            source file.
     * @param lhs
     *            the lhs operand.
     * @param rhs
     *            the rhs operand.
     */

class JBitXOrOp extends JBinaryExpression {
    public JBitXOrOp(int line, JExpression lhs, JExpression rhs) {
	super(line, "^", lhs, rhs);
    }

     /**
     * Analyzing the ^ operation involves analyzing its operands, checking
     * types, and determining the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */
    public JExpression analyze(Context context){
	lhs = (JExpression) lhs.analyze(context);
	rhs = (JExpression) rhs.analyze(context);
	lhs.type().mustMatchExpected(line(), Type.INT);
	rhs.type().mustMatchExpected(line(), Type.INT);
	type = Type.INT;
	return this;
    }

    /**
     * Generating code for the ^ operation involves generating code for the two
     * operands, and then the Bitwise XOR instruction.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */
    public void codegen(CLEmitter output){
	lhs.codegen(output);
	rhs.codegen(output);
	output.addNoArgInstruction(IXOR);
    }
}

/**
     * Constructs an AST for a Bitwise And expression given its line number,
     * and the lhs and rhs operands.
     * 
     * @param line
     *            line in which the devision  expression occurs in the
     *            source file.
     * @param lhs
     *            the lhs operand.
     * @param rhs
     *            the rhs operand.
     */

class JBitAndOp extends JBinaryExpression {
    public JBitAndOp(int line, JExpression lhs, JExpression rhs) {
	super(line, "&", lhs, rhs);
    }

     /**
     * Analyzing the & operation involves analyzing its operands, checking
     * types, and determining the result type.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    public JExpression analyze(Context context){
	lhs = (JExpression) lhs.analyze(context);
	rhs = (JExpression) rhs.analyze(context);
	lhs.type().mustMatchExpected(line(), Type.INT);
	rhs.type().mustMatchExpected(line(), Type.INT);
	type = Type.INT;
	return this;
    }

    /**
     * Generating code for the & operation involves generating code for the two
     * operands, and then the Bitwise And instruction.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */

    public void codegen(CLEmitter output){
	lhs.codegen(output);
	rhs.codegen(output);
	output.addNoArgInstruction(IAND);
    }
}

