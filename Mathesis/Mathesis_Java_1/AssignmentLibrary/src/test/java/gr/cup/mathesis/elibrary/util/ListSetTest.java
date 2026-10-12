package gr.cup.mathesis.elibrary.util;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author mathesis
 */
public class ListSetTest {
    
    private ListSet<String> listSet;
    
    @BeforeEach
    public void setUp() {
        listSet = new ListSet();
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testAdd_GenericType() {
        System.out.println("add");
        assertTrue(listSet.isEmpty());
        listSet.add("one");
        assertFalse(listSet.isEmpty());
        assertTrue(listSet.contains("one"));
    }

    @Test
    public void testAddAll_Collection() {
        System.out.println("addAll");
        assertTrue(listSet.isEmpty());
        listSet.addAll(List.of("one", "two", "three", "one"));
        assertFalse(listSet.isEmpty());
        assertTrue(listSet.containsAll(List.of("one", "two", "three")));
        assertEquals(3, listSet.size());
        
        listSet.addAll(List.of("one", "two"));
        assertTrue(listSet.containsAll(List.of("one", "two", "three")));
        assertEquals(3, listSet.size());        
    }

    @Test
    public void testAddAll_int_Collection() {
        System.out.println("addAll");
        
        assertTrue(listSet.isEmpty());
        listSet.addAll(List.of("one", "four"));
        assertFalse(listSet.isEmpty());
        assertTrue(listSet.containsAll(List.of("one", "four")));
        assertEquals(2, listSet.size());
        
        listSet.addAll(1,List.of("one", "two", "three", "two"));
        assertTrue(listSet.containsAll(List.of("one", "two", "three", "four")));
        assertEquals(4, listSet.size());        
    }

    @Test
    public void testAdd_int_GenericType() {
        System.out.println("add_int");
        assertTrue(listSet.isEmpty());
        listSet.add("one");
        listSet.add("three");
        listSet.add(1,"two");
        listSet.add(2,"two");
        assertFalse(listSet.isEmpty());
        assertTrue(listSet.containsAll(List.of("one", "two", "three")));
    }
    
}
