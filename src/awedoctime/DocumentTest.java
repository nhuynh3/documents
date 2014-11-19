/* Copyright (c) 2007-2014 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package awedoctime;

import static awedoctime.Document.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import awedoctime.Document.ConversionException;

/**
 * Tests for Document.
 * 
 * You SHOULD create additional test classes to unit-test variants of Document.
 * You MAY strengthen the specs of Document and test those specs.
 */
public class DocumentTest {

    // Testing strategy
    //
    // Methods: toString, equals, hashCode, append, bodyWordCount, 
    //          tableOfContents, toLaTeX, toMarkdown
    //    
    // Partitions-
    // Document 0:                                      empty Document
    // Document oneParagraph:                          Document w/ 1 paragraph
    // Document twoParagraph:                          Document w/ 2 paragraph
    // Document oneSectionNoContent:                   Document w/ 1 section
    //
    // Document SectionWithOneParagraph:         Document w/ 1 section  1 nested paragraph
    // Document SectionWithTwoParagraph:         Document w/ 1 section, 2 nested paragraphs 
    // Document SectionWithSectionAndParagraph:  Document w/ 1 section, 1 nested section, 1 nested paragraph
    //
    // Document A on Pset:                              Document w/ only paragraphs
    // Document B on Pset:                              Document w/ sections and nested paragraphs, 
    //                                                      starting with an unnested paragraph
    // Document C on Pset:                              Document w/ nested sections and nested paragraphs
    //
    // Document Complex Create vs Complex Append:       Two abstractly equal documents created in a different manner
    //
    // 
    // Extra feature
    //   Find tests in DocumentToHTMLTest.java

    private static Document documentA;
    private static Document paraA1;
    private static Document paraA2;
    private static Document paraA3;
    private static Document paraA4;

    private static Document documentB;
    private static Document paraB1;
    private static Document sectB1;
    private static Document paraB2;
    private static Document sectB2;
    private static Document paraB3;

    private static Document documentC;
    private static Document paraC1;
    private static Document paraC2;
    private static Document sectC1;
    private static Document sectC2;
    private Document documentD;
    private Document docComplexCreate;


    @Before public void setupDocuments() {

        paraA1 = paragraph("You say tomayto.");
        paraA2 = paragraph("I say tomahto.");
        paraA3 = paragraph("You say potayto.");
        paraA4 = paragraph("I say potahto.");
        documentA = paraA1.append(paraA2).append(paraA3).append(paraA4);

        paraB1 = paragraph("Dr. Seuss");
        paraB2 = paragraph("Two fish.");
        paraB3 = paragraph("Blue fish.");
        sectB1 = section("One fish.", paraB2);
        sectB2 = section("Red fish.", paraB3);
        documentB = paraB1.append(sectB1).append(sectB2);

        paraC1 = paragraph("Francisco, Bernardo, Horatio, & Marcellus.");
        paraC2 = paragraph("King Hamlet!");
        sectC1 = section("Guards", paraC1);
        sectC2 = section("Ghosts", paraC2);
        documentC = section("Scene 1", sectC1.append(sectC2));


        documentD = documentC.append(documentB);

        docComplexCreate = new Section("Fox", 
                new Section("Socks", paragraph("Tweetle bettles"), 
                        new Section("Puddle", 
                                new Paragraph("Bottle", 
                                        new Paragraph("Our Play:", paragraph("Phyramus and Thisby"))))),
                                        new Section("Pyramus", paragraph("Nick Bottom"),
                                                section("Thisby", paragraph("Peter Quince"))));

    }
    //*************************************append Tests*************************************\\
    @Test public void appendTestsA() {

        Document newDocumentA = new Paragraph("You say tomayto.", 
                new Paragraph("I say tomahto.", 
                        new Paragraph("You say potayto.", 
                                new Paragraph("I say potahto."))));
        assertTrue(documentA.equals(newDocumentA));
    }

    @Test public void appendTestsB() {
        Document newDocumentB = new Paragraph("Dr. Seuss", 
                new Section("One fish.", new Paragraph("Two fish."),
                        new Section("Red fish.", new Paragraph("Blue fish."),
                                new Empty())));

        assertTrue(documentB.equals(newDocumentB));
        assertEquals("Document B", newDocumentB.toString(), documentB.toString());

    }

    @Test public void appendTestsC() {
        Document newDocumentC = new Section("Scene 1",
                new Section("Guards", 
                        new Paragraph("Francisco, Bernardo, Horatio, & Marcellus."), 
                        new Section("Ghosts", 
                                new Paragraph("King Hamlet!", new Empty()), new Empty())), new Empty());

        assertTrue(documentC.equals(newDocumentC));

    }

    @Test public void appendTestsD() {
        Document newDocumentD = new Section("Scene 1", new Section("Guards", 

                new Paragraph("Francisco, Bernardo, Horatio, & Marcellus.", new Empty()),

                new Section("Ghosts", new Paragraph("King Hamlet!", new Paragraph("Dr. Seuss", new Empty())), new Empty())), 

                new Section("One fish.", new Paragraph("Two fish.", new Empty()), 

                        new Section ("Red fish.", new Paragraph("Blue fish.", new Empty()), new Empty())));


        assertEquals(newDocumentD.toString(), documentD.toString());
        assertTrue("Document D", documentD.equals(newDocumentD));

    }

    @Test public void appendComplexTestOne() {

        Document complexAppendTop =  new Section("Fox", 
                new Section("Socks", new Paragraph("Tweetle bettles", empty()), 
                        new Section("Puddle", empty(), empty())));

        Document complexAppendMid = 
                new Paragraph("Bottle", 
                        new Paragraph("Our Play:", empty())) ;

        Document complexAppendBottom = new Paragraph("Phyramus and Thisby", 
                new Section("Pyramus", paragraph("Nick Bottom"),
                        section("Thisby", paragraph("Peter Quince"))));
        Document docComplexAppendOne = complexAppendTop.append(complexAppendMid).append(complexAppendBottom );

        assertEquals(docComplexCreate.toString(), docComplexAppendOne.toString());
        assertEquals(docComplexCreate, docComplexAppendOne);
    }

    @Test public void appendComplexTestTwo() {

        Document complexAppendTop =  new Section("Fox", 
                new Section("Socks", new Paragraph("Tweetle bettles", empty()), 
                        new Section("Puddle", empty(), empty())));

        Document complexAppendMid1 = paragraph("Bottle") ;


        Document complexAppendMid2 = new Paragraph("Our Play:", empty()) ;

        Document complexAppendBottom = new Paragraph("Phyramus and Thisby", 
                new Section("Pyramus", paragraph("Nick Bottom"),
                        section("Thisby", paragraph("Peter Quince"))));

        Document docComplexAppendTwo = complexAppendTop.append(complexAppendMid1).append((complexAppendMid2).append(complexAppendBottom ));

        assertEquals(docComplexCreate.toString(), docComplexAppendTwo.toString());
        assertEquals(docComplexCreate, docComplexAppendTwo);
    }

    //*************************************bodyWordCount Tests*************************************\\


    @Test public void testBodyWordCountEmpty() {
        Document doc = empty();
        assertEquals(0, doc.bodyWordCount());
    }

    @Test public void testBodyWordCountParagraph() {
        Document doc = paragraph("Hello, world!");
        assertEquals(2, doc.bodyWordCount());
    }

    @Test public void testBodyWordCountSectionParagraphs() {
        Document paragraphs = paragraph("Hello,   world!").append(paragraph("Goodbye."));
        Document doc = section("Section One", paragraphs);
        assertEquals(3, doc.bodyWordCount());
    }

    @Test public void testBodyWordCountDocumentA() {

        assertEquals(12, documentA.bodyWordCount());
    }

    @Test public void testBodyWordCountDocumentB() {
        assertEquals(6, documentB.bodyWordCount());
        assertEquals(2, sectB1.bodyWordCount());
        assertEquals(2, sectB2.bodyWordCount());
    }

    @Test public void testBodyWordCountDocumentC() {
        assertEquals(7, documentC.bodyWordCount());
        assertEquals(5, sectC1.bodyWordCount());
        assertEquals(2, sectC2.bodyWordCount());
    }

    @Test public void testBodyWordCountDocumentD() {
        assertEquals(13, documentD.bodyWordCount());
        assertEquals(4, documentD.getNextItemOfSameHeirarchy().bodyWordCount());
    }

    //*************************************tableOfContents Tests*************************************\\
    @Test public void testTableOfContentsDocumentA() {
        assertEquals(empty(), documentA.tableOfContents());
    }

    @Test public void testTableOfContentsDocumentB() {

        String tableContentDocBSec1 = "1" + "." + " " + "One fish." + " (2 words)";
        String tableContentDocBSec2 = "2" + "." + " " + "Red fish." + " (2 words)"; 

        Document tableContentDocB = paragraph(tableContentDocBSec1).append(paragraph(tableContentDocBSec2));

        assertEquals(tableContentDocB, documentB.tableOfContents());
    }

    @Test public void testTableOfContentsDocumentC() {
        String tableContentDocCSec1 = "1" + "." + " " + "Scene 1" + " (7 words)";
        String tableContentDocCSec11 = "1" + "." + "1" + "." + " " + "Guards" + " (5 words)"; 
        String tableContentDocCSec12 = "1" + "." + "2" + "." + " " + "Ghosts" + " (2 words)"; 

        Document tableContentDocC = paragraph(tableContentDocCSec1).append(paragraph(tableContentDocCSec11).append(paragraph(tableContentDocCSec12)));
        assertEquals(tableContentDocC, documentC.tableOfContents());      
    }

    @Test public void testTableOfContentsDocumentD() {
        String tableContentDocCSec1 = "1" + "." + " " + "Scene 1" + " (9 words)";
        String tableContentDocCSec11 = "1" + "." + "1" + "." + " " + "Guards" + " (5 words)"; 
        String tableContentDocCSec12 = "1" + "." + "2" + "." + " " + "Ghosts" + " (4 words)"; 

        String tableContentDocBSec1 = "2" + "." + " " + "One fish." + " (2 words)";
        String tableContentDocBSec2 = "3" + "." + " " + "Red fish." + " (2 words)"; 

        Document tableContentDocD = paragraph(tableContentDocCSec1).append(paragraph(tableContentDocCSec11).append(paragraph(tableContentDocCSec12).append(paragraph(tableContentDocBSec1).append(paragraph(tableContentDocBSec2)))));

        assertEquals(tableContentDocD, documentD.tableOfContents());      
    }

    //*************************************toMarkdown Tests*************************************\\
    @Test 
    public void testToMarkdownDocumentA() throws ConversionException {
        String documentAToMarkdownString = "\n\n" + "You say tomayto\\."
                + "\n\n" + "I say tomahto\\." 
                + "\n\n" + "You say potayto\\." 
                + "\n\n" + "I say potahto\\."; 

        assertEquals(documentAToMarkdownString, documentA.toMarkdown());
    }

    @Test public void testToMarkdownDocumentB() throws ConversionException {
        String documentBToMarkdownString = "\n\n" + "Dr\\. Seuss"
                + "\n" + "# " + "One fish\\."
                + "\n\n"+ "Two fish\\."
                + "\n" + "# " + "Red fish\\."
                + "\n\n" +  "Blue fish\\.";
        assertEquals(documentBToMarkdownString, documentB.toMarkdown());
    }

    @Test public void testToMarkdownDocumentC() throws ConversionException {
        String documentCToMarkdownString = "\n" + "# " + "Scene 1"
                + "\n" + "## " + "Guards"
                + "\n\n" + "Francisco, Bernardo, Horatio, & Marcellus\\."
                + "\n" + "## " + "Ghosts"
                + "\n\n" + "King Hamlet\\!";
        assertEquals(documentCToMarkdownString, documentC.toMarkdown());
    }


    //*************************************toLaTeX Tests*************************************\\
    @Test public void testToLaTeXDocumentC() throws ConversionException {
        String documentCToLaTexString = "\\documentclass{article}" +
                "\n" + "\\begin{document}" +
                "\n" + "\\section{Scene 1}" +
                "\n" + "\\subsection{Guards}" +
                "\n\n" +  "Francisco, Bernardo, Horatio, \\& Marcellus." +
                "\n" +  "\\subsection{Ghosts}" +
                "\n\n" +  "King Hamlet!" +
                "\n" +  "\\end{document}";
        
        assertEquals(documentCToLaTexString,documentC.toLaTeX());
    }

}

