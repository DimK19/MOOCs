package gr.cup.mathesis.elibrary.model.library;

import gr.cup.mathesis.elibrary.util.ListSet;
import gr.cup.mathesis.elibrary.model.Book;
import static gr.cup.mathesis.elibrary.model.Book.MAX_ISBN;
import static gr.cup.mathesis.elibrary.model.Book.MIN_ISBN;
import gr.cup.mathesis.elibrary.model.CD;
import gr.cup.mathesis.elibrary.model.DVD;
import gr.cup.mathesis.elibrary.model.Disk;
import gr.cup.mathesis.elibrary.model.Medium;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public final class Library implements ILibrary {

    /** Media. */
    private final List<Medium> media = new ListSet<>();
    private static Library INSTANCE;

    private Library() {
    }

    public static Library getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Library();
        }
        return INSTANCE;
    }

    @Override
    public Iterator<Medium> listAll() {
        return media.iterator();
    }

    @Override
    public Book getBookBy(long isbn) {
        if (isInvalidIsbn(isbn))
            return null;
        for (Medium medium : media) {
            if (medium instanceof Book book) {
                if (book.getIsbn() == isbn) {
                    return book;
                }
            }
        }
        return null;
    }

    @Override
    public Set<Medium> findMediaByTitle(String title) {
        // TODO: 6. Implement this method
        Set<Medium> res = new HashSet<>();
        for(Medium i: this.media) {
            if(i.getTitle().toLowerCase().contains(title.toLowerCase()))
                res.add(i);
        }
        return res;
    }
    
    @Override
    public Set<Medium> findMediaByKeyword(String keyword) {
        Set<Medium> found = new HashSet<>();
        for (Medium medium : media) {
            if (keyword != null && !keyword.isEmpty()) {
                Set<String> keywords = medium.getKeywords();
                for (String k : keywords) {
                    if (k.equalsIgnoreCase(keyword.trim())) {
                        found.add(medium);
                    }
                }
            }
        }
        return found;
    }
    
    @Override
    public boolean deleteMedia(Set<Medium> m) {
        // TODO: 5. Implement this method
        return this.media.removeAll(m);
    }

    @Override
    public Book addBook(long isbn, String title, Book.Category cat,
            Set<String> authors, Set<String> keywords, int pages, int pubYear) {
        Book book = new Book(isbn, title, cat, pages, pubYear);
        book.addKeywords(keywords);
        book.addAuthors(authors);
        media.add(book);
        return book;
    }

    @Override
    public Disk addDisk(String title, Set<String> keywords, int size, int pubYear) {
        Disk disk = new Disk(title, size, pubYear);
        disk.addKeywords(keywords);
        media.add(disk);
        return disk;
    }
    
    @Override
    public Set<Book> findBy(Book.Category category) {
        Set<Book> found = new HashSet<>();
        for (Medium medium : media) {
            if (medium instanceof Book book) {
                if (book.getCategory() == category) {
                    found.add(book);
                }
            }
        }
        return found;
    }

    @Override
    public Set<CD> findBy(CD.Genre genre) {
        Set<CD> found = new HashSet<>();
        for (Medium medium : media) {
            if (medium instanceof CD cd) {
                if (cd.getGenre() == genre) {
                    found.add(cd);
                }
            }
        }
        return found;
    }

    @Override
    public Set<DVD> findBy(DVD.Genre genre) {
        // TODO: 4. Implement this method
        Set<DVD> found = new HashSet<>();
        for (Medium medium : media) {
            if (medium instanceof DVD dvd) {
                if (dvd.getGenre() == genre) {
                    found.add(dvd);
                }
            }
        }
        return found;
    }

    @Override
    public CD addMusicCD(String album, int len, int size, int pubYear,
            CD.Genre genre, Set<String> artists, Set<String> keywords) {
        CD cd = new CD(album, len, size, pubYear, genre);
        cd.addArtists(artists);
        cd.addKeywords(keywords);
        media.add(cd);
        return cd;
    }

    @Override
    public DVD addVideoDVD(String title, int len, int size, int pubYear,
            DVD.Genre genre, Set<String> keywords) {
        // TODO: 3. Implement this method
        DVD dvd = new DVD(title, len, size, pubYear, genre);
        dvd.addKeywords(keywords);
        media.add(dvd);
        return dvd;
    }
    
    private static boolean isInvalidIsbn(long isbn) {
        return isbn < MIN_ISBN || isbn > MAX_ISBN;
    }
}
