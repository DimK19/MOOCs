package gr.cup.mathesis.elibrary;

/**
 *
 * @author mathesis
 */
public class MediumNotFoundException extends RuntimeException {

    public MediumNotFoundException() {
        super();
    }
    
    public MediumNotFoundException(String message) {
        super(message);
    }
}
