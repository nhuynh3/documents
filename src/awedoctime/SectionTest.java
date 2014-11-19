package awedoctime;

import static awedoctime.Document.empty;
import static awedoctime.Document.paragraph;
import static awedoctime.Document.section;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import awedoctime.Document.ConversionException;

public class SectionTest {

    // Methods: toString, equals, hashCode, append, bodyWordCount, 
    //          tableOfContents, 
    // Partition:
    // Document oneSectionNoContent:             Document w/ 1 section
    // Document SectionWithOneParagraph:         Document w/ 1 section  1 nested paragraph with many words, 1 nested paragraph with 1 word
    // Document SectionWithSectionAndParagraph:  Document w/ 1 section, 1 nested section, 1 nested paragraph
    // 
    // Methods: toLaTeX and toMarkdown 
    // Partitions:
    // Document SectionWithLaTeX escaped characters, with 3 nested sections, with >3 nested sections
    // Document SectionWithMarkdown escaped characters, with 7 nested sections, with >7 nested sections
    //

    private static Document empty;
    private static Document firstParagraph;
    private static Document secondParagraph;

    private static Document oneSectionNoContent;
    private static Document sectionWithOneParagraph;
    private static Document sectionWithNestedSectionAndParagraph;

    private static String oneSectionNoContentString;
    private static String sectionWithOneParagraphString;
    private static String sectionWithNestedSectionAndParagraphString;



    @Before public void setupDocuments() {
        empty = Document.empty();

        firstParagraph = paragraph("This is a paragraph");
        secondParagraph = paragraph("This is a second paragraph");

        oneSectionNoContent = section("This is a section with no content", empty);
        sectionWithOneParagraph = section("This is a section with one paragraph", firstParagraph);
        sectionWithNestedSectionAndParagraph = section("This is a section with a nested section and a paragraph", sectionWithOneParagraph);

        oneSectionNoContentString =  "#" + " " + "This is a section with no content" + "\n"   
                + empty.toString() 
                + empty.toString();

        sectionWithOneParagraphString = "#" + " " + "This is a section with one paragraph" + "\n" 
                + firstParagraph.toString()
                + empty.toString();

        sectionWithNestedSectionAndParagraphString = "#" + " " + "This is a section with a nested section and a paragraph" + "\n"
                + "#" + sectionWithOneParagraphString
                + empty.toString();

    }

    //*************************************toString Tests*************************************\\

    @Test public void testToStringOneSectionNoContent() {
        String expected = oneSectionNoContentString;
        assertEquals(expected, oneSectionNoContent.toString());
    }

    @Test public void testToStringSectionWithOneParagraph() {
        String expected = sectionWithOneParagraphString;
        assertEquals(expected, sectionWithOneParagraph.toString());
    }  

    @Test public void testToStringSectionWithNestedSectionAndParagraph() {
        String expected = sectionWithNestedSectionAndParagraphString;
        assertEquals(expected, sectionWithNestedSectionAndParagraph.toString());
    }

    //*************************************equals Tests*************************************\\

    @Test public void testEqualsOneSectionNoContent() {
        Document equalSection = section("This is a section with no content", empty);
        Document unequalTextSection = section("This is an unequal section with no content", empty);

        assertEquals(oneSectionNoContent, equalSection);
        assertEquals(equalSection, oneSectionNoContent);

        assertNotEquals(oneSectionNoContent, unequalTextSection);
        assertNotEquals(oneSectionNoContent, empty);
    }

    @Test public void testEqualsSectionWithOneParagraphContent() {
        Document equalSection = section("This is a section with one paragraph", firstParagraph);
        Document unequalSectionWithContent = section("This is a section with one paragraph", secondParagraph);
        Document unequalSectionWithoutContent = section("This is a section with one paragraph", empty);


        assertEquals(sectionWithOneParagraph, equalSection);
        assertEquals(equalSection, sectionWithOneParagraph);

        assertNotEquals(sectionWithOneParagraph, unequalSectionWithContent);
        assertNotEquals(sectionWithOneParagraph, unequalSectionWithoutContent);
    }

    @Test public void testEqualsSectionWithOneNestedSectionAndParagraphContent() {
        Document equalSection = section("This is a section with a nested section and a paragraph", sectionWithOneParagraph);
        Document unequalSectionWithContent = section("This is an unequal section with a nested section and a paragraph", 
                section("This is a section with one paragraph", 
                        firstParagraph));
        Document unequalSectionWithoutContent = section("This is a section with a nested section and a paragraph", 
                empty);

        assertTrue(sectionWithNestedSectionAndParagraph.toString().equals(equalSection.toString()));

        assertEquals(sectionWithNestedSectionAndParagraph, equalSection);
        assertEquals(equalSection, sectionWithNestedSectionAndParagraph);

        assertNotEquals(sectionWithNestedSectionAndParagraph, unequalSectionWithContent);
        assertNotEquals(sectionWithNestedSectionAndParagraph, unequalSectionWithoutContent);
    }

    //*************************************hashCode Tests*************************************\\

    @Test public void testHashCodeOneSectionNoContent() {
        Document equalSection = section("This is a section with no content", empty);
        Document unequalTextSection = section("This is an unequal section with no content", empty);

        assertEquals(oneSectionNoContent.hashCode(), equalSection.hashCode());

        assertNotEquals(oneSectionNoContent.hashCode(), unequalTextSection.hashCode());
        assertNotEquals(oneSectionNoContent.hashCode(), empty.hashCode());
    }

    @Test public void testHashCodeWithOneParagraphContent() {
        Document equalSection = section("This is a section with one paragraph", firstParagraph);
        Document unequalSectionWithContent = section("This is a section with one paragraph", secondParagraph);
        Document unequalSectionWithoutContent = section("This is a section with one paragraph", empty);


        assertEquals(sectionWithOneParagraph.hashCode(), equalSection.hashCode());

        assertNotEquals(sectionWithOneParagraph.hashCode(), unequalSectionWithContent.hashCode());
        assertNotEquals(sectionWithOneParagraph.hashCode(), unequalSectionWithoutContent.hashCode());
    }

    @Test public void testHashCodeWithOneNestedSectionAndParagraphContent() {
        Document equalSection = section("This is a section with a nested section and a paragraph", sectionWithOneParagraph);
        Document unequalSectionWithContent = section("This is an unequal section with a nested section and a paragraph", 
                section("This is a section with one paragraph", 
                        firstParagraph));
        Document unequalSectionWithoutContent = section("This is a section with a nested section and a paragraph", 
                empty);


        assertEquals(sectionWithNestedSectionAndParagraph.hashCode(), equalSection.hashCode());

        assertNotEquals(sectionWithNestedSectionAndParagraph.hashCode(), unequalSectionWithContent.hashCode());
        assertNotEquals(sectionWithNestedSectionAndParagraph.hashCode(), unequalSectionWithoutContent.hashCode());
    }

    //*************************************append Tests*************************************\\


    @Test public void testAppendEmpty() {
        Document sectionNoContentAppendEmpty = oneSectionNoContent.append(empty);
        Document sectionOneParaAppendEmpty = sectionWithOneParagraph.append(empty);
        Document sectionNestSectionAndParaAppendEmpty = sectionWithNestedSectionAndParagraph.append(empty);

        assertTrue(sectionNoContentAppendEmpty.equals(oneSectionNoContent));
        assertTrue(sectionOneParaAppendEmpty.equals(sectionWithOneParagraph));
        assertTrue(sectionNestSectionAndParaAppendEmpty.equals(sectionWithNestedSectionAndParagraph));

        //check if immutable
        assertFalse(sectionNoContentAppendEmpty == oneSectionNoContent);
        assertFalse(sectionOneParaAppendEmpty == sectionWithOneParagraph);
        assertFalse(sectionNestSectionAndParaAppendEmpty == sectionWithNestedSectionAndParagraph);

    }

    @Test public void testAppendSection() {
        Document sectionNoContentAppendSection = oneSectionNoContent.append(oneSectionNoContent);
        Document newSectionNoContentAppendSection = new Section("This is a section with no content", new Empty(), 
                new Section("This is a section with no content", new Empty()));

        Document sectionNoContentAppendSectionWithOnePara = oneSectionNoContent.append(sectionWithOneParagraph);
        Document newSectionNoContentAppendSectionWithOnePara = new Section("This is a section with no content", new Empty(), 
                new Section("This is a section with one paragraph", new Paragraph("This is a paragraph"), new Empty()));



        assertTrue(sectionNoContentAppendSection.equals(newSectionNoContentAppendSection));
        assertTrue(sectionNoContentAppendSectionWithOnePara.equals(newSectionNoContentAppendSectionWithOnePara));

    }


    @Test public void testAppendParagraph() {
        Document sectionNoContentAppendPargraph = oneSectionNoContent.append(firstParagraph);
        Document newSectionNoContentAppendParagraph = new Section("This is a section with no content", 
                new Paragraph("This is a paragraph", new Empty()), 
                new Empty());

        Document sectionWithOneParaAppendPara = sectionWithOneParagraph.append(firstParagraph);
        Document newSectionWithOneParaAppendPara = new Section("This is a section with one paragraph", 
                new Paragraph("This is a paragraph", new Paragraph("This is a paragraph")),
                new Empty());

        Document sectionWithNestSectAndParaAppendPara = sectionWithNestedSectionAndParagraph.append(firstParagraph);
        Document newSectionWithNestSectAndParaAppendPara = new Section("This is a section with a nested section and a paragraph",
                new Section("This is a section with one paragraph", 
                        new Paragraph("This is a paragraph", new Paragraph("This is a paragraph")),
                        new Empty()));

        assertTrue(sectionNoContentAppendPargraph.equals(newSectionNoContentAppendParagraph));
        assertTrue(sectionWithOneParaAppendPara.equals(newSectionWithOneParaAppendPara));
        assertTrue(sectionWithNestSectAndParaAppendPara.equals(newSectionWithNestSectAndParaAppendPara));

    }

    //*************************************bodyWordCount Tests*************************************\\

    @Test public void testBodyWordCountSectionNoContent() {

        assertEquals(0, oneSectionNoContent.bodyWordCount());
    }

    @Test public void testBodyWordCountSectionWithOnePara() {
        assertEquals(4, sectionWithOneParagraph.bodyWordCount());
    }

    @Test public void testBodyWordCountSectionWithOneParaWithTabs() {
        Document tabbedPara = paragraph("This is a \t paragraph");

        Document sectionWithOneParagraphAndTabbedPara = section("This is a section with one paragraph", tabbedPara);


        String sectionWithOneParagraphAndTabbedParaString = "#" + " " + "This is a section with one paragraph" + "\n" 
                + tabbedPara.toString()
                + empty.toString();

        assertEquals(4, sectionWithOneParagraphAndTabbedPara.bodyWordCount());
    }

    @Test public void testBodyWordCountSectionWithOneParaWithNewLines() {

        Document newLinePara = paragraph("This is a \n paragraph");

        Document sectionWithOneParagraphAndNewLinePara = section("This is a section with one paragraph", newLinePara);


        String sectionWithOneParagraphAndTabbedParaString = "#" + " " + "This is a section with one paragraph" + "\n" 
                + newLinePara.toString()
                + empty.toString();

        assertEquals(4, sectionWithOneParagraphAndNewLinePara.bodyWordCount());
    }

    @Test public void testBodyWordCountSectionWithNestSectOnePara() {
        assertEquals(4, sectionWithNestedSectionAndParagraph.bodyWordCount());
    }


    //*************************************tableOfContents Tests*************************************\\

    @Test public void testTableOfContentsSectionNoContent() {
        String tableContentSectionNoContentText = "1" + "." + " " + "This is a section with no content" + " (0 words)"; 
        Document tableContentSectionNoContent = paragraph(tableContentSectionNoContentText);
        assertEquals(tableContentSectionNoContent, oneSectionNoContent.tableOfContents());
    }

    @Test public void testTableOfContentsSectionOneWordPara() {
        String tableContentSectionNoContentText = "1" + "." + " " + "This is a section with no content" + " (1 word)"; 
        Document tableContentSectionNoContent = paragraph(tableContentSectionNoContentText);
        assertEquals(tableContentSectionNoContent, oneSectionNoContent.append(paragraph("one")).tableOfContents());
    }
    @Test public void testTableOfContentsSectionWithOnePara() {
        String tableContentSectionOneParagraphText = "1" + "." + " " + "This is a section with one paragraph" + " (4 words)"; 
        Document tableContentSectionOneParagraph = paragraph(tableContentSectionOneParagraphText);
        assertEquals(tableContentSectionOneParagraph, sectionWithOneParagraph.tableOfContents());
    }

    @Test public void testTableOfContentsSectionWithOneParaDifferentDocuments() {
        String tableContentSectionOneParagraphText = "1" + "." + " " + "This is a section with one paragraph" + " (4 words)"; 
        Document tableContentSectionOneParagraph = paragraph(tableContentSectionOneParagraphText);

        assertEquals(tableContentSectionOneParagraph, firstParagraph.append(sectionWithOneParagraph).tableOfContents());
    }

    @Test public void testTableOfContentsSectionWithNestSectOnePara() {
        String tableContentSectionWithNestedSectionAndParagraphText = "1" + "." + " " + "This is a section with a nested section and a paragraph" + " (4 words)";
        String tableContentSubSectionOneParagraphText = "1" + "." + "1" + "." + " " + "This is a section with one paragraph" + " (4 words)"; 

        Document tableContentSubSectionOneParagraph = paragraph(tableContentSubSectionOneParagraphText);
        Document tableContentSectionWithNestedSectionAndParagraph = new Paragraph(tableContentSectionWithNestedSectionAndParagraphText, tableContentSubSectionOneParagraph);

        assertEquals(tableContentSectionWithNestedSectionAndParagraph, sectionWithNestedSectionAndParagraph.tableOfContents());

    }

    //*************************************toMarkdown Tests*************************************\\

    @Test public void testToMarkdownOneSectionNoContent() throws ConversionException {
        String oneSectionNoContentMarkdownString = "\n" + "#" + " " + "This is a section with no content";

        assertTrue(oneSectionNoContentMarkdownString.equals(oneSectionNoContent.toMarkdown()));
    }

    @Test public void testToMarkdownSectionWithOnePara() throws ConversionException {
        String sectionWithOneParagraphText = "\n" + "#" + " " + "This is a section with one paragraph" + 
                "\n\n" + "This is a paragraph";

        assertTrue(sectionWithOneParagraphText.equals(sectionWithOneParagraph.toMarkdown()));
    }

    @Test public void testToMarkdownSectionWithNestedSectionOnePara() throws ConversionException {
        String sectionWithNestedSectionOneParaText =  "\n" + "#" + " " + "This is a section with a nested section and a paragraph" 
                + "\n" + "##" + " " + "This is a section with one paragraph" 
                + "\n\n" + "This is a paragraph";

        assertTrue(sectionWithNestedSectionOneParaText.equals(sectionWithNestedSectionAndParagraph.toMarkdown()));
    }

    @Test public void testToMarkdownSectionWithSixthLevelNesting() throws ConversionException {
        String sixSectionNestingText =  
                "\n" + "#" + " " + "Section 1" 
                        + "\n" + "##" + " " + "Section 2" 
                        + "\n" + "###" + " " + "Section 3"
                        + "\n" + "####" + " " + "Section 4"
                        + "\n" + "#####" + " " + "Section 5"
                        + "\n" + "######" + " " + "Section 6";

        Document sixSectionNesting = section("Section 1", 
                section("Section 2", 
                        section("Section 3", 
                                section("Section 4", 
                                        section("Section 5", 
                                                section("Section 6", empty()))))));

        assertEquals(sixSectionNestingText, sixSectionNesting.toMarkdown());
    }

    @Test (expected = ConversionException.class)
    public void testToMarkdownSectionWithSeventhLevelNesting() throws ConversionException {

        Document sevenSectionNesting = section("Section 1", section("Section 2", section("Section 3", section("Section 4", 
                section("Section 5", section("Section 6", section("Section 7", empty())))))));

        sevenSectionNesting.toMarkdown();
    }

    @Test public void testToMarkdownEscapedCharsInSection() throws ConversionException {
        String expected = "\n" + "#" + " " +"\\\\ \\` \\* \\_ \\{ \\} \\[ \\] \\( \\) \\# \\+ \\- \\. \\!";
        Document escapedSection = section("\\ ` * _ { } [ ] ( ) # + - . !", empty());

        assertTrue(expected.equals(escapedSection.toMarkdown()));
    }

    //*************************************toLaTeX Tests*************************************\\
    @Test public void testToLaTeXFirstParagraph() throws ConversionException {

        String oneSectionNoContentToLaTeXString = 
                "\\documentclass{article}" +
                        "\n" + "\\begin{document}" +
                        "\n" + "\\section{This is a section with no content}" +
                        "\n" +  "\\end{document}";

        assertEquals(oneSectionNoContentToLaTeXString, oneSectionNoContent.toLaTeX());
    }

    @Test public void testToLaTeXSectionWithOnePara() throws ConversionException {
        String sectionWithOneParagraphToLaTeXString = 
                "\\documentclass{article}" +
                        "\n" + "\\begin{document}" +
                        "\n" + "\\section{This is a section with one paragraph}" +
                        "\n\n" + "This is a paragraph" +
                        "\n" +  "\\end{document}";

        assertEquals(sectionWithOneParagraphToLaTeXString, sectionWithOneParagraph.toLaTeX());
    }

    @Test public void testToLaTeXSectionWithNestedSectionOnePara() throws ConversionException {
        String sectionWithNestedSectionOneParaToLaTeXString = 
                "\\documentclass{article}" +
                        "\n" + "\\begin{document}" +
                        "\n" + "\\section{This is a section with a nested section and a paragraph}" +
                        "\n" + "\\subsection{This is a section with one paragraph}" +
                        "\n\n" + "This is a paragraph" +
                        "\n" +  "\\end{document}";

        assertEquals(sectionWithNestedSectionOneParaToLaTeXString, sectionWithNestedSectionAndParagraph.toLaTeX());
    }

    @Test public void testToLaTeXSectionWithThreeNestedSection() throws ConversionException {
        String sectionWithThreeNestedSectionToLaTeXString = 
                "\\documentclass{article}" +
                        "\n" + "\\begin{document}" +
                        "\n" + "\\section{Section 1}" +
                        "\n" + "\\subsection{Section 2}" +
                        "\n" + "\\subsubsection{Section 3}" +
                        "\n" +  "\\end{document}";

        Document sectionWithThreeNestedSection = section("Section 1", 
                section("Section 2", 
                        section("Section 3", empty())));

        assertEquals(sectionWithThreeNestedSectionToLaTeXString, sectionWithThreeNestedSection.toLaTeX());
    }

    @Test (expected = ConversionException.class)
    public void testToLaTeXSectionWithFourNestedSection() throws ConversionException {

        Document sectionWithFourNestedSection = section("Section 1", 
                section("Section 2", 
                        section("Section 3", 
                                section("Section 4", empty()))));

        sectionWithFourNestedSection.toLaTeX();
    }


    @Test public void testToLaTeXEscapedChars() throws ConversionException {
        String escapedCharsString = "\\documentclass{article}" +
                "\n" + "\\begin{document}" +
                "\n" + "\\section{\\& \\% \\$ \\# \\~ \\_ \\^ \\\\ \\{ \\}}" +
                "\n" +  "\\end{document}";

        Document escapedSection = section("& % $ # ~ _ ^ \\ { }", empty());

        assertEquals(escapedCharsString, escapedSection.toLaTeX());
    }
}
