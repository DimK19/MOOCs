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
public class CDTest {
    
    private CD cd;
    
    @BeforeEach
    public void setUp() {
        cd = new CD("Thriller", 42, 650, 1982, CD.Genre.POP);
        cd.addArtist("Michael Jackson");
    }
    
    @AfterEach
    public void tearDown() {
    }
    
    @DisplayName("Inheritance")
    @Test
    public void testInheritance() {
        assertTrue(Disk.class.isAssignableFrom(CD.class));
        assertTrue(Disk.class.isSealed());
        assertFalse(CD.class.isSealed());
        assertTrue(Arrays.asList(Disk.class.getPermittedSubclasses()).contains(CD.class));        
    }    

    @DisplayName("genre")
    @Test
    public void testGenre() {
        assertEquals(CD.Genre.POP, cd.getGenre());
        cd.setGenre(CD.Genre.ROCK);
        assertEquals(CD.Genre.ROCK, cd.getGenre());
    }

    @DisplayName("length")
    @Test
    public void testLength() {
        assertEquals(42, cd.getLength());
        
        cd.setLength(43);
        assertEquals(43, cd.getLength());
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cd.setLength(-1);
        });
        assertEquals("CD length should be between 0' and " + CD.MAX_LENGTH + "'.", exception.getMessage());        

        exception = assertThrows(IllegalArgumentException.class, () -> {
            cd.setLength(CD.MAX_LENGTH + 1);
        });
        assertEquals("CD length should be between 0' and " + CD.MAX_LENGTH + "'.", exception.getMessage());        
    }
    
    @DisplayName("publicationYear")
    @Test 
    public void testPublicationYear() {
        assertEquals(1982, cd.getPublicationYear());
        cd.setPublicationYear(1981);
        assertEquals(1981, cd.getPublicationYear());
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cd.setPublicationYear(-1);
        });
        assertEquals("Publication year should be between " + Disk.MIN_PUB_YEAR + " and " + Medium.MAX_PUB_YEAR + '.', exception.getMessage());        
        
        exception = assertThrows(IllegalArgumentException.class, () -> {
            cd.setPublicationYear(Disk.MIN_PUB_YEAR - 1);
        });
        assertEquals("Publication year should be between " + Disk.MIN_PUB_YEAR + " and " + Medium.MAX_PUB_YEAR + '.', exception.getMessage());        

        exception = assertThrows(IllegalArgumentException.class, () -> {
            cd.setPublicationYear(Medium.MAX_PUB_YEAR + 1);
        });
        assertEquals("Publication year should be between " + Disk.MIN_PUB_YEAR + " and " + Medium.MAX_PUB_YEAR + '.', exception.getMessage());          
    }    

    @DisplayName("artists")
    @Test
    public void testArtists() {
        Assertions.assertIterableEquals(Collections.unmodifiableCollection(Set.of("Michael Jackson")), cd.getArtists());
        
        cd.addArtists(Set.of("Janet Jackson", "La Toya Jackson"));
        Collection<String> artists = cd.getArtists();
        assertEquals(3, cd.getArtists().size());
        assertTrue(artists.containsAll(Set.of("Michael Jackson","Janet Jackson", "La Toya Jackson")));
        
        cd.removeArtist("Bee Gees");
        assertEquals(3, cd.getArtists().size());
        assertTrue(artists.containsAll(Set.of("Michael Jackson","Janet Jackson", "La Toya Jackson")));     
        
        cd.removeArtist("Janet Jackson");
        assertEquals(2, cd.getArtists().size());
        assertTrue(artists.containsAll(Set.of("Michael Jackson", "La Toya Jackson")));
        
        cd.removeAllArtists();
        assertTrue(artists.isEmpty());
    }    

    @DisplayName("toShortString")
    @Test
    public void testToShortString() {
        assertEquals("CD {Title: 'Thriller', Artists: [Michael Jackson]}", cd.toShortString());
    }

    @DisplayName("toString")
    @Test
    public void testToString() {
        System.out.println("toString");
        String result = "CD {Genre: POP, Length: 42', Artists: [Michael Jackson], Size: 650 MB , Title: 'Thriller', Publication year: 1982,\n" + "   keywords: []}";
        assertEquals(result, cd.toString());
    }
    
}
