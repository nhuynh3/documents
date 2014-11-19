package awedoctime;

import static awedoctime.Document.empty;
import static awedoctime.Document.paragraph;
import static awedoctime.Document.section;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import awedoctime.Document.ConversionException;

public class DocumentToHTMLTest {
    // Methods: toHTML
    // Partition: an empty document, 
    //            a paragraph document w/ normal text, w/escaped text
    //            2 paragraphs
    //            a section document w/ normal text, w/ escaped text
    //            a section with a paragraph
    //            a section with a nested section and a paragraph
    //            Document B
    //            Document C

    private static String header;
    private static String closer;
    private static Empty empty;
    private Document firstParagraph;
    private Document secondParagraph;
    private Document twoParagraphInOrder;

    private static Document oneSectionNoContent;
    private static Document sectionWithOneParagraph;
    private static Document sectionWithNestedSectionAndParagraph;


    @Before public void setupDocuments() {
        header ="<html lang = \"en\">"
                + "\n" + "<head>" 
                + "\n" + "<title>The Title</title>"
                + "\n" + "</head>"
                + "\n" + "<body>";
        closer = "\n"+ "</body>" + "\n" + "</html>";


        empty = new Empty();
        new Empty();
        firstParagraph = paragraph("This is a paragraph");
        oneSectionNoContent = section("This is a section with no content", empty);
        secondParagraph = paragraph("This is a second paragraph");
        twoParagraphInOrder = firstParagraph.append(secondParagraph);


        oneSectionNoContent = section("This is a section with no content", empty);
        sectionWithOneParagraph = section("This is a section with one paragraph", firstParagraph);
        sectionWithNestedSectionAndParagraph = section("This is a section with a nested section and a paragraph", sectionWithOneParagraph);
    }

    //*************************************toHTML Empty Tests*************************************\\

    @Test public void testToHTML() throws ConversionException {
        String expected = header + closer;
        assertEquals(expected, empty.toHTML());
    }

    //*************************************toHTML Paragraph Tests*************************************\\
    @Test public void testToHTMLFirstParagraph() {
        String expected = header 
                + "\n" + "<p>" + "This is a paragraph" + "</p>"
                +  closer;

        try {
            assertEquals(expected, firstParagraph.toHTML());
        } catch (ConversionException e) {
            fail("exception thrown");
        }
    }

    @Test public void testToHTMLTwoParagraph() {
        String expected = header 
                + "\n" + "<p>" + "This is a paragraph" + "</p>"
                + "\n" + "<p>" + "This is a second paragraph"  + "</p>"
                +  closer;

        try {
            assertEquals(expected, twoParagraphInOrder.toHTML());
        } catch (ConversionException e) {
            fail("exception thrown");
        }
    }    

    @Test public void testToHTMLEscapedChars() {
        String expected = header 
                + "\n" + "<p>" + "&amp &lt &gt &quot" + "</p>"
                +  closer;
        Document escapedParagraph = paragraph("& < > \"");

        try {
            assertEquals(expected, escapedParagraph.toHTML());
        } catch (ConversionException e) {
            fail("exception thrown");
        }
    }

    //*************************************toHTML Section Tests*************************************\\
    @Test public void testToHTMLOneSectionNoContent() {
        String oneSectionNoContentHTMLString =header 
                + "\n" + "<h1>" + "This is a section with no content" + "</h1>"
                +  closer;

        try {
            assertTrue(oneSectionNoContentHTMLString.equals(oneSectionNoContent.toHTML()));
        } catch (ConversionException e) {
            fail("exception thrown");
        }
    }

    @Test public void testToHTMLSectionWithOnePara() {
        String sectionWithOneParagraphText = header 
                + "\n" + "<h1>" + "This is a section with one paragraph" 
                + "\n" + "<p>" + "This is a paragraph"  + "</p>"+ "</h1>"
                +  closer;
        try {
            assertTrue(sectionWithOneParagraphText.equals(sectionWithOneParagraph.toHTML()));
        } catch (ConversionException e) {
            fail("exception thrown");
        }
    }

    @Test public void testToHTMLSectionWithNestedSectionOnePara() {
        String sectionWithNestedSectionOneParaText =  
                header 
                + "\n" + "<h1>" + "This is a section with a nested section and a paragraph" 
                + "\n" + "<h2>" + "This is a section with one paragraph" 
                + "\n" + "<p>" + "This is a paragraph" + "</p>"+ "</h2>"+ "</h1>"
                +  closer;

        try {
            assertTrue(sectionWithNestedSectionOneParaText.equals(sectionWithNestedSectionAndParagraph.toHTML()));
        } catch (ConversionException e) {
            fail("exception thrown");
        }
    }

    @Test public void testToHTMLSectionWithSixthLevelNesting() {
        String sixSectionNestingText =  header 
                + "\n" + "<h1>" + "Section 1" 
                + "\n" + "<h2>" + "Section 2"
                + "\n" + "<h3>" + "Section 3"
                + "\n" + "<h4>" + "Section 4"
                + "\n" + "<h5>" + "Section 5"
                + "\n" + "<h6>" + "Section 6"
                + "</h6>"+ "</h5>" + "</h4>"+ "</h3>" + "</h2>"+ "</h1>"
                +  closer;


        Document sixSectionNesting = section("Section 1", 
                section("Section 2", 
                        section("Section 3", 
                                section("Section 4", 
                                        section("Section 5", 
                                                section("Section 6", empty()))))));

        try {
            assertEquals(sixSectionNestingText, sixSectionNesting.toHTML());
        } catch (ConversionException e) {
        }
    }

    @Test
    public void testToHTMLSectionWithSeventhLevelNesting() {

        Document sevenSectionNesting = section("Section 1", section("Section 2", section("Section 3", section("Section 4", 
                section("Section 5", section("Section 6", section("Section 7", empty())))))));
        try {
            sevenSectionNesting.toHTML();
            fail("didn't catch exception");
        } catch (ConversionException e) {

        }
    }

    @Test public void testToHTMLEscapedCharsInSection() {
        String expected = header 
                + "\n" + "<h1>" + "&amp &lt &gt &quot" + "</h1>"
                +  closer;
        Document escapedSection = section("& < > \"", empty());

        try {
            assertEquals(expected,escapedSection.toHTML());
        } catch (ConversionException e) {
            fail("exception thrown");
        }
    }
}
