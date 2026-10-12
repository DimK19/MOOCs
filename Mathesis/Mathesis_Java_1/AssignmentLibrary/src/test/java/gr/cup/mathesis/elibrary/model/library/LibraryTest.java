package gr.cup.mathesis.elibrary.model.library;

import gr.cup.mathesis.elibrary.model.Book;
import gr.cup.mathesis.elibrary.model.CD;
import gr.cup.mathesis.elibrary.model.DVD;
import gr.cup.mathesis.elibrary.model.Disk;
import gr.cup.mathesis.elibrary.model.Medium;
import java.util.Iterator;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;

/**
 *
 * @author mathesis
 */
public class LibraryTest {

    private final ILibrary library = Library.getInstance();
    private Book book;
    private Disk disk;
    private CD cd;
    private DVD dvd;

    @BeforeEach
    public void setUp() {
        book = library.addBook(1111111111L, "Pro Apache NetBeans", Book.Category.COMPUTER_SCIENCE, Set.of("Kostaras I.", "Drabo K.", "Juneau J."), Set.of("Java", "NetBeans", "IDE"), 467, 2020);
        disk = library.addDisk("Knoppix", Set.of("Operating System"), 4140, 2021);
        cd = library.addMusicCD("Ο Δρόμος", 33, 600, 1983, CD.Genre.GREEK, Set.of("Γιάννης Πουλόπουλος"), Set.of("Επιτυχίες"));
        dvd = library.addVideoDVD("Avatar: The way of water", 184, 0, 2022, DVD.Genre.FANTASY, Set.of("Avatar, Aliens"));
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testListAll() {
        System.out.println("listAll");
        assertTrue(library.listAll().hasNext());
    }

    @Test
    public void testGetBookBy() {
        System.out.println("getBookBy");
        assertEquals(book, library.getBookBy(1111111111L));
    }

    @DisplayName("findMediaBy")
    @Test
    public void testFindMediaBy() {
        long isbn = 2222222222L;
        String title = "Δον Κιχώτης";
        Book.Category cat = Book.Category.LITERATURE;
        Set<String> authors = Set.of("Θερβάντες");
        Set<String> keywords = Set.of("Παραμύθι");
        int pages = 230;
        int pubYear = 1605;
        Book fairyTail = library.addBook(isbn, title, cat, authors, keywords, pages, pubYear);

        assertTrue(!library.findMediaByTitle("Knoppix").isEmpty());
        assertEquals(disk, library.findMediaByTitle("Knoppix").iterator().next());
        assertTrue(areEqual(Set.of(book), library.findMediaByTitle("Apache")));
        assertTrue(areEqual(Set.of(book), library.findMediaByKeyword("NetBeans")));
        assertTrue(areEqual(Set.of(fairyTail), library.findBy(Book.Category.LITERATURE)));

    }

    @DisplayName("findBy")
    @Test
    public void testFindBy() {
        Set<CD> greekMusic = library.findBy(CD.Genre.GREEK);
        assertEquals(1, greekMusic.size());
        assertTrue(greekMusic.iterator().hasNext());
        CD greekCD = greekMusic.iterator().next();
        assertEquals(cd, greekCD);
        assertTrue(!cd.getArtists().isEmpty());
        assertEquals("Γιάννης Πουλόπουλος", cd.getArtists().iterator().next());
        assertTrue(!cd.getKeywords().isEmpty());
        assertEquals("Επιτυχίες", cd.getKeywords().iterator().next());
    }

    @DisplayName("musicCD")
    @Test
    public void testMusicCD() {
        String album = "A Night At The Opera";
        Set<String> artists = Set.of("Queen");
        int len = 60;
        int size = 600;
        int pubYear = 1997;
        CD.Genre genre = CD.Genre.ROCK;
        Set<String> keywords = null;
        CD cd = library.addMusicCD(album, len, size, pubYear, genre, artists, keywords);
        Medium medium = library.findMediaByTitle(album).iterator().next();
        assertEquals(medium, cd);
        Iterator<Medium> iter = library.listAll();
        boolean found = false;
        while (iter.hasNext()) {
            medium = iter.next();
            if (medium.equals(cd)) {
                found = true;
            }
        }
        assertTrue(found);
    }

    @DisplayName("videoDVD")
    @Test
    public void testVideoDVD() {
        Set<Medium> media = library.findMediaByTitle("Avatar: The way of water");
        assertTrue(!media.isEmpty());
        Medium medium = library.findMediaByTitle("Avatar: The way of water").iterator().next();        
        assertEquals(medium, dvd);
        Iterator<Medium> iter = library.listAll();
        boolean found = false;
        while (iter.hasNext()) {
            medium = iter.next();
            if (medium.equals(dvd)) {
                found = true;
            }
        }
        assertTrue(found);     
    }    
    
    /**
     * Checks if two sets contain the same elements.
     *
     * @param s1 A Set
     * @param s2 Another Set
     * @return {@code true} if the two sets are equal.
     */
    private boolean areEqual(Set<? extends Medium> s1, Set<? extends Medium> s2) {
        return s1.size() == s2.size() && s1.containsAll(s2) && s2.containsAll(s1);
    }

}
