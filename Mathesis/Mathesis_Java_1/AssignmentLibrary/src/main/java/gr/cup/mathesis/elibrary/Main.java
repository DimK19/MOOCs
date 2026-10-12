package gr.cup.mathesis.elibrary;

import gr.cup.mathesis.elibrary.model.Book;
import gr.cup.mathesis.elibrary.model.CD;
import gr.cup.mathesis.elibrary.model.DVD;
import gr.cup.mathesis.elibrary.model.Disk;
import gr.cup.mathesis.elibrary.model.Medium;
import gr.cup.mathesis.elibrary.model.library.ILibrary;
import gr.cup.mathesis.elibrary.model.library.Library;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * Main class of e-Library.
 * 
 * @author mathesis
 */
public class Main {
    private static final ILibrary LIBRARY = Library.getInstance();
    private static final String PROMPT = "-> ";
    // scanner to allow to press ENTER
    private static final Scanner IN = new Scanner(System.in).useDelimiter("(\\b|\\B)");

    public static void main(String[] args) {
        if (args.length > 1) {
            System.err.println("Usage: java -cp elibrary.jar gr.cup.mathesis.elibrary.Main");
            System.exit(1);
        }

        mainMenu();
    }

    private static void mainMenu() {
        int answer = -1;
        do {
            System.out.println("=========== MENU ===========");
            System.out.println("== 1. List media ===========");
            System.out.println("== 2. Search for a medium ==");
            System.out.println("== 3. Add new book =========");
            System.out.println("== 4. Add new disk =========");
            System.out.println("== 5. Edit medium ==========");
            System.out.println("== 6. Delete medium ========");            
            System.out.println("== 0. Exit =================");
            System.out.println("============================");
            System.out.print(PROMPT);
            try {
                answer = Integer.parseInt(IN.nextLine());
            } catch (NumberFormatException e) {
                answer = -1;
            }
            switch (answer) {
                case 1 -> {
                    System.out.println("== 1. List all media ========= ");
                    Iterator<Medium> allMedia = LIBRARY.listAll();
                    if (!allMedia.hasNext()) {
                        System.out.println("No media found.");
                    } else {
                        listMedia(allMedia);
                    }
                }
                case 2 -> {
                    System.out.println("== 2. Search for media ========= ");
                    searchForMedia();
                }
                case 3 ->
                    addOrEditBook(null);  // 3. Add new book
                case 4 ->
                    diskMenu();  // 4. Add new disk
                case 5 ->
                    editMedium(); // 5. Edit medium
                case 6 -> {                      
                    deleteMedia(); // 6. Delete media
                }                    
                case 0 -> {
                    System.out.println("Bye!");
                    System.exit(0);
                }
            }
        } while (answer != 0);
    }

    // 6. Delete medium
    private static void deleteMedia() {
        // 6. Delete medium
        System.out.println("== 6. Delete Media ==");
        Set<Medium> mediaToDelete = searchForMedia();
        if (!mediaToDelete.isEmpty()) {
            String answer = yesOrNo("Delete (y/n)? ");
            if (answer.equalsIgnoreCase("y")) {
                String feedback = LIBRARY.deleteMedia(mediaToDelete)
                        ? " were deleted successfully" : " were not deleted";
                System.out.println(feedback);
            }
        }
    }
    
    // 1. List all media
    private static void listMedia(Iterator<Medium> media) {
        if (media.hasNext()) {
            int i = 0;
            while (media.hasNext()) {
                Medium m = media.next();
                System.out.println((++i) + ". " + m.toString());
            }
        }
    }    

    // 2. Search for a Book or Disk
    private static Set<Medium> searchForMedia() {
        Set<Medium> foundMedia = new HashSet<>();
        String input = getAnswer("ISBN or title or keyword or '-' to cancel: ");
        try {
            Book book = LIBRARY.getBookBy(Long.parseLong(input));
            if (book != null) {
                foundMedia.add(book);
                System.out.println("1." + book.toString());
            } else {
                System.out.println("No book found.");
            }
        } catch (NumberFormatException nfe) {
            foundMedia = LIBRARY.findMediaByTitle(input);
            foundMedia.addAll(LIBRARY.findMediaByKeyword(input));
            if (foundMedia.isEmpty()) {
                System.out.println("No media found.");
            } else {
                int i = 0;
                for (Medium medium : foundMedia) {
                    System.out.print((++i) + ". ");
                    System.out.println(medium.toString());
                }
            }
        }
        return foundMedia;
    }

    // 3. Add or edit a Book
    private static void addOrEditBook(Book book) {
        if (book == null) {
            System.out.println("== 3. Add New Book ========= ");
            long isbn = getISBN();
            if (isbn != -1) {
                // title
                String title = getAnswer("Title? ");
                Book.Category category = getBookCategory();
                // artists
                Set<String> authors = getAuthors();
                // keywords
                Set<String> keywords = getKeywords();
                // number of pages
                int pages = getNumericalAnswer("Number of pages? ", 0, Book.MAX_NUM_OF_PAGES);
                // publication year
                int publicationYear = getNumericalAnswer("Publication year (yyyy)? ", Medium.MIN_PUB_YEAR, Medium.MAX_PUB_YEAR);

                LIBRARY.addBook(isbn, title, category, authors, keywords, pages, publicationYear);
                System.out.println("== Book added.");
            } 
        } else {
            System.out.println("== 5. Edit Book ========= ");
            // ISBN
            long isbn = book.getIsbn();
            String answer = yesOrNo("ISBN is " + isbn + ". Change (y/n)? ");
            if (answer.equalsIgnoreCase("y")) {
                long newIsbn = getISBN();
                if (newIsbn > -1) {
                    book.setIsbn(newIsbn);
                }
            }
            // Title
            String title = book.getTitle();
            answer = yesOrNo("Title is " + title + ". Change (y/n)? ");
            if (answer.equalsIgnoreCase("y")) {
                title = getTitle();
                book.setTitle(title);
            }
            // Category
            Book.Category category = book.getCategory();
            answer = yesOrNo("Category is " + category + ". Change (y/n)? ");
            if (answer.equalsIgnoreCase("y")) {
                book.setCategory(getBookCategory());
            }
            // Number of pages
            int pages = book.getNumOfPages();
            answer = yesOrNo("Number of pages are " + pages + ". Change (y/n)? ");
            if (answer.equalsIgnoreCase("y")) {
                pages = getNumericalAnswer("Number of pages? ", 0, Book.MAX_NUM_OF_PAGES);
                book.setNumOfPages(pages);
            }
            // artists
            Collection<String> authors = book.getAuthors();
            answer = yesOrNo("Authors: " + authors.toString() + ". Change (y/n)? ");
            if (answer.equalsIgnoreCase("y")) {
                book.removeAllAuthors();
                book.addAuthors(getAuthors());
            }
            // keywords
            Set<String> keywords = book.getKeywords();
            answer = yesOrNo("Keywords: " + keywords.toString() + ". Change (y/n)? ");
            if (answer.equalsIgnoreCase("y")) {
                book.removeAllKeywords();
                book.addKeywords(getKeywords());
            }
            // publication date
            int publicationYear = book.getPublicationYear();
            answer = yesOrNo("Publication year is " + publicationYear + ". Change (y/n)? ");
            if (answer.equalsIgnoreCase("y")) {
                publicationYear = getNumericalAnswer("Publication year (yyyy)? ", Medium.MIN_PUB_YEAR, Medium.MAX_PUB_YEAR);
                book.setPublicationYear(publicationYear);
            }
            System.out.println("== Book updated.");
        }
    }
    
    // 41. Add or edit a Disk
    private static void addOrEditDataDisk(Disk disk) {
        if (disk == null) {
            System.out.println("== 4.1 Add New Disk ========= ");
            // title
            String title = getTitle();
            if (!title.equals("-")) {
                // keywords
                Set<String> keywords = getKeywords();
                // size in MB
                int size = getNumericalAnswer("Size in MB (max: " + Disk.MAX_SIZE + ")? ", 1, Disk.MAX_SIZE);
                // publication year
                int publicationYear = getNumericalAnswer("Publication year (yyyy)? ", Medium.MIN_PUB_YEAR, Medium.MAX_PUB_YEAR);

                LIBRARY.addDisk(title, keywords, size, publicationYear);
                System.out.println("== Disk added.");
            }
        } else {
            System.out.println("== 5. Edit Disk ========= ");
            // Title
            String title = disk.getTitle();
            String answer = yesOrNo("Title is " + title + ". Change (y/n)? ");
            if (answer.equalsIgnoreCase("y")) {
                title = getTitle();
                disk.setTitle(title);
            }
            // keywords
            Set<String> keywords = disk.getKeywords();
            answer = yesOrNo("Keywords: " + keywords.toString() + ". Change (y/n)? ");
            if (answer.equalsIgnoreCase("y")) {
                disk.removeAllKeywords();
                disk.addKeywords(getKeywords());
            }
            // size
            int size = disk.getSize();
            answer = yesOrNo("Size is " + size + "MB. Change (y/n)? ");
            if (answer.equalsIgnoreCase("y")) {
                size = getNumericalAnswer("Size in MB (max: " + Disk.MAX_SIZE + ")? ", 1, Disk.MAX_SIZE);
                disk.setSize(size);
            }            
            // publication date
            int publicationYear = disk.getPublicationYear();
            answer = yesOrNo("Publication year is " + publicationYear + ". Change (y/n)? ");
            if (answer.equalsIgnoreCase("y")) {
                publicationYear = getNumericalAnswer("Publication year (yyyy)? ", Medium.MIN_PUB_YEAR, Medium.MAX_PUB_YEAR);
                disk.setPublicationYear(publicationYear);
            }            
            System.out.println("== Disk updated.");
        }
    }
    
    // 42. Add or Edit a Music CD
    private static void addOrEditCD(CD disk) {
        if (disk == null) {
            System.out.println("== 4.2 Add New Music CD ========= ");
            // title
            String title = getTitle();
            if (!title.equals("-")) {
                // genre
                CD.Genre genre = getCDGenre();
                // keywords
                Set<String> keywords = getKeywords();
                // length in minutes
                int length = getNumericalAnswer("Length in minutes (max: " + CD.MAX_LENGTH + ")?", 0, CD.MAX_LENGTH);
                // size in MB
                int size = getNumericalAnswer("Size in MB (max: " + CD.MAX_SIZE + ")? ", 1, CD.MAX_SIZE);
                // publication year
                int publicationYear = getNumericalAnswer("Publication year (yyyy)? ", Medium.MIN_PUB_YEAR, Medium.MAX_PUB_YEAR);
                // artists
                Set<String> artists = getAuthors();

                LIBRARY.addMusicCD(title, length, size, publicationYear, genre, artists, keywords);
                System.out.println("== Disk added.");
            }
        } else {
            System.out.println("== 5. Edit Music CD ========= ");
            // Title
            String title = disk.getTitle();
            String answer = yesOrNo("Title is " + title + ". Change (y/n)? ");
            if (answer.equalsIgnoreCase("y")) {
                title = getTitle();
                disk.setTitle(title);
            }
            // Genre
            CD.Genre genre = disk.getGenre();
            answer = yesOrNo("Genre is " + genre + ". Change (y/n)? ");
            if (answer.equalsIgnoreCase("y")) {
                disk.setGenre(getCDGenre());
            }
            // artists
            Collection<String> artists = disk.getArtists();
            answer = yesOrNo("Artists: " + artists.toString() + ". Change (y/n)? ");
            if (answer.equalsIgnoreCase("y")) {
                disk.removeAllArtists();
                disk.addArtists(getAuthors());
            }            
            // keywords
            Set<String> keywords = disk.getKeywords();
            answer = yesOrNo("Keywords: " + keywords.toString() + ". Change (y/n)? ");
            if (answer.equalsIgnoreCase("y")) {
                disk.removeAllKeywords();
                disk.addKeywords(getKeywords());
            }
            // length
            int length = disk.getLength();
            answer = yesOrNo("Length is " + length + "'. Change (y/n)? ");
            if (answer.equalsIgnoreCase("y")) {
                length = getNumericalAnswer("Length in minutes (max: " + CD.MAX_LENGTH + ")?", 1, CD.MAX_LENGTH);
                disk.setLength(length);
            }            
            // size
            int size = disk.getSize();
            answer = yesOrNo("Size is " + size + "MB. Change (y/n)? ");
            if (answer.equalsIgnoreCase("y")) {
                size = getNumericalAnswer("Size in MB (max: " + CD.MAX_SIZE + ")? ", 1, CD.MAX_SIZE);
                disk.setSize(size);
            }            
            // publication date
            int publicationYear = disk.getPublicationYear();
            answer = yesOrNo("Publication year is " + publicationYear + ". Change (y/n)? ");
            if (answer.equalsIgnoreCase("y")) {
                publicationYear = getNumericalAnswer("Publication year (yyyy)? ", Medium.MIN_PUB_YEAR, Medium.MAX_PUB_YEAR);
                disk.setPublicationYear(publicationYear);
            }              
            System.out.println("== CD updated.");
        }
    }
   
    // 43. Add or edit a Video DVD
    private static void addOrEditDVD(DVD disk) {
        // TODO: 7. Implement this method
        if (disk == null) {
            System.out.println("== 4.2 Add New DVD ========= ");
            // title
            String title = getTitle();
            if (!title.equals("-")) {
                // genre
                DVD.Genre genre = getDVDGenre();
                // keywords
                Set<String> keywords = getKeywords();
                // length in minutes
                int length = getNumericalAnswer("Length in minutes (max: " + DVD.MAX_LENGTH + ")?", 0, DVD.MAX_LENGTH);
                // size in MB
                int size = getNumericalAnswer("Size in MB (max: " + DVD.MAX_SIZE + ")? ", 1, DVD.MAX_SIZE);
                // publication year
                int publicationYear = getNumericalAnswer("Publication year (yyyy)? ", DVD.MIN_PUB_YEAR, Medium.MAX_PUB_YEAR);

                LIBRARY.addVideoDVD(title, length, size, publicationYear, genre, keywords);
                System.out.println("== Disk added.");
            }
        } else {
            System.out.println("== 5. Edit DVD ========= ");
            // Title
            String title = disk.getTitle();
            String answer = yesOrNo("Title is " + title + ". Change (y/n)? ");
            if (answer.equalsIgnoreCase("y")) {
                title = getTitle();
                disk.setTitle(title);
            }
            // Genre
            DVD.Genre genre = disk.getGenre();
            answer = yesOrNo("Genre is " + genre + ". Change (y/n)? ");
            if (answer.equalsIgnoreCase("y")) {
                disk.setGenre(getDVDGenre());
            }       
            // keywords
            Set<String> keywords = disk.getKeywords();
            answer = yesOrNo("Keywords: " + keywords.toString() + ". Change (y/n)? ");
            if (answer.equalsIgnoreCase("y")) {
                disk.removeAllKeywords();
                disk.addKeywords(getKeywords());
            }
            // length
            int length = disk.getLength();
            answer = yesOrNo("Length is " + length + "'. Change (y/n)? ");
            if (answer.equalsIgnoreCase("y")) {
                length = getNumericalAnswer("Length in minutes (max: " + DVD.MAX_LENGTH + ")?", 1, DVD.MAX_LENGTH);
                disk.setLength(length);
            }            
            // size
            int size = disk.getSize();
            answer = yesOrNo("Size is " + size + "MB. Change (y/n)? ");
            if (answer.equalsIgnoreCase("y")) {
                size = getNumericalAnswer("Size in MB (max: " + DVD.MAX_SIZE + ")? ", 1, DVD.MAX_SIZE);
                disk.setSize(size);
            }            
            // publication date
            int publicationYear = disk.getPublicationYear();
            answer = yesOrNo("Publication year is " + publicationYear + ". Change (y/n)? ");
            if (answer.equalsIgnoreCase("y")) {
                publicationYear = getNumericalAnswer("Publication year (yyyy)? ", DVD.MIN_PUB_YEAR, Medium.MAX_PUB_YEAR);
                disk.setPublicationYear(publicationYear);
            }              
            System.out.println("== DVD updated.");
        }
    }
    
    // 5. Edit medium
    private static void editMedium() {
        System.out.println("== 5. Edit Medium == ");
        Set<Medium> mediaFound = searchForMedia();
        if (!mediaFound.isEmpty()) {
            for (Medium medium : mediaFound) {
                if (medium instanceof Book book) {
                    addOrEditBook(book);
                } else if (medium instanceof CD cd) {
                    addOrEditCD(cd);
                // TODO: 8. Uncommment the following 2 lines and fix errors
                } else if (medium instanceof DVD dvd) {
                    addOrEditDVD(dvd);
                } else if (medium instanceof Disk disk) {
                    addOrEditDataDisk(disk);
                } else {
                    throw new MediumNotFoundException();
                }
            }
        }
    }

    /**
     * The sub-mainMenu when the operator selects Disk.
     */
    private static void diskMenu() {
        int answer = -1;
        do {
            System.out.println("====== 4. DISK MENU ========");
            System.out.println("== 1. Add new data disk ===");
            System.out.println("== 2. Add new Music CD ====");
            System.out.println("== 3. Add new Video DVD ===");
            System.out.println("== 0. Back ================");
            System.out.println("============================");
            System.out.print(PROMPT);
            try {
                answer = Integer.parseInt(IN.nextLine());
            } catch (NumberFormatException e) {
                answer = -1;
            }
            switch (answer) {
                case 1 ->
                    addOrEditDataDisk(null);
                case 2 ->
                    addOrEditCD(null);
                case 3 ->
                    addOrEditDVD(null);
                case 0 -> {
                    mainMenu();
                }

            }
        } while (answer != 0);
    }
    
    //~~~~~~~~~~~~~~~~~~~~~ Convenient methods ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private static String getTitle() {
        Set<Medium> media;
        String title = "";
        do {
            title = getAnswer("title (or -)? ");
            if (title.equals("-")) break;
            media = LIBRARY.findMediaByTitle(title);
            if (media != null && !media.isEmpty()) {
                System.out.println("One of more media already exist in the library with title: " + title);
            }
        } while (media != null && !media.isEmpty());
        return title;
    }

    private static Set<String> getKeywords() {
        // keywords
        String keywordsAsString = getAnswer("Keywords (separated by ,) or -? ");
        if (keywordsAsString.equals("-")) return new HashSet<>();
        String[] keywordsArray = keywordsAsString.split(", ");
        Set<String> keywords = new HashSet<>(Arrays.asList(keywordsArray));
        return keywords;
    }
    
    private static CD.Genre getCDGenre() {
        String genrePrompt = "Genre (0.UNCATEGORIZED, 1.POP, 2.ROCK, 3.GREEK)?";
        int gen = getNumericalAnswer(genrePrompt, 0, 3);
        CD.Genre[] genres = CD.Genre.values();
        CD.Genre genre = gen >= 0 && gen <= 3 ? genres[gen] : genres[0];
        return genre;
    }    
    
    private static DVD.Genre getDVDGenre() {
        String genrePrompt = """
                           Genre (0.UNCATEGORIZED, 1.ACTION, 2.COMEDY, 
                             3.DOCUMENTARY, 4.DRAMA, 5.FANTASY, 6.HORROR, 
                             7.ROMANCE, 8.SCIFI, 9.THRILLER)? """;
        DVD.Genre[] genres = DVD.Genre.values();
        int gen = getNumericalAnswer(genrePrompt, 0, genres.length - 1);
        return gen >= 0 && gen <= genres.length - 1 ? genres[gen] : genres[0];
    }
    
    private static long getISBN() {
        Book book;
        long isbn = -1;
        do {
            isbn = getNumericalAnswer("ISBN (10 digits) or -? ", 1000000000L, 9999999999L);
            if (isbn == -1) break;
            book = LIBRARY.getBookBy(isbn);
            if (book != null) {
                System.out.println("A book already exists in the library with ISBN: " + isbn);
            }
        } while (book != null);
        return isbn;
    }    
    
    private static Set<String> getAuthors() {
        String authorsAsString = getAnswer("Authors/artists (separated by ,)? ");
        String[] authorsArray = authorsAsString.split(", ");
        return new HashSet<>(Arrays.asList(authorsArray));
    }

    private static Book.Category getBookCategory() {
        String catPrompt = """
            Category (0.UNCATEGORIZED, 1.MATHEMATICS, 2.COMPUTER_SCIENCE, 3.PHYSICS,
            4.CHEMISTRY, 5.LITERATURE, 6.SCIENCE, 7.BIOLOGY, 8.HISTORY, 9.ENGINEERING)? """;
        Book.Category[] categories = Book.Category.values();
        int cat = getNumericalAnswer(catPrompt, 0, categories.length - 1);
        return cat >= 0 && cat <= categories.length - 1 ? categories[cat] : categories[0];
    }
    
    //~~~~~~~~~~~~~~~~~~~~~ Utility methods ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Prompts the user for a number. {@code 0} means back to main mainMenu
     *
     * @param prompt prompt to use
     * @param lowLimit lower valid menuChoice or 0 default
     * @param upperLimit upper valid menuChoice or -1 for {@code Integer.MAX_VALUE}
     * @return the numerical input by the user; {@code 0} means back to main
 mainMenu
     */
    private static int getNumericalAnswer(String prompt, int lowLimit, int upperLimit) {
        if (upperLimit == -1 || upperLimit <= lowLimit) {
            upperLimit = Integer.MAX_VALUE;
        }
        int numericalAnswer = -1;
        do {
            System.out.print(prompt);
            try {
                numericalAnswer = Integer.parseInt(IN.nextLine());
            } catch (NumberFormatException e) {
                numericalAnswer = -1;
            }
        } while (numericalAnswer < lowLimit || numericalAnswer > upperLimit);
        return numericalAnswer;
    }
    
    /**
     * Prompts the user for a number. {@code 0} means back to main mainMenu
     *
     * @param prompt prompt to use
     * @param lowLimit lower valid menuChoice or 0 default
     * @param upperLimit upper valid menuChoice or -1 for {@code Long.MAX_VALUE}
     * @return the numerical input by the user; {@code 0} means back to main
 mainMenu
     */
    private static long getNumericalAnswer(String prompt, long lowLimit, long upperLimit) {
        if (upperLimit == -1 || upperLimit <= lowLimit) {
            upperLimit = Long.MAX_VALUE;
        }
        long numericalAnswer = -1;
        do {
            System.out.print(prompt);
            try {
                final String input = IN.nextLine();
                if (input.equals("-")) break;
                numericalAnswer = Long.parseLong(input);
            } catch (NumberFormatException e) {
                numericalAnswer = -1;
            }
        } while (numericalAnswer < lowLimit || numericalAnswer > upperLimit);
        return numericalAnswer;
    }

    /**
     * Prompts the user for an answer.
     *
     * @param prompt prompt to use
     * @return the answer the user provided
     */
    private static String getAnswer(String prompt) {
        String answer;
        do {
            System.out.print(prompt);
            answer = IN.nextLine();
        } while (answer == null || answer.isEmpty());
        return answer;
    }

    private static String yesOrNo(String prompt) {
        String answer;
        do {
            System.out.print(prompt);
            answer = IN.nextLine();
        } while (answer == null || answer.isEmpty()
                || !(answer.equalsIgnoreCase("y") 
                || answer.equalsIgnoreCase("n")));
        return answer;
    }

}
