package gr.cup.mathesis.elibrary.model;

import gr.cup.mathesis.elibrary.util.Validations;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A music CD. A music CD contains tracks.
 *
 * @author mathesis
 */
public final class CD extends Disk {

    public enum Genre {
        UNCATEGORIZED, POP, ROCK, GREEK; 
    }
    
    /** Maximum length of a music CD in minutes. */
    public static final int MAX_LENGTH = 120;
    /** Maximum size of a CD in MB. */
    public static final int MAX_SIZE = 700;
    
    private int length = 0; // in minutes
    private Genre genre = Genre.UNCATEGORIZED;
    private final Set<String> artists = new HashSet<>();  
    
    public CD(String album, int len, int size, int pubYear) {
        this(album, len, size, pubYear, Genre.UNCATEGORIZED);
    }

    public CD(String album, int len, int size, int pubYear, Genre genre) {
        super(album, size, pubYear);
        setLength(len);
        setSize(size);
        setGenre(genre);
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
    
    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        if (Validations.isValid(length, 0, MAX_LENGTH))
            this.length = length;
        else 
            throw new IllegalArgumentException("CD length should be between 0' and " + MAX_LENGTH + "'.");
    }
    
    @Override
    public void setSize(int size) {
        if (Validations.isValid(size, 0, MAX_SIZE))
            this.size = size;
        else 
            throw new IllegalArgumentException("CD size should be between 0 and " + MAX_SIZE + " MB.");
    }    
    
    public Collection<String> getArtists() {
        return Collections.unmodifiableCollection(artists);
    }

    public void addArtist(String artist) {
        if (artist != null && !artist.isBlank())
            this.artists.add(artist);
    }

    public void addArtists(Set<String> artists) {
        if (Validations.isSetValid(artists))
            this.artists.addAll(artists);
    }

    public void removeArtist(String author) {
        this.artists.remove(author);
    }

    public void removeAllArtists() {
        this.artists.clear();
    }
    
    @Override
    public String toShortString() {
        return "CD {Title: '" + getTitle() + "', Artists: " + getArtists() + '}';
    }

    @Override
    public String toString() {
        return "CD {Genre: " + genre + ", Length: " + getLength() + "', Artists: " + artists + ", " + super.toString().substring(6);
    }

}
