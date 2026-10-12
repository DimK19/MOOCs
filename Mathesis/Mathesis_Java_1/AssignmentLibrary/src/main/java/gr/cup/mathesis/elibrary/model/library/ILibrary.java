package gr.cup.mathesis.elibrary.model.library;

import gr.cup.mathesis.elibrary.model.Book;
import gr.cup.mathesis.elibrary.model.CD;
import gr.cup.mathesis.elibrary.model.DVD;
import gr.cup.mathesis.elibrary.model.Disk;
import gr.cup.mathesis.elibrary.model.Medium;
import java.util.Iterator;
import java.util.Set;

/**
 * DO NOT MODIFY.
 * 
 * @author mathesis
 */
public interface ILibrary {
    
    /**
     * @return all books in the e-Library
     */
    Iterator<Medium> listAll();
    
    /**
     * @param isbn book's ISBN
     * @return the Book with the provided ISBN
     */
    Book getBookBy(long isbn);
    
    /**
     * Find media by title.
     * 
     * @param title media's title
     * @return the media found
     */
    Set<Medium> findMediaByTitle(String title);

    /**
     * Find media by keyword.
     * 
     * @param keyword media's keyword
     * @return the media found
     */
    Set<Medium> findMediaByKeyword(String keyword);
    
    /**
     * @param category
     * @return all book of the given category 
     */
    Set <Book> findBy(Book.Category category);
    
    /**
     * @param genre
     * @return all DVDs of the given genre
     */
    Set<DVD> findBy(DVD.Genre genre);
    
    /**
     * @param genre
     * @return all CDs of the given genre
     */
    Set<CD> findBy(CD.Genre genre);
    
    /**
     * Delete the book.
     * 
     * @param media set of media
     * @return {@code true} if all media were deleted successfully
     */
    boolean deleteMedia(Set<Medium> media);
    
    /**
     * Add a new book to the library. 
     * 
     * @param isbn book's ISBN
     * @param title book's title
     * @param cat book's category
     * @param authors books' authors
     * @param keywords keywords
     * @param pages number of pages
     * @param pubYear publication year
     * @return the newly created book
     */
    Book addBook(long isbn, String title, Book.Category cat, Set<String> authors, 
            Set<String> keywords, int pages, int pubYear);

    /**
     * Add a new disk to the library. 
     * 
     * @param title disk's title
     * @param keywords keywords
     * @param size in MB
     * @param pubYear publication year
     * @return the new Disk
     */
    Disk addDisk(String title, Set<String> keywords, int size, int pubYear);
    
    /**
     * Add a new CD to the library.
     * 
     * @param title CD title
     * @param len CD length in minutes
     * @param size in MB
     * @param pubYear publication year
     * @param genre {@see CD.Genre}
     * @param artists artists
     * @param keywords keywords
     * @return the new CD
     */
    CD addMusicCD(String title, int len, int size, int pubYear, CD.Genre genre, 
            Set<String> artists, Set<String> keywords);
    
    /**
     * Add a new DVD to the library.
     * 
     * @param title movie title
     * @param len movie length in minutes
     * @param size in MB
     * @param pubYear publication year
     * @param genre {@see DVD.Genre}
     * @param keywords keywords
     * @return the new created DVD object
     */
    DVD addVideoDVD(String title, int len, int size, int pubYear, DVD.Genre genre, Set<String> keywords);
}