/* Copyright (c) 2007-2014 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package awedoctime;

import java.util.List;

/**
 * An immutable structured document.
 * 
 * <p>
 * Documents consist of paragraphs and sections. The top-level of a document
 * may have any number of paragraphs or sections. Paragraphs always belong to
 * the section whose heading most closely precedes them in the document.
 * </p>
 * 
 * This is a required ADT interface. It defines static creator methods, which
 * you must implement here, and instance methods, which you must implement in
 * the concrete variants of Document.
 * 
 * You MUST NOT weaken the provided specifications, and you MUST NOT change the
 * method signatures (names, parameter types, return types, and exceptions).
 * You MAY strengthen the provided specifications, and you MAY also add
 * additional methods, but you MUST NOT expose your rep when you write your
 * strengthened specs or write specs of your additional methods.
 */
public interface Document {
    
    // Datatype definition
    // Document = Empty + Paragraph(text:String, rest:Document)
    //                  + Section(header:String, contents:Document, rest:Document)
    /**
     * Creates a new empty document.
     * @return a new empty document
     */
    public static Document empty() {
        return new Empty();
    }
    
    /**
     * Creates a new document with one paragraph.
     * @param text paragraph text containing English-language characters and
     *             punctuation, may not contain newlines
     * @return a new paragraph
     */
    public static Document paragraph(String text) {
        return new Paragraph(text);
    }
    
    /**
     * Creates a new document with one top-level section.
     * @param heading section heading containing English-language characters
     *                and punctuation, may not contain newlines
     * @param contents contents of the section
     * @return a new section
     */
    public static Document section(String heading, Document contents) {
        return new Section(heading, contents);
    }
    
    /**
     * 
     * @return the text contained in the top level of
     * the Document
     */
    public String getText();
    
    
    
    /**
     * Returns a document which has the contents of this followed by the
     * contents of other. Top-level sections in other become top-level sections
     * in the new document.
     * @param other document to append
     * @return concatenation of this and other
     */
    public Document append(Document other);
    
    /**
     * returns whether or not this document is empty
     * @return
     */
    public boolean isEmpty();
    
    /**
     * Returns whether or not this starts with a a paragraph or not
     * @return true if the first thing in this document is a paragraph
     */
    public boolean startsWithParagraph();
    
    /**
     * Returns the first paragraphs of this document or an empty document if
     * the document does not begin with a paragraph
     * @return a document containing paragraphs
     */
    public Document getLeadingParagraphs();
    
    /**
     * Returns the next part of the document that shares
     * the same document hierarchy as the document 
     * item on which this method is called
     * @return
     */
    public Document getNextItemOfSameHeirarchy();
    
    /**
     * 
     * @return every level of the document other than 
     * the leading paragraphs
     */
    public Document getBody();
    
    /**
     * Returns the number of words in the paragraphs of this document. Words
     * are delimited by one or more spaces and by the beginnings and ends of
     * paragraphs.
     * @return body word count
     */
    public int bodyWordCount();
    
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
    public Document tableOfContents();
    
    /**
     * creates a document preceded by the level representation
     * of where the document is within a document
     * @param contentLevel the top level of the document represented in
     * List format
     * @return a document preceded by the level representation
     * relative to contentLevel
     */
    public Document createDocumentLevel(List<Integer> contentLevel);


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
    public String toLaTeX() throws ConversionException;
    
    /** 
     * Returns a LaTeX-readable string of text
     * @param level the document level at which toLaTexHelper is called
     * @return a string of LaTex-escaped text if document can be converted to LaTex
     * @throws ConversionException if the document cannot be converted
     */
    public String toLaTexHelper(int i) throws ConversionException;
    
    
    /**
     * Returns a Markdown representation of the document that:
     * <br> - renders all the section headings and paragraphs of the document
     *        using appropriate Markdown syntax and character escaping, with no
     *        additional formatting
     * <br> For an example, see the problem set handout.
     * @return Markdown conversion
     * @throws ConversionException if the document cannot be converted
     */
    public String toMarkdown() throws ConversionException;
    
    /** 
     * Returns a Markdown-readable string of text
     * @param level the document level at which toMarkdownHelper is called
     * @return a string of Markdown-escaped text if text cannot be converted to markdown
     * @throws ConversionException if the document can be converted
     */
    public String toMarkdownHelper(int i) throws ConversionException;

    /**
     * Returns a HTML representation of the document that:
     * <br> - renders all the section headings and paragraphs of the document
     *        using appropriate HTML syntax and character escaping, with no
     *        additional formatting
     * @return HTML conversion
     * @throws ConversionException if the document cannot be converted
     */
    public String toHTML() throws ConversionException;
    
    /** 
     * Returns a HTML-readable string of text
     * @param level the document level at which toMarkdownHelper is called
     * @return a string of HTML-escaped text if the document can be converted to HTML
     */
    public String toHTMLHelper(int i) throws ConversionException;

        
    /**
     * Returns a concise String representation of the document
     * in Markdown syntax
     */
    @Override public String toString();
    
    /**
     * @param level the level at which sectionHeading is called, where
     *        the top level = 1
     * @return a string representation of the document
     */
    public String printDocument(int level);
    
    /**
     * Returns whether or not an object is a Document that contains the same
     * document variants in the same order with the same text.
     * @param other - an Object we are comparing to
     * @return if a document contains the same text, and variants in the same order
     */    
    @Override public boolean equals(Object other);
    
    /**
     * returns the hash value of a Docment object
     * @return
     */
    @Override public int hashCode();
    
    
    /**
     * Indicates that a document conversion could not be completed.
     */
    public static class ConversionException extends Exception {
        
        // required by Java because this class implements Serializable
        private static final long serialVersionUID = 1L;
        
        /**
         * Constructs a ConversionException with a detail message.
         * @param message description of the error
         */
        public ConversionException(String message) {
            super(message);
        }
        
        /**
         * Constructs a ConversionException with a detail message and cause.
         * @param message description of the error
         * @param cause exception that caused this error
         */
        public ConversionException(String message, Throwable cause) {
            super(message, cause);
        }
    }



}
