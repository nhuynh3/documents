package awedoctime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Section implements Document {
    private String heading;
    private Document contents;
    private Document rest;

    private static final String ESCAPELATEX= "&%$#~_^\\{}";
    private static final String ESCAPEMARKDOWN = "\\`*_{}[]()#+-.!";
    private static final String ESCAPEHTML = "<>&\"";


    private static final Map<String, String> HTMLMAP;
    static {
        Map<String, String> aMap = new HashMap<String, String>();
        aMap.put("&", "&amp");
        aMap.put("<", "&lt");
        aMap.put(">", "&gt");
        aMap.put("\"", "&quot");

        HTMLMAP = Collections.unmodifiableMap(aMap);
    }


    // Rep Invariant:
    //      heading - any string
    //      contents - any Document, Paragraph, Section or Empty
    //      rest - any Document, Section, Empty,
    //             CANNOT be a Paragraph
    //
    // Abstract Function:
    //      represents a section in a document with a specific 
    //      textual and hierarchical representation
    //      

    /**
     * 
     * @param heading - heading containing English-language characters
     *                  and punctuation, may not contain newlines
     * @param contents - contents of the section
     */
    public Section(String heading, Document contents) {
        this.heading = heading;
        this.contents = contents;
        this.rest = new Empty();
        checkRep();
    }

    /**
     * 
     * @param heading - heading containing English-language characters
     *                  and punctuation, may not contain newlines
     * @param contents - contents of the section
     * @param rest - the Document that follows this section. Must not be
     *               a Paragraph
     */
    public Section(String heading, Document contents, Document rest) {
        this.heading = heading;
        this.contents = contents;
        this.rest = rest;
        checkRep();
    }

    /**
     * Check that the rep invariant is true
     * Warning: this does nothing unless you turn on assertion checking
     * by passing -enableassertions to Java
     */
    private void checkRep() {
        //assert that there is no paragraph in rest
        assert !(rest.startsWithParagraph());
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
        return heading;
    }
    
    /**
     * @return the contents of a section
     */
    private Document getContents() {
        return contents;
    }

    
    /**
     * @return //TODO
     */
    @Override
    public boolean startsWithParagraph() {
        return false;
    }

    @Override
    public Document getLeadingParagraphs() {
        return new Empty();
    }
    
    @Override
    public Document getNextItemOfSameHeirarchy() {
        return rest;
    }

    /**
     * @return every level of the document other than the leading paragraphs
     */
    @Override
    public Document getBody() {
        return this;
    }



    /**
     * @return a new document which represents other appended to this
     * if a paragraph is appended to this, the paragraph must be nested 
     * within this section. This paragraph belongs to the section
     */

    @Override
    public Document append(Document other) {
        Document newRest;
        Document newContents;
        
        if (!rest.isEmpty() && !rest.startsWithParagraph()) {//if rest is a section, append to rest
            newContents = contents;
            newRest = rest.append(other);
            }
        else { //otherwise, check if other starts with a paragraph and append accordingly
            if (other.startsWithParagraph()) { //then change the contents of section
                newContents = contents.append(other.getLeadingParagraphs());
                newRest = rest.append(other.getBody());
            } 
            else {
                newContents = contents;
                newRest = rest.append(other);
            }
        }
        return new Section(heading, newContents, newRest);
    }

        /**
         * Returns the number of words in the paragraphs of this document. Words
         * are delimited by one or more spaces and by the beginnings and ends of
         * paragraphs.
         * @return body word count
         */
        @Override
        public int bodyWordCount() {
            return contents.bodyWordCount() + rest.bodyWordCount();
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
         * helper method that prints the outline level
         * that precedes the document
         * @param level an array that represents the level at which a 
         *      document sits
         * @return a string that represents the outline level in a table of contents
         */
        private String sectionOutline(List<Integer> level) {
            StringBuilder sb = new StringBuilder("");
            for (Integer integer : level) {
                sb.append(integer + ".");
            }
            return sb.toString();
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
        public Document createDocumentLevel(List<Integer> level) {
            List<Integer> restLevel = getRestLevels(level);
            List<Integer> contentLevel = getContentLevels(level);
            int wordCount = contents.bodyWordCount();
            String wordOrWords;
            
            if (wordCount == 1)  {
                wordOrWords = " word)";
            } else {
                wordOrWords = " words)";
            }
            
            Document paragraph = new Paragraph(sectionOutline(level) + " " + heading + " (" 
                    + wordCount
                    + wordOrWords);
            return paragraph.append(contents.createDocumentLevel(contentLevel)).append(rest.createDocumentLevel(restLevel));
        }

        /**
         * 
         * @param level an array that represents the level at which a 
         *      document sits
         * @return a new array that represents the level at which a documents content
         * sits at
         */
        private List<Integer>  getContentLevels(List<Integer> level) {
            List<Integer> contentLevel = makeCopy(level);
            contentLevel.add(1);
            return contentLevel;

        }

        /**
         * 
         * @param level an array that represents the level at which a 
         *      document sits
         * @return a new array that represents the level at which a documents rest
         * sits at
         */
        private List<Integer> getRestLevels(List<Integer> level) {
            List<Integer> restLevel = makeCopy(level);
            Integer lastElement = restLevel.remove(restLevel.size()-1);
            restLevel.add(lastElement + 1);
            return restLevel;
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
            int maxLaTeXLevels = 3;
            if (level > maxLaTeXLevels) {
                throw new ConversionException("Cannot create a LaTeX with more than 3 nested levels");
            }
            else{
                String escapeString = ESCAPELATEX;
                String txt = escapeText(escapeString);
                return "\n" + sectionLaTeXHeading(level) + txt + "}" + contents.toLaTexHelper(level+1) + rest.toLaTexHelper(level);
            }
        }

        /** 
         * @param level the level at which sectionLaTeXHeading is called, where
         *        the top level = 1
         * @return a string that corresponds to the level at which a section is nested
         *         in LaTex syntax
         */
        private String sectionLaTeXHeading(int level) {
            String wrapper = "";
            if (level == 1) {
                wrapper+="\\section{";
            } else if (level == 2) {
                wrapper+="\\subsection{";
            } else if (level == 3) {
                wrapper+="\\subsubsection{";
            }
            return wrapper;
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
            return toMarkdownHelper(firstLevel);
        }

        /** 
         * Returns a Markdown-readable string of text
         * @param level the document level at which toMarkdownHelper is called
         * @return a string of Markdown-escaped text if text cannot be converted to markdown
         */
        public String toMarkdownHelper(int level) throws ConversionException{
            if (level > 6) {
                throw new ConversionException("Cannot create a Markdown with more than 6 nested levels");

            }
            else{
                String escapeString = ESCAPEMARKDOWN;
                String txt = escapeText(escapeString);
                return "\n" + sectionHeading(level) + " " + txt + contents.toMarkdownHelper(level+1) + rest.toMarkdownHelper(level);
            }    
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
            if (level > 6) {
                throw new ConversionException("Cannot create a HTML with more than 6 nested levels");
            }
            else{
                String txt = escapeTextToHTML(ESCAPEHTML);
                List<String> headings = sectionHTMLHeading(level);
                return "\n" + headings.get(0) + txt + contents.toHTMLHelper(level+1) + headings.get(1) + rest.toHTMLHelper(level);
            }
        }

        private List<String> sectionHTMLHeading(int level) {
            List<String> headings = new ArrayList<>();
            headings.add("<h"+ level + ">");
            headings.add("</h"+ level + ">");
            return headings;
        }

        private String escapeTextToHTML(String escapeString) {
            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i < heading.length(); i++){
                String c = Character.toString(heading.charAt(i));        
                if (escapeString.indexOf(c) == -1) {
                    //index does not occur
                    sb.append(c);
                } else {
                    String escape = HTMLMAP.get(c);
                    sb.append(escape);
                }
            }
            return sb.toString();
        }
        /**
         * 
         * @param escapeString a string of characters that need to be escaped
         * in a specific document format
         * @return 
         */
        private String escapeText(String escapeString) {
            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i < heading.length(); i++){
                String c = Character.toString(heading.charAt(i));        
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
         *        the top level = 1
         * @return a string of #'s, where the number of #'s correspond to 
         *         the level at which a section belongs, as per Markdown syntax
         */
        private String sectionHeading(int level) {
            StringBuilder sb = new StringBuilder("");
            for (int i=0; i<level; i++) {
                sb.append("#");
            }
            return sb.toString();
        }

        /**
         * @param level the level at which printDocument is called, where
         *        the top level = 1
         * @return a string representation of the document
         */
        @Override
        public String printDocument(int level) {
            return sectionHeading(level) + " " + heading + "\n" + contents.printDocument(level+1) + rest.printDocument(level);
        }

        /**
         * creates a deep copy of a list
         * @param level an array of integers
         * @return a new copy of level
         */
        private List<Integer> makeCopy(List<Integer> level) {
            List<Integer> copy = new ArrayList<Integer>();

            for (Integer i : level) {
                copy.add(i);
            }
            return copy;
        }

        /**
         * @return the hashCode of this
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + contents.hashCode();
            result = prime * result + heading.hashCode();
            result = prime * result + rest.hashCode();
            return result;
        }

        /**
         * @return whether or not this is observationally equivalent to obj
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Section other = (Section) obj;
            if (heading == null) {
                if (other.getText() != null)
                    return false;
            } else if (!heading.equals(other.getText()))
                return false;
            if (contents == null) {
                if (other.getContents() != null)
                    return false;
            } else if (!contents.equals(other.getContents())) {
                return false;
            }
            if (rest == null) {
                if (other.getNextItemOfSameHeirarchy() != null)
                    return false;
            } else if (!rest.equals(other.getNextItemOfSameHeirarchy())) {
                return false;
            }
            return true;
        }
    }


