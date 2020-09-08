import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class Django22Output extends CodeOutput {
    // General description
    // Model compiler
    // This is a helper class to manage indentation in generated code
    // Invariants
    // none
    // Implementation notes
    // All static methods to get global visibility
    // flag is a simple way to enable indentation markers in the generated code
    // Application notes
    // Outer rule needs to establish indent level for any inner rules that need it
    // Inner rule should never Indent.more() before emitting anything
    // Outer rule needs to Indent.more() and Indent.less() in same rule
    //
    // This material is copied and/or adapted from the (to be)
    // How to Engineer Software website
    //
    // http://www.Construx.com/howtoengineersw
    //
    // This material is Copyright � 2018 by Stephen R. Tockey
    // Permission is hereby given to copy, adapt, and distribute this material as
    // long as this notice is included on all such materials and the materials are
    // not sold, licensed, or otherwise distributed for commercial gain.
    //
    // This tool is being distributed "as-is". No warrantee is expressed or implied.
    // While the author believes that this tool gives correct answers, the user
    // assumes all risk of use.

    // Constants

    private static final int indentStep = 4;
    private static final boolean showMarkerFlag = false;

    // Static (class) variables

    private static OutputStream outputJALFileStream;
    private static PrintStream outputJALPrintStream;
    private static int indentLevel;
    private static boolean outputAnythingOnLine;
    private static int errorCount;

    // Static (class) methods

    public boolean openJALOutputFile(String outputJALFileName) {
        // description
        // tries to open an output file for write
        // requires
        // outputModelFileName <> null, with directory path name and extension already
        // on
        // guarantees
        // returns true only if the output file was successfully opened
        boolean success = true;
        try {
            outputJALFileStream = new FileOutputStream(outputJALFileName);
            outputJALPrintStream = new PrintStream(outputJALFileStream);
        } catch (Exception ex) {
            System.out.println(" Can't write model output file = " + outputJALFileName);
            success = false;
        }
        outputAnythingOnLine = false;
        return success;
    }

    public void print(String line) {
        // description
        // outputs line (without <cr><lf>) to the output stream
        // requires
        // none
        // guarantees
        // line has been written as-is (no <cr><lf>) to the output stream
        if (!outputAnythingOnLine) {
            this.outputActualIndentation();
        }
        outputJALPrintStream.print(line);
        outputAnythingOnLine = true;
    }

    public void println(String line) {
        // description
        // outputs line (with <cr><lf>) to the output stream
        // requires
        // none
        // guarantees
        // line has been written with <cr><lf> to the output stream
        if (!outputAnythingOnLine) {
            this.outputActualIndentation();
        }
        outputJALPrintStream.println(line);
        outputAnythingOnLine = false;
    }

    public void indentNone() {
        // description
        // resets to indentation level zero (none)
        // requires
        // none
        // guarantees
        // indentation level has been set to zero
        indentLevel = 0;
    }

    public void indentMore() {
        // description
        // sets indentation level to one step deeper
        // requires
        // none
        // guarantees
        // indentation level has been increased by one step
        indentLevel += indentStep;
    }

    public void indentLess() {
        // description
        // sets indentation level to one step shallower
        // requires
        // none
        // guarantees
        // indentation level has been decreased by one step
        indentLevel -= indentStep;
    }

    public void indent() {
        // description
        // indents to the proper level in the output
        // note: flags the indentation level with "|" when flag = true
        // requires
        // none
        // guarantees
        // proper number of indentation spaces have been put into the output
        // *** IT USED TO, NOT ANYMORE. NOW IT DOES NOTHING ***
        // for( int steps = 1; steps <= indentLevel; steps++ ) {
        // System.out.print( " " );
        // }
        // if( showMarkerFlag ) {
        // System.out.print( "|" );
        // }

        // NOTE: DOESN'T ACTUALLY DO ANYTHING ANYMORE, TO ALLOW FOR DYNAMIC BACKING-UP
        // ON INDENTATION
        // I'M KEEPING IT FOR THE TIME BEING JUST TO MAINTAIN COMPATIBILITY WITH MODEL
        // EDITOR AND COMPILER LEGACY

    }

    public void outputActualIndentation() {
        // description
        // indents to the proper level in the output
        // note: flags the indentation level with "|" when flag = true
        // requires
        // none
        // guarantees
        // proper number of indentation spaces have been put into the output
        for (int steps = 1; steps <= indentLevel; steps++) {
            outputJALPrintStream.print(" ");
        }
        if (showMarkerFlag) {
            outputJALPrintStream.print("|");
        }
    }

    public boolean closeJALOutputFile() {
        // description
        // tries to close an output file after writing
        // requires
        // the file/print stream is still open (i.e. it hasn't already been closed)
        // guarantees
        // returns true only if the output file was closed
        outputJALPrintStream.flush();
        outputJALPrintStream.close();
        return true;
    }

    public void clearErrorCount() {
        // description
        // clears the error count before compilation
        // requires
        // none
        // guarantees
        // errorCount has been set to zero
        errorCount = 0;
    }

    public void incrementErrorCount() {
        // description
        // adds one to the compilation error count
        // requires
        // none
        // guarantees
        // errorCount has been incremented
        errorCount++;
    }

    public int errorCount() {
        // description
        // returns the error count after compilation
        // requires
        // none
        // guarantees
        // returns the number of compilation errors found during compilation
        return errorCount;
    }

    // Instance variables

    // none

    // Constructor(s)

    // none

    // Accessors

    // none

    // Modifiers

    // none

    // Private methods

    // none

}
