package gr.cup.mathesis.elibrary.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;

/**
 * DO NOT MODIFY.
 * 
 * @author mathesis
 */
public class BookTest {
    
    private Book book;
    
    @BeforeEach
    public void setUp() {
        book = new Book(1111111111L, "Δον Κιχώτης", Book.Category.LITERATURE, 200, 1620);
        book.addAuthor("Θερβάντες");
    }
    
    @AfterEach
    public void tearDown() {
    }
    
    @DisplayName("Inheritance")
    @Test
    public void testInheritance() {
        assertTrue(Medium.class.isAssignableFrom(Book.class));
        assertFalse(Book.class.isSealed());
        assertTrue(Medium.class.isSealed());
        assertTrue(Arrays.asList(Medium.class.getPermittedSubclasses()).contains(Book.class));        
    }

    @DisplayName("authors")
    @Test
    public void testAuthors() {
        Assertions.assertIterableEquals(Collections.unmodifiableCollection(Set.of("Θερβάντες")), book.getAuthors());
        
        book.addAuthors(Set.of("Σπαθάρης", "Όσκαρ Ουάιλντ"));
        Collection<String> authors = book.getAuthors();
        assertEquals(3, book.getAuthors().size());
        assertTrue(authors.containsAll(Set.of("Θερβάντες","Σπαθάρης", "Όσκαρ Ουάιλντ")));
        
        book.removeAuthor("Κωστάρας");
        assertEquals(3, book.getAuthors().size());
        assertTrue(authors.containsAll(Set.of("Θερβάντες","Σπαθάρης", "Όσκαρ Ουάιλντ")));       
        
        book.removeAuthor("Σπαθάρης");
        assertEquals(2, book.getAuthors().size());
        assertTrue(authors.containsAll(Set.of("Θερβάντες", "Όσκαρ Ουάιλντ")));
        
        book.removeAllAuthors();
        assertTrue(authors.isEmpty());
    }

    @DisplayName("keywords")
    @Test
    public void testKeywords() {
        book.addKeyword(null);
        assertTrue(book.getKeywords().isEmpty());
        book.addKeyword("   ");
        assertTrue(book.getKeywords().isEmpty());
        book.addKeywords(Set.of("  "));
        assertTrue(book.getKeywords().isEmpty());
        
    }
    
    @DisplayName("category")
    @Test
    public void testCategory() {
        assertEquals(Book.Category.LITERATURE, book.getCategory());
        book.setCategory(Book.Category.HISTORY);
        assertEquals(Book.Category.HISTORY, book.getCategory());
    }

    @DisplayName("isbn")
    @Test
    public void testIsbn() {
        assertEquals(1111111111L, book.getIsbn());
        
        book.setIsbn(1111111112L);
        assertEquals(1111111112L, book.getIsbn());
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            book.setIsbn(0);
        });
        assertEquals("ISBN should be a 10 digit positive number", exception.getMessage());
        
        exception = assertThrows(IllegalArgumentException.class, () -> {
            book.setIsbn(111111);
        });
        assertEquals("ISBN should be a 10 digit positive number", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            book.setIsbn(-1111111111L);
        });
        assertEquals("ISBN should be a 10 digit positive number", exception.getMessage());
        
    }

    @DisplayName("numOfPages")
    @Test
    public void testNumOfPages() {
        assertEquals(200, book.getNumOfPages());
        
        book.setNumOfPages(230);
        assertEquals(230, book.getNumOfPages());
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            book.setNumOfPages(0);
        });
        assertEquals("Number of pages must be >0 and <="+Book.MAX_NUM_OF_PAGES, exception.getMessage());
        
        exception = assertThrows(IllegalArgumentException.class, () -> {
            book.setNumOfPages(-1);
        });
        assertEquals("Number of pages must be >0 and <="+Book.MAX_NUM_OF_PAGES, exception.getMessage());        
        
        exception = assertThrows(IllegalArgumentException.class, () -> {
            book.setNumOfPages(Book.MAX_NUM_OF_PAGES+1);
        });
        assertEquals("Number of pages must be >0 and <="+Book.MAX_NUM_OF_PAGES, exception.getMessage());        
    }
    
    
    @DisplayName("publicationYear")
    @Test 
    public void testPublicationYear() {
        assertEquals(1620, book.getPublicationYear());
        book.setPublicationYear(1630);
        assertEquals(1630, book.getPublicationYear());
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            book.setPublicationYear(-1);
        });
        assertEquals("Publication year should be between " + Medium.MIN_PUB_YEAR + " and " + Medium.MAX_PUB_YEAR + '.', exception.getMessage());        
        exception = assertThrows(IllegalArgumentException.class, () -> {
            book.setPublicationYear(Medium.MIN_PUB_YEAR - 1);
        });
        assertEquals("Publication year should be between " + Medium.MIN_PUB_YEAR + " and " + Medium.MAX_PUB_YEAR + '.', exception.getMessage());        

        exception = assertThrows(IllegalArgumentException.class, () -> {
            book.setPublicationYear(Medium.MAX_PUB_YEAR + 1);
        });
        assertEquals("Publication year should be between " + Medium.MIN_PUB_YEAR + " and " + Medium.MAX_PUB_YEAR + '.', exception.getMessage());          
    }            

    @DisplayName("equals")
    @Test
    public void testEquals() {
        Book otherBook = new Book(1111111111L, "Δον Κιχώτης", Book.Category.LITERATURE, 200, 1620);
        assertEquals(book, otherBook);

        otherBook = new Book(1111111111L, "Pro Apache NetBeans", Book.Category.COMPUTER_SCIENCE, 550, 2022);
        assertEquals(book, otherBook);
    }

    @DisplayName("toShortString")
    @Test
    public void testToShortString() {
        assertEquals("Book {Title: 'Δον Κιχώτης', authors: [Θερβάντες]}", book.toShortString());
    }

    @DisplayName("toString")
    @Test
    public void testToString() {
        String result = "Book {ISBN: 1111111111, category: LITERATURE, pages: 200, authors: [Θερβάντες], Title: 'Δον Κιχώτης', Publication year: 1620,\n   keywords: []}";
        assertEquals(result, book.toString());
    }
    
}
