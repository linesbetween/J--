// Copyright 2013 Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import java.util.ArrayList;

/**
 * The AST node for a forStatement. 
 * JForStatement(line, forInitStatement, forExpression,  forUpdateStatement);
 */

class JForStatement extends JStatement {

    /** List of JStatement . */
    private JForInit forInitStatement;
    private JExpression forExpression;
    private ArrayList<JStatement> forUpdateStatement;
    private JStatement statement;
    

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

    public JForStatement(int line, JForInit  forInitStatement,
			 JExpression forExpression,
			 ArrayList<JStatement> forUpdateStatement,
			 JStatement statement) {
        super(line);
        this.forInitStatement = forInitStatement;
	this.forExpression = forExpression;
	this.forUpdateStatement = forUpdateStatement;
	this.statement = statement;
    }

    /**
     * Returns the list of statements.
     * 
     * @return list of statements.
     */
    /*
    public ArrayList<JStatement> statements() {
        return statements;
	}*/

    /**
     * Declares the variable(s). Initializations are rewritten as assignment
     * statements.
     * 
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    public JStatement analyze(Context context) {
       
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
      
    }

    /**
     * {@inheritDoc}
     */

    public void writeToStdOut(PrettyPrinter p) {
        p.println("<JForStatement>");
        p.indentRight();
        if (forInitStatement != null) {
            p.println("<forInitStatement>");
            p.println("</forInitStatement>");
        }
	
	 if (forExpression != null) {
            p.println("<forExpression>");
            p.println("</forExpression>");
        }
	  if (forUpdateStatement != null) {
            p.println("<forUpdateStatement>");
            p.println("</forUpdateStatement>");
	  }
        p.indentLeft();
        p.println("</JForStatement>");
    }

}
