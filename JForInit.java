// Copyright 2013 Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import java.util.ArrayList;

/**
 * The AST node for a forloop forInit. 
 */

class JForInit extends JStatement {

    /** List of JStatement . */
    private ArrayList<JStatement> statements;

    /**
     * Constructs an AST node for a forInit  given the line number,
     * and a list of JStatement
     * 
     * @param line
     *            line in which the variable declaration occurs in the source
     *            file.
     * @param statements
     *            statements.
     */

    public JForInit(int line, ArrayList<JStatement> statements) {
        super(line);
        this.statements = statements;
    }

    /**
     * Returns the list of statements.
     * 
     * @return list of statements.
     */

    public ArrayList<JStatement> statements() {
        return statements;
    }

    /**
     * Declares the variable(s). Initializations are rewritten as assignment
     * statements.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    public JStatement analyze(Context context) {
	
	for (JStatement state : statements){
	    state = (JStatement)state.analyze(context);
	}
	
        return this;
    }

    /**
     * Local variable initializations (rewritten as assignments in 
     * {@code analyze}) are generated here.
     * 
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */

    public void codegen(CLEmitter output) {
	if (statements != null ){
	    for(JStatement state : statements){
		state.codegen(output);
	    }
	}
    }

    /**
     * {@inheritDoc}
     */

    public void writeToStdOut(PrettyPrinter p) {
        p.println("<JForInit>");
        p.indentRight();
        if (statements != null) {
            p.println("<StatementExpressions>");
            p.indentRight();
            for (JStatement astatement : statements) {
                p.printf("<statement=\"%s\"/>\n", astatement);
            }
            p.indentLeft();
            p.println("</StatementExpressions>");
        }
        p.indentLeft();
        p.println("</JForInit>");
    }

}
