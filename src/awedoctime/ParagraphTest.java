package awedoctime;

import static awedoctime.Document.paragraph;
import static awedoctime.Document.section;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import awedoctime.Document.ConversionException;

public class ParagraphTest {
    // Methods: toString, equals, hashCode, append, bodyWordCount, 
    //          tableOfContents,
    // Partition:
    // Document oneParagraph:                          Document w/ 1 paragraph
    // Document twoParagraph:                          Document w/ 2 paragraph, symmetry of equals and hashcode
    //
    // Methods:  toLaTeX, toMarkdown
    // Partition:
    // Paragraph with escaped characters, 1 paragraph, 2 paragraphs

    private static Document empty;
    private static Document firstParagraph;
    private static Document secondParagraph;

    private static Document twoParagraphInOrder;
    private static Document twoParagraphOutOrder;

    private static String oneParagraphString;
    private static String twoParagraphString;
    private Document oneSectionNoContent;



    @Before public void setupDocuments() {
        empty = Document.empty();
        firstParagraph = paragraph("This is a paragraph");
        secondParagraph = paragraph("This is a second paragraph");
        twoParagraphInOrder = firstParagraph.append(secondParagraph);
        twoParagraphOutOrder = secondParagraph.append(firstParagraph);
        oneSectionNoContent = section("This is a section with no content", empty);


        oneParagraphString = "This is a paragraph" + "\n" + empty.toString();
        twoParagraphString = "This is a paragraph" + "\n" + "This is a second paragraph" + "\n" + empty.toString();
    }


    //*************************************toString Tests*************************************\\

    @Test public void testToStringOneParagraph() {
        String expected = oneParagraphString;
        assertEquals(expected, firstParagraph.toString());
    }

    @Test public void testToStringTwoParagraph() {
        String expected = twoParagraphString;
        assertEquals(expected, twoParagraphInOrder.toString());
    }

    //*************************************equals Tests*************************************\\

    @Test public void testEqualsOneParagraph() {
        Document equalParagraph = paragraph("This is a paragraph");
        Document unequalTextParagraph = paragraph("This is an unequal paragraph");

        assertEquals(firstParagraph, equalParagraph);
        //symmetric?
        assertEquals(equalParagraph, firstParagraph);

        assertNotEquals(firstParagraph, unequalTextParagraph);
        assertNotEquals(firstParagraph, empty);
    }

    @Test public void testEqualsTwoParagraph() {
        Document equalParagraph = paragraph("This is a paragraph").append(paragraph("This is a second paragraph"));

        assertEquals(twoParagraphInOrder, equalParagraph);
        assertEquals(equalParagraph, twoParagraphInOrder);

        assertNotEquals(twoParagraphInOrder, twoParagraphOutOrder);
        assertNotEquals(firstParagraph, empty);
    }

    //*************************************hashCode Tests*************************************\\
    @Test public void testHashCodeOneParagraph() {
        Document equalParagraph = paragraph("This is a paragraph");
        Document unequalTextParagraph = paragraph("This is an unequal paragraph");

        assertEquals(firstParagraph.hashCode(), equalParagraph.hashCode());

        assertNotEquals(firstParagraph.hashCode(), unequalTextParagraph.hashCode());
        assertNotEquals(firstParagraph.hashCode(), empty.hashCode());
    }

    @Test public void testHashCodeTwoParagraph() {
        Document equalParagraph = paragraph("This is a paragraph").append(paragraph("This is a second paragraph"));

        assertEquals(twoParagraphInOrder.hashCode(), equalParagraph.hashCode());

        assertNotEquals(twoParagraphInOrder.hashCode(), twoParagraphOutOrder.hashCode());
        assertNotEquals(firstParagraph.hashCode(), empty.hashCode());
    }


    //*************************************append Tests*************************************\\

    @Test public void testAppend() {
        Document paragraphAppendEmpty = firstParagraph.append(empty);

        Document paragraphAppendParagraph = firstParagraph.append(secondParagraph);
        Document newParagraphWithParagraph = new Paragraph("This is a paragraph", new Paragraph("This is a second paragraph"));

        Document paragraphAppendSection = firstParagraph.append(oneSectionNoContent);
        Document newParagraphWithSection = new Paragraph("This is a paragraph", oneSectionNoContent);


        assertTrue(paragraphAppendEmpty.equals(firstParagraph));
        assertTrue(paragraphAppendParagraph.equals(newParagraphWithParagraph));

        assertTrue(paragraphAppendSection.equals(newParagraphWithSection));

        //check if immutable
        assertFalse(paragraphAppendEmpty == firstParagraph);
    }

    //*************************************bodyWordCount Tests*************************************\\

    @Test public void testBodyWordCountOneParagraph() {
        Document firstParagraphSpaces = paragraph("This is a   paragraph");

        assertEquals(4, firstParagraph.bodyWordCount());
        assertEquals(4, firstParagraphSpaces.bodyWordCount());
    }

    @Test public void testBodyWordCountMultiParagraph() {
        assertEquals(9, twoParagraphInOrder.bodyWordCount());
    }

    //*************************************toMarkdown Tests*************************************\\

    @Test
    public void testToMarkdownSingleParagraph() throws ConversionException {
        String firstParagraphText = "\n\n" + "This is a paragraph";

        assertTrue(firstParagraphText.equals(firstParagraph.toMarkdown()));
    }

    @Test public void testToMarkdownDoubleParagraph() throws ConversionException {
        String doubleParagraphText ="\n\n" + "This is a paragraph" + "\n\n" + "This is a second paragraph";

        assertTrue(doubleParagraphText.equals(twoParagraphInOrder.toMarkdown()));
    }


    @Test public void testToMarkdownEscapedChars() throws ConversionException {
        String expected = "\n\n" + "\\\\ \\` \\* \\_ \\{ \\} \\[ \\] \\( \\) \\# \\+ \\- \\. \\!";
        Document escapedParagraph = paragraph("\\ ` * _ { } [ ] ( ) # + - . !");

        assertTrue(expected.equals(escapedParagraph.toMarkdown()));
    }



    //*************************************toLaTeX Tests*************************************\\
    @Test public void testToLaTeXFirstParagraph() throws ConversionException {
        String firstParagraphToLaTexString = "\\documentclass{article}" +
                "\n" + "\\begin{document}" +
                "\n\n" + "This is a paragraph" +
                "\n" +  "\\end{document}";

        assertEquals(firstParagraphToLaTexString, firstParagraph.toLaTeX());
    }

    @Test public void testToLaTeXDoubleParagraph() throws ConversionException {
        String doubleParagraphToLaTexString = "\\documentclass{article}" +
                "\n" + "\\begin{document}" +
                "\n\n" + "This is a paragraph" +
                "\n\n" + "This is a second paragraph" +
                "\n" +  "\\end{document}";
        assertEquals(doubleParagraphToLaTexString, twoParagraphInOrder.toLaTeX());
    }

    @Test public void testToLaTeXEscapedChars() throws ConversionException {
        String escapedCharsString = "\\documentclass{article}" +
                "\n" + "\\begin{document}" +
                "\n\n" + "\\& \\% \\$ \\# \\~ \\_ \\^ \\\\ \\{ \\}" +
                "\n" +  "\\end{document}";

        Document escapedParagraph = paragraph("& % $ # ~ _ ^ \\ { }");
        assertEquals(escapedCharsString, escapedParagraph.toLaTeX());
    }
}
