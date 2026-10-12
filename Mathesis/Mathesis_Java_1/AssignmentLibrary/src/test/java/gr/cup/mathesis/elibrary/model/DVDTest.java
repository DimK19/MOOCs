package gr.cup.mathesis.elibrary.model;

import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;

/**
 * TODO: 9. Uncomment all commented out lines.  All unit tests pass.
 * 
 * @author mathesis
 */
public class DVDTest {

    private DVD dvd;

    @BeforeEach
    public void setUp() {
        dvd = new DVD("Deep Blue", 83, 3000, 2003, DVD.Genre.DOCUMENTARY);
    }

    @AfterEach
    public void tearDown() {
    }

    @DisplayName("inheritance")
    @Test
    public void testInheritance() {
        assertTrue(Disk.class.isAssignableFrom(DVD.class));
        assertTrue(Disk.class.isSealed());
        assertFalse(DVD.class.isSealed());
        assertTrue(Arrays.asList(Disk.class.getPermittedSubclasses()).contains(DVD.class));         
    }    
    
    @Test
    public void testGenre() {
        assertEquals(DVD.Genre.DOCUMENTARY, dvd.getGenre());
        dvd.setGenre(DVD.Genre.UNCATEGORIZED);
        assertEquals(DVD.Genre.UNCATEGORIZED, dvd.getGenre());
    }    

    @DisplayName("length")
    @Test
    public void testLength() {
        assertEquals(83, dvd.getLength());
        
        dvd.setLength(84);
        assertEquals(84, dvd.getLength());
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            dvd.setLength(-1);
        });
        assertEquals("DVD length should be between 0' and " + DVD.MAX_LENGTH + "'.", exception.getMessage());        

        exception = assertThrows(IllegalArgumentException.class, () -> {
            dvd.setLength(DVD.MAX_LENGTH + 1);
        });
        assertEquals("DVD length should be between 0' and " + DVD.MAX_LENGTH + "'.", exception.getMessage());  
    }
    
    @DisplayName("publicationYear")
    @Test 
    public void testPublicationYear() {
        assertEquals(2003, dvd.getPublicationYear());
        dvd.setPublicationYear(2004);
        assertEquals(2004, dvd.getPublicationYear());
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            dvd.setPublicationYear(-1);
        });
        assertEquals("Publication year should be between " + DVD.MIN_PUB_YEAR + " and " + Medium.MAX_PUB_YEAR + '.', exception.getMessage());        
        
        exception = assertThrows(IllegalArgumentException.class, () -> {
            dvd.setPublicationYear(DVD.MIN_PUB_YEAR - 1);
        });
        assertEquals("Publication year should be between " + DVD.MIN_PUB_YEAR + " and " + Medium.MAX_PUB_YEAR + '.', exception.getMessage());        

        exception = assertThrows(IllegalArgumentException.class, () -> {
            dvd.setPublicationYear(Medium.MAX_PUB_YEAR + 1);
        });
        assertEquals("Publication year should be between " + DVD.MIN_PUB_YEAR + " and " + Medium.MAX_PUB_YEAR + '.', exception.getMessage());          
    }        

    @DisplayName("toShortString")
    @Test
    public void testToShortString() {
        assertEquals("DVD {Title: 'Deep Blue'}", dvd.toShortString());
    }

    @DisplayName("toString")
    @Test
    public void testToString() {
        String result = "DVD {Genre: DOCUMENTARY, Length: 83', Size: 3000 MB , Title: 'Deep Blue', Publication year: 2003,\n" + "   keywords: []}";
        assertEquals(result, dvd.toString());
    }

}
