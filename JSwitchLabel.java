//Copyright 2018 Faye

package jminusminus;

import static jminusminus.CLConstants.*;

/**
 * The AST node for an switch-labels.
 */

class JSwitchLabel extends JExpression {

    /** expression to be excuted. */
    private JExpression expr;


    /**
     * Constructs an AST node for an switchlabel given its line number, the 
     * test expression, the consequent, and the alternate.
     * 
     * @param line
     *            line in which the if-statement occurs in the source file.
     * @param test
     *            test expression.
     * @param switchBlock
     *            then switchBlockStatementGroup
     */

    public JSwitchLabel(int line, JExpression expr) {
        super(line);
        this.expr = expr;;
    }

    /**
     * Analyzing the if-statement means analyzing its components and checking
     * that the test is a boolean.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    public JExpression analyze(Context context) {
     
        return this;
    }

    /**
     * Code generation for an if-statement. We generate code to branch over the
     * consequent if !test; the consequent is followed by an unconditonal branch
     * over (any) alternate.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */

    public void codegen(CLEmitter output) {
     
    }

    /**
     * {@inheritDoc}
     */

    public void writeToStdOut(PrettyPrinter p) {
        p.printf("<JSwitchLabels line=\"%d\">\n", line());
        p.indentRight();
        p.printf("<Expression>\n");
	if (expr != null){
        p.indentRight();
	expr.writeToStdOut(p);
        p.indentLeft();
	}
        p.printf("</Expression>\n");
	p.indentLeft();
        p.printf("</JSwitchLabels>\n");
    }

}


//not necessary
class JSwitchLabelDefault extends JExpression {

   

    /**
     * Constructs an AST node for an switchlabel given its line number, the 
     * test expression, the consequent, and the alternate.
     * 
     * @param line
     *            line in which the if-statement occurs in the source file.
     * @param test
     *            test expression.
     * @param switchBlock
     *            then switchBlockStatementGroup
     */

    public JSwitchLabelDefault(int line) {
        super(line);
    }

    /**
     * Analyzing the if-statement means analyzing its components and checking
     * that the test is a boolean.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    public JExpression analyze(Context context) {
     
        return this;
    }

    /**
     * Code generation for an if-statement. We generate code to branch over the
     * consequent if !test; the consequent is followed by an unconditonal branch
     * over (any) alternate.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */

    public void codegen(CLEmitter output) {
     
    }

    /**
     * {@inheritDoc}
     */

    public void writeToStdOut(PrettyPrinter p) {
        p.printf("<JSwitchLabels line=\"%d\">\n", line());
       
        p.printf("</JSwitchLabels>\n");
    }

}


