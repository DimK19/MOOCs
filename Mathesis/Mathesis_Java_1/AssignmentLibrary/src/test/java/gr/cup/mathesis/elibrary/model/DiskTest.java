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
 *
 * DO NOT MODIFY.
 * 
 * @author mathesis
 */
public class DiskTest {
    
    private Disk disk;
    
    @BeforeEach
    public void setUp() {
        disk = new Disk("Windows 11", 2300, 2020);
        disk.addKeyword("Operating System");
    }
    
    @AfterEach
    public void tearDown() {
    }
    
    @DisplayName("Inheritance")
    @Test
    public void testInheritance() {
        assertTrue(Medium.class.isAssignableFrom(Disk.class));
        assertTrue(Disk.class.isSealed());
        assertTrue(Medium.class.isSealed());
        assertTrue(Arrays.asList(Medium.class.getPermittedSubclasses()).contains(Disk.class));
    }        

    @DisplayName("size")
    @Test
    public void testSize() {
        assertEquals(2300, disk.getSize());
        disk.setSize(2400);
        assertEquals(2400, disk.getSize());
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            disk.setSize(-1);
        });
        assertEquals("Disk size should be between 0 and " + Disk.MAX_SIZE + " MB.", exception.getMessage());        

        exception = assertThrows(IllegalArgumentException.class, () -> {
            disk.setSize(Disk.MAX_SIZE + 1);
        });
         assertEquals("Disk size should be between 0 and " + Disk.MAX_SIZE + " MB.", exception.getMessage());           
    }
    
    @DisplayName("publicationYear")
    @Test 
    public void testPublicationYear() {
        assertEquals(2020, disk.getPublicationYear());
        disk.setPublicationYear(2021);
        assertEquals(2021, disk.getPublicationYear());
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            disk.setPublicationYear(-1);
        });
        assertEquals("Publication year should be between " + Disk.MIN_PUB_YEAR + " and " + Medium.MAX_PUB_YEAR + '.', exception.getMessage());        
        
        exception = assertThrows(IllegalArgumentException.class, () -> {
            disk.setPublicationYear(Disk.MIN_PUB_YEAR - 1);
        });
        assertEquals("Publication year should be between " + Disk.MIN_PUB_YEAR + " and " + Medium.MAX_PUB_YEAR + '.', exception.getMessage());        

        exception = assertThrows(IllegalArgumentException.class, () -> {
            disk.setPublicationYear(Medium.MAX_PUB_YEAR + 1);
        });
        assertEquals("Publication year should be between " + Disk.MIN_PUB_YEAR + " and " + Medium.MAX_PUB_YEAR + '.', exception.getMessage());          
    }
    
    @DisplayName("keywords")
    @Test
    public void testKeywords() {
        Assertions.assertIterableEquals(Collections.unmodifiableCollection(Set.of("Operating System")), disk.getKeywords());
        
        disk.addKeywords(Set.of("Windows", "Microsoft"));
        Collection<String> keywords = disk.getKeywords();
        assertEquals(3, disk.getKeywords().size());
        assertTrue(keywords.containsAll(Set.of("Operating System","Windows", "Microsoft")));
        
        disk.removeKeyword("Windows 11");
        assertEquals(3, disk.getKeywords().size());
        assertTrue(keywords.containsAll(Set.of("Operating System","Windows", "Microsoft")));   
        
        disk.removeKeyword("Windows");
        assertEquals(2, disk.getKeywords().size());
        assertTrue(keywords.containsAll(Set.of("Operating System", "Microsoft")));
        
        disk.removeAllKeywords();
        assertTrue(keywords.isEmpty());
    }        
    
    @DisplayName("equals")
    @Test
    public void testEquals() {
        Disk otherDisk = new Disk("Windows 11", 2000, 2010);
        assertEquals(disk, otherDisk);
        otherDisk = new Disk("Windows 10", 2300, 2020);
        assertNotEquals(disk, otherDisk);
    }

    @DisplayName("toShortString")
    @Test
    public void testToShortString() {
        assertEquals("Disk {Title: 'Windows 11'}", disk.toShortString());
    }

    @DisplayName("toString")
    @Test
    public void testToString() {
        assertEquals("Disk {Size: 2300 MB , Title: 'Windows 11', Publication year: 2020,\n" + "   keywords: [Operating System]}", disk.toString());
    }
    
}
