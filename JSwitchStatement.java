//Copyright 2018 Faye

package jminusminus;

import static jminusminus.CLConstants.*;
import java.util.ArrayList;

/**
 * The AST node for an switch-statement.
 */

class JSwitchStatement extends JStatement {

    /** Test expression. */
    private JExpression test;

    /** Then clause. */
    private ArrayList<JStatement> switchBlock;

    /**
     * Constructs an AST node for an if-statement given its line number, the 
     * test expression, the consequent, and the alternate.
     * 
     * @param line
     *            line in which the if-statement occurs in the source file.
     * @param test
     *            test expression.
     * @param switchBlock
     *            then switchBlockStatementGroup
     */

    public JSwitchStatement(int line, JExpression test, ArrayList<JStatement> switchblock) {
        super(line);
        this.test = test;
        this.switchBlock = switchblock;
    }

    /**
     * Analyzing the if-statement means analyzing its components and checking
     * that the test is a boolean.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    public JStatement analyze(Context context) {
     
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
        p.printf("<JSwitchStatement line=\"%d\">\n", line());
        p.indentRight();
        p.printf("<CaseExpression>\n");
        p.indentRight();
	// test.writeToStdOut(p);
        p.indentLeft();
        p.printf("</TestExpression>\n");
        p.printf("<SwitchBlock>\n");
        p.indentRight();
        //switchBlock.writeToStdOut(p);
        p.indentLeft();
        p.printf("</SwitchBlock>\n");
        p.printf("</JSwitchStatement>\n");
    }

}
