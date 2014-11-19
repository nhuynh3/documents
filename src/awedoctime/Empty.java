package awedoctime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Empty implements Document {
    // Rep invariant:
    //      cannot contain another document
    // Abstraction function: 
    //      represents an empty document
    
    /**
     * Make a new Empty
     */
    public Empty() {        
    }

    /**
     * @return the text contained in the top level of
     * the Document
     */
    @Override
    public String getText() {
        return "";
    }
    
    /**
     * @return every level of the document other than 
     * the top level
     */
    @Override
    public Document getBody() {
        return new Empty();
    }

    /**
     * Returns a document which has the contents of this followed by the
     * contents of other. Top-level sections in other become top-level sections
     * in the new document.
     * @param other document to append
     * @return concatenation of this and other
     */
    @Override
    public Document append(Document other) {
        return other;
    }
    
    /**
     * @return //TODO
     * 
     */
    @Override
    public boolean startsWithParagraph() {
        return false;
    }
    
    /**
     * Returns the number of words in the paragraphs of this document. Words
     * are delimited by one or more spaces and by the beginnings and ends of
     * paragraphs.
     * @return body word count
     */
    @Override
    public int bodyWordCount() {
        return 0;
    }

    /**
     * Returns a document containing one paragraph for every section heading in
     * this document. Each paragraph contains:
     * <br> - the section number (starting from 1), written as a sequence of
     *        parent section numbers separated by periods, ending with the
     *        position of this section under its parent (or under the top level,
     *        if none)
     * <br> - the section heading
     * <br> - the word count of paragraphs in this section and its sub-sections,
     *        written as "1 word", or "N words" for N != 1
     * <br> For an example, see the problem set handout.
     * @return table of contents
     */
    @Override
    public Document tableOfContents() {
        Integer[] levelArray = new Integer[] { 1 };
        List<Integer> level = new ArrayList<Integer>(Arrays.asList(levelArray));
        return createDocumentLevel(level);
    }
    
    /**
     * creates a document preceded by the level representation
     * of where the document is within a document
     * @param level the top level of the document represented in
     * List format
     * @return a document preceded by the level representation
     * relative to level
     */
    @Override
    public Document createDocumentLevel(List<Integer> contentLevel) {
        return new Empty();
    }
    
    /**
     * Returns a LaTeX representation of the document that:
     * <br> - contains a preamble with document class "article" and no other
     *        options or packages; uses \section, \subsection, & \subsubsection
     *        to indicate sections; uses ordinary paragraphs
     * <br> - renders all the section headings and paragraphs of the document
     *        using appropriate LaTeX syntax and character escaping, with no
     *        additional formatting
     * <br> For an example, see the problem set handout.
     * @return LaTeX conversion
     * @throws ConversionException if the document cannot be converted
     */
    @Override
    public String toLaTeX() throws ConversionException {
        String start = "\\documentclass{article}" 
                     + "\n" + "\\begin{document}";
        String end   = "\n" + "\\end{document}";
        int firstLevel = 1;
        return start + toLaTexHelper(firstLevel) + end;
    }
    
    /** 
     * Returns a LaTeX-readable string of text
     * @param level the document level at which toLaTexHelper is called
     * @return a string of LaTex-escaped text if text can be converted to LaTex
     * @throws ConversionException if the document cannot be converted
     */
    @Override
    public String toLaTexHelper(int i) throws ConversionException {
        return "";
    }

    /**
     * Returns a Markdown representation of the document that:
     * <br> - renders all the section headings and paragraphs of the document
     *        using appropriate Markdown syntax and character escaping, with no
     *        additional formatting
     * <br> For an example, see the problem set handout.
     * @return Markdown conversion
     * @throws ConversionException if the document cannot be converted
     */
    @Override
    public String toMarkdown() throws ConversionException {
        return "";
    }
    
    /** 
     * Returns a Markdown-readable string of text
     * @param level the document level at which toMarkdownHelper is called
     * @return a string of Markdown-escaped text if text can be converted to markdown
     * @throws ConversionException if the document cannot be converted
     */
    @Override
    public String toMarkdownHelper(int i) throws ConversionException {
        return "";
    }
    
    @Override
    /**
     * Returns a HTML representation of the document that:
     * <br> - renders all the section headings and paragraphs of the document
     *        using appropriate HTML syntax and character escaping, with no
     *        additional formatting
     * @return HTML conversion
     * @throws ConversionException if the document cannot be converted
     */
    public String toHTML() throws ConversionException{
        String header ="<html lang = \"en\">"
                + "\n" + "<head>" 
                + "\n" + "<title>The Title</title>"
                + "\n" + "</head>"
                + "\n" + "<body>";
        
        String closer = "\n"+ "</body>" + "\n" + "</html>";
        int firstLevel = 1;
        return header + toHTMLHelper(firstLevel) + closer;
    }
    
    @Override
    /** 
     * Returns a HTML-readable string of text
     * @param level the document level at which toMarkdownHelper is called
     * @return a string of HTML-escaped text if text can be converted to HTML
     */
    public String toHTMLHelper(int i) throws ConversionException{
        return "";
    }

    /**
     * Returns a concise String representation of the document
     * in Markdown syntax
     */
    @Override
    public String toString() {
        int firstLevel = 1;
        return printDocument(firstLevel);
    }
    
    /**
     * @param level the level at which sectionHeading is called, where
     *         the top level = 1
     * @return a string representation of the document
     */
    @Override
    public String printDocument(int level) {
        return "Empty\n";
    }
    
    /**
     * @return the integer hashcode of empty
     */
    @Override
    public int hashCode() {
        return 0;
    }
    
    /**
     * @return if other is an Empty
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof Empty);               
    }

    @Override
    public boolean isEmpty() {
        return true;
    }
    
    

    @Override
    public Document getLeadingParagraphs() {
        return new Empty();
    }
    

    @Override
    public Document getNextItemOfSameHeirarchy() {
        return new Empty();
    }
    
    
}
