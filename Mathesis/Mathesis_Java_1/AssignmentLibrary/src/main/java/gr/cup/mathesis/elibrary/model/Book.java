package gr.cup.mathesis.elibrary.model;

import gr.cup.mathesis.elibrary.util.Validations;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class Book extends Medium {

    public enum Category {
        UNCATEGORIZED, MATHEMATICS, COMPUTER_SCIENCE, PHYSICS, CHEMISTRY,
        LITERATURE, SCIENCE, BIOLOGY, HISTORY, ENGINEERING
    }

    public static final int MAX_NUM_OF_PAGES = 2000;
    public static final long MAX_ISBN = 9999999999L;
    public static final long MIN_ISBN = 1000000000L;    
    
    private long isbn;
    private int numOfPages;
    private Category category;
    private final Set<String> authors = new HashSet<>();    

    public Book(long isbn, String title, Category cat, int numOfPages, int pubYear) {
        super(title, pubYear);
        setIsbn(isbn);
        setCategory(cat);
        setNumOfPages(numOfPages);
    }

    public Collection<String> getAuthors() {
        return Collections.unmodifiableCollection(authors);
    }

    public void addAuthor(String author) {
        if (author != null && !author.isBlank())
            this.authors.add(author);
    }

    public void addAuthors(Set<String> authors) {
        if (Validations.isSetValid(authors))
            this.authors.addAll(authors);
    }

    public void removeAuthor(String author) {
        this.authors.remove(author);
    }

    public void removeAllAuthors() {
        this.authors.clear();
    }
    
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public long getIsbn() {
        return isbn;
    }

    public int getNumOfPages() {
        return numOfPages;
    }

    public void setIsbn(long isbn) {
        if (isbn >= MIN_ISBN && isbn <= MAX_ISBN) 
            this.isbn = isbn;
        else 
            throw new IllegalArgumentException("ISBN should be a 10 digit positive number");
    }

    public void setNumOfPages(int numOfPages) {
        if (numOfPages > 0 && numOfPages <= MAX_NUM_OF_PAGES)
            this.numOfPages = numOfPages;
        else 
            throw new IllegalArgumentException("Number of pages must be >0 and <=" + MAX_NUM_OF_PAGES);
    }

    @Override
    public int hashCode() {
        long hash = 5;
        hash = 71 * hash + this.isbn;
        return (int)hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Book other = (Book) obj;
        if (this.isbn != other.isbn) {
            return false;
        }
        return true;
    }

    public String toShortString() {
        return "Book {Title: '" + getTitle() + "', authors: " + authors + '}';
    }

    @Override
    public String toString() {
        return "Book {ISBN: " + getIsbn() + ", category: " + getCategory() + ", pages: " + getNumOfPages() + ", authors: " + authors + super.toString();
    }

}
