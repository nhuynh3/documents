package awedoctime;

import static awedoctime.Document.paragraph;
import static awedoctime.Document.section;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import awedoctime.Document.ConversionException;

public class EmptyTest {
    // Methods: toString, equals, hashCode,
    //          append, bodyWordCount, 
    //          tableOfContents, toLaTeX, toMarkdown
    // Partition: an empty variant
    
    private static Document empty;
    private static Document testEmpty;
    private Document firstParagraph;
    private Document oneSectionNoContent;

    @Before public void setupDocuments() {
        empty = Document.empty();
        testEmpty = Document.empty();
        firstParagraph = paragraph("This is a paragraph");
        oneSectionNoContent = section("This is a section with no content", empty);
    }
    
    //*************************************bodyWordCount Tests*************************************\\

    @Test public void testBodyWordCountEmpty() {
        assertEquals(0, empty.bodyWordCount());
    }


    
    //*************************************toString Tests*************************************\\
    @Test public void testToString() {
        String expected = "Empty" + "\n";
        assertEquals(expected, empty.toString());
    }
    
    //*************************************equals Tests*************************************\\

    @Test public void testEquality() {
        assertTrue(empty.equals(testEmpty));
    }
    
    //*************************************hashCode Tests*************************************\\

    @Test public void testHashCode() {
        assertEquals(0, empty.hashCode());
    }
    
    //*************************************append Tests*************************************\\
    
    @Test public void testAppend() {
        Document newEmpty = empty.append(testEmpty);
        Document newParagraph = empty.append(firstParagraph);
        Document newSection = empty.append(oneSectionNoContent);

        
        assertTrue(newEmpty.equals(empty));
        assertTrue(newParagraph.equals(firstParagraph));
        assertTrue(newSection.equals(oneSectionNoContent));

    }
    
    //*************************************toMarkdown Tests*************************************\\
    @Test public void testToMarkdown() throws ConversionException {
        String emptyMarkdown = "";
        assertTrue(emptyMarkdown.equals(empty.toMarkdown()));
    }
    
    //*************************************toLaTeX Tests*************************************\\

    @Test public void testToLaTex() throws ConversionException {
        String expected = "\\documentclass{article}" 
                + "\n" + "\\begin{document}" 
                + "\n" + "\\end{document}";
        assertEquals(expected, empty.toLaTeX());
    }
    

}
