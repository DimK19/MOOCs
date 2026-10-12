package gr.cup.mathesis.elibrary.model;
import gr.cup.mathesis.elibrary.util.Validations;

/**
 * A DVD that contains one or more movies.
 * 
 * TODO: 1. Implement this class
 *
 * @author mathesis
 */
public final class DVD extends Disk {

    public enum Genre {
        UNCATEGORIZED, ACTION, COMEDY, DOCUMENTARY, DRAMA, FANTASY, HORROR, ROMANCE, SCIFI, THRILLER;
    }
    
    /** First DVD. */
    public static final int MIN_PUB_YEAR = 1996;
    /** Maximum length of a DVD in minutes. */
    public static final int MAX_LENGTH = 360;
    
    private int length = 0; // in minutes, range 0-360
    private Genre genre = Genre.UNCATEGORIZED;
    
    public DVD(String title, int length, int size, int publicationYear, Genre genre) {
        super(title, size, publicationYear);
        setLength(length);
        setGenre(genre);
    }
    
    public void setLength(int length) {
        if (Validations.isValid(length, 0, MAX_LENGTH))
            this.length = length;
        else 
            throw new IllegalArgumentException("DVD length should be between 0' and " + MAX_LENGTH + "'.");
    }
    
    public int getLength() {
        return this.length;
    }

    public Genre getGenre() {
        return this.genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
    
    @Override
    public void setPublicationYear(int pubYear) {
        if (Validations.isValid(pubYear, MIN_PUB_YEAR, MAX_PUB_YEAR)) {
            this.publicationYear = pubYear;
        } else {
            throw new IllegalArgumentException("Publication year should be between " + MIN_PUB_YEAR + " and " + MAX_PUB_YEAR + ".");
        }
    }
    
    @Override
    public String toShortString() {
        return "DVD {Title: '" + this.getTitle() + "'}";
    }

    @Override
    public String toString() {
        return "DVD {Genre: " + this.genre + ", Length: "
            + this.length + "', Size: " + this.size + " MB , Title: '" + this.getTitle()
            + "', Publication year: " + this.publicationYear + ",\n   keywords: "
            + this.keywords + "}";
    }
}
