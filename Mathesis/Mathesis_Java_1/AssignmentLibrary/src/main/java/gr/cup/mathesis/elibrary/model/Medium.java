package gr.cup.mathesis.elibrary.model;

import gr.cup.mathesis.elibrary.util.Validations;
import java.time.YearMonth;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A medium to be stored, i.e. a {@code Book} or a {@code Disk}.
 *
 * @author mathesis
 */
public abstract sealed class Medium permits Book, Disk {

    /** Publication year of first book. */
    public static final int MIN_PUB_YEAR = 1455;
    public static final int MAX_PUB_YEAR = YearMonth.now().getYear() + 1;

    protected String title;
    protected final Set<String> keywords = new HashSet<>();
    protected int publicationYear;

    Medium(String title, int publicationYear) {
        setTitle(title);
        setPublicationYear(publicationYear);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (Validations.isNotEmpty(title)) {
            this.title = title;
        }
    }

    public Set<String> getKeywords() {
        return Collections.unmodifiableSet(keywords);
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void addKeyword(String keyword) {
        if (Validations.isNotEmpty(keyword)) {        
            this.keywords.add(keyword);
        }
    }

    public void addKeywords(Set<String> keywords) {
        if (Validations.isSetValid(keywords))
            this.keywords.addAll(keywords);
    }

    public void removeKeyword(String keyword) {
        this.keywords.remove(keyword);
    }

    public void removeAllKeywords() {
        this.keywords.clear();
    }

    public void setPublicationYear(int pubYear) {
        if (Validations.isValid(pubYear, MIN_PUB_YEAR, MAX_PUB_YEAR)) {
            this.publicationYear = pubYear;
        } else {
            throw new IllegalArgumentException("Publication year should be between " + MIN_PUB_YEAR + " and " + MAX_PUB_YEAR + ".");
        }
    }

    @Override
    public String toString() {
        return ", Title: '" + title + "', Publication year: " + publicationYear + ",\n   keywords: " + keywords + '}';
    }

}
