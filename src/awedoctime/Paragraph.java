package awedoctime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Paragraph implements Document {
    private String text;
    private Document rest;
    private static final String ESCAPELATEX= "&%$#~_^\\{}";
    private static final String ESCAPEMARKDOWN = "\\`*_{}[]()#+-.!";
    private static final String ESCAPEHTML = "<>&\"";


    private static final Map<String, String> HTMLMap;
    static {
        Map<String, String> aMap = new HashMap<String, String>();
        aMap.put("&", "&amp");
        aMap.put("<", "&lt");
        aMap.put(">", "&gt");
        aMap.put("\"", "&quot");

        HTMLMap = Collections.unmodifiableMap(aMap);
    }




    // Rep Invariant:
    // 
    //      text - containing English-language characters
    //             and punctuation, may not contain newlines
    //      rest - can be a Document, Paragraph, Section or Empty
    // Abstract Function:
    //      represents a paragraph in a document with a specific 
    //      textual and representation
    /**
     * 
     * @param text paragraph containing English-language characters
     *              and punctuation, may not contain newlines
     */
    public Paragraph(String text) {
        this.text = text;
        this.rest = new Empty();
    }

    /**
     * 
     * @param text paragraph containing English-language characters
     *                and punctuation, may not contain newlines
     * @param rest the Document that follows this paragraph
     */
    public Paragraph(String text, Document rest) {
        this.text = text;
        this.rest = rest;
    }


    @Override
    public boolean isEmpty() {
        return this.equals(new Empty());
    }

    /**
     * @return the text contained in the top level of
     * the Document
     */
    @Override
    public String getText() {
        return text;
    }

    /**
     * @return every level of the document other than 
     * the leading paragraphs
     */
    @Override
    public Document getBody() {

        return rest.getBody();

    }

    /**
     * @return a new document that represents other appended to paragraph
     */
    @Override
    public Document append(Document other) {
        return new Paragraph(text, rest.append(other));
    }


    /**
     * @return whether or not this can always be appended to
     *          the top level of any other document object
     */
    @Override
    public boolean startsWithParagraph() {
        return true;
    }

    @Override
    public Document getLeadingParagraphs() {
        return new Paragraph(text, rest.getLeadingParagraphs());
    }
    
    @Override
    public Document getNextItemOfSameHeirarchy() {
        return rest;
    }
    

    /**
     * Returns the number of words in the paragraphs of this document. Words
     * are delimited by one or more spaces and by the beginnings and ends of
     * paragraphs.
     * @return body word count
     */
    @Override
    public int bodyWordCount() {
        return getWordCountInLIne(text) + rest.bodyWordCount();
    }

    /**
     * counts the number of words in a string of words
     * @param textToMatch the string of words to count
     * @return the number of words in textToMatch
     */
    private int getWordCountInLIne(String textToMatch) {
        Pattern pattern = Pattern.compile("[^\\s]+");
        Matcher matcher = pattern.matcher(textToMatch);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
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
        return rest.tableOfContents();
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
        int firstLevel = 1;
        String start = "\\documentclass{article}" 
                + "\n" + "\\begin{document}";
        String end   = "\n" + "\\end{document}";
        return start + toLaTexHelper(firstLevel) + end;    
    }

    /** 
     * Returns a LaTeX-readable string of text
     * @param level the document level at which toLaTexHelper is called
     * @return a string of LaTex-escaped text if text cannot be converted to LaTex
     */
    @Override
    public String toLaTexHelper(int level) throws ConversionException {
        String txt = escapeText(ESCAPELATEX);
        return "\n\n" + txt + rest.toLaTexHelper(level); 
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
        int firstLevel = 1;
        return  toMarkdownHelper(firstLevel);
    }

    /** 
     * Returns a Markdown-readable string of text
     * @return a string of Markdown-escaped text
     */
    public String toMarkdownHelper(int level) throws ConversionException{
        String txt = escapeText(ESCAPEMARKDOWN);
        return "\n\n" + txt + rest.toMarkdownHelper(level);
    }

    /**
     * 
     * @param escapeString a string of characters that need to be escaped
     * in a specific document format
     * @return 
     */
    private String escapeText(String escapeString) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < text.length(); i++){
            String c = Character.toString(text.charAt(i));        
            if (escapeString.indexOf(c) == -1) {
                //index does not occur
                sb.append(c);
            } else {
                sb.append("\\");
                sb.append(c);
            }
        }
        return sb.toString();
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
        int firstLevel = 1;
        String closer = "\n"+ "</body>" + "\n" + "</html>";
        return header + toHTMLHelper(firstLevel) + closer; 
    }

    @Override
    /** 
     * Returns a HTML-readable string of text
     * @param level the document level at which toMarkdownHelper is called
     * @return a string of HTML-escaped text if text can be converted to HTML
     */
    public String toHTMLHelper(int level) throws ConversionException{

        String txt = escapeTextToHTML(ESCAPEHTML);
        return "\n" + "<p>" + txt + "</p>" + rest.toHTMLHelper(level+1);
    }

    private String escapeTextToHTML(String escapeString) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < text.length(); i++){
            String c = Character.toString(text.charAt(i));        
            if (escapeString.indexOf(c) == -1) {
                //index does not occur
                sb.append(c);
            } else {
                String escape = HTMLMap.get(c);
                sb.append(escape);
            }
        }
        return sb.toString();
    }

    /**
     * Returns a concise String representation of the document
     * in Markdown syntax
     */
    @Override
    public String toString() {
        return printDocument(1);
    }

    /**
     * @param level the level at which sectionHeading is called, where
     *         the top level = 1
     * @return a string representation of the document
     */
    @Override
    public String printDocument(int level) {
        return text + "\n" + rest.printDocument(level);
    }

    /**
     * @return the hashCode
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + rest.hashCode();
        result = prime * result + text.hashCode();
        return result;
    }

    /**
     * @return whether or not this is equal to obj
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Paragraph other = (Paragraph) obj;
        if (text == null) {
            if (other.getText() != null)
                return false;
        } else if (!text.equals(other.getText()))
            return false;
        if (rest == null) {
            if (other.getNextItemOfSameHeirarchy() != null)
                return false;
        } else if (!rest.equals(other.getNextItemOfSameHeirarchy())) {
            return false;
        }

        return true;
    }

}



