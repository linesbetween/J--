//Copyright 2018 Faye

package jminusminus;

import static jminusminus.CLConstants.*;
import java.util.ArrayList;

/**
 * The AST node for an switch block statement group.
 */

class JSwitchBlockStatementGroup extends JStatement {

    /** Switch Labels. */
    private ArrayList<JExpression> switchLabels;

    /** Then clause. */
    private ArrayList<JStatement> blockStatements;;

  

    /**
     * Constructs an AST node for an if-statement given its line number, the 
     * test expression, the consequent, and the alternate.
     * 
     * @param line
     *            line in which the if-statement occurs in the source file.
     * @param switchLabels
     *            switch labels.
     * @param blockStatements
     *            block statements
     */

    public JSwitchBlockStatementGroup(int line, ArrayList<JExpression> switchLabels,
			    ArrayList<JStatement> blockStatements) {
        super(line);
        this.switchLabels = switchLabels;
        this.blockStatements= blockStatements;
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
        p.printf("<JSwitchBlockStatementGroup line=\"%d\">\n", line());
        p.indentRight();
        p.printf("<SwitchLabels>\n");
        p.indentRight();
	for (JExpression label : switchLabels){
	    label.writeToStdOut(p);
	}
	// switchLabels.writeToStdOut(p);
        p.indentLeft();
        p.printf("</SwitchLabels>\n");
        p.printf("<BlockStatements>\n");
        p.indentRight();
	for (JStatement state : blockStatements){
	    state.writeToStdOut(p);
	}
	//blockStatements.wrtieToStdOut(p);
        p.indentLeft();
        p.printf("</BlockStatements>\n");
        p.printf("</JSwitchBlockStatementGroup>\n");
    }

}
