/* Copyright (c) 2007-2014 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package awedoctime;

import static awedoctime.Document.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import awedoctime.Document.ConversionException;

public class Main {
    
    /**
     * Use the Awesome Document Time system to create a document.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        // Construct a document with a few sentences from _Structure_and_
        // _Interpretation_of_Computer_Programs_ by Abelson, Sussman, & Sussman
        Document sicp = empty();
        sicp = sicp.append(
                paragraph("\"I think that it's extraordinarily important that we in "
                        + "computer science keep fun in computing.\" --Alan J. Perlis"));
        sicp = sicp.append(
                section("Chapter 1: Building Abstractions",
                paragraph("We are about to study the idea of a computational process.")));
        
        // Write LaTeX source into awesome-doc-sicp.tex
        try {
            PrintWriter writer = new PrintWriter("awesome-doc-sicp.tex");
            writer.print(sicp.toLaTeX());
            writer.close();
        } catch (FileNotFoundException fnfe) {
            System.err.println("Could not create file");
            fnfe.printStackTrace();
        } catch (ConversionException ce) {
            System.err.println("Could not convert document");
            ce.printStackTrace();
        }
    }
}
