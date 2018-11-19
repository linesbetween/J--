// Copyright 2013 Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import static jminusminus.CLConstants.*;

/**

 * @see 
 */

class JTryStatement  extends JStatement {

   

    /** Interface name. */
    private JBlock block;

    /** Interface block. */
    private JFormalParameter fp;

    private JBlock catchBlock;
    /** . */
    private JBlock finalBlock;



    

    /**
     * Constructs an AST node for a class declaration given the line number, list
     * of class modifiers, name of the class, its super class type, and the
     * class block.
     * 
     * @param line
     *            line in which the class declaration occurs in the source file.
     * @param block
     *            try block.
     * @param fp
     *            formal parameter.
     *
     * @param finalBlock
     *            final  block.
     */

    public JTryStatement (int line, JBlock block, JFormalParameter fp,
			  JBlock catchBlock, JBlock finalBlock) {
        super(line);
        this.block = block;
        this.fp = fp;
	this.catchBlock = catchBlock;
        this.finalBlock = finalBlock;
 
    }

   

    public void preAnalyze(Context context) {
       
       
    }

     public JAST analyze(Context context) {
        // Analyze all members
     

        // Copy declared fields for purposes of initialization.
        

        // Finally, ensure that a non-abstract class has
        // no abstract methods.
       
        return this;
    }

    /**
     * Generates code for the class declaration.
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
        p.printf("<JTryBlock line=\"%d\"" , line);
        p.indentRight();
        
        p.indentLeft();
        p.println("</JTryBlock>");
    }


}
