package gr.cup.mathesis.elibrary.model;

import gr.cup.mathesis.elibrary.util.Validations;
import java.util.Objects;

/**
 * A CD or DVD or other medium with data on it. A {@code Disk} is normally a disk containing data.
 *
 * @author mathesis
 */
// TODO: 2. Something is missing here. Can you fix it?
public sealed class Disk extends Medium permits CD, DVD {

    /** Maximum size of a double sided DVD in MB.*/
    public static final int MAX_SIZE = 9000;
    
    /** Publication year of the first CD. */
    public static final int MIN_PUB_YEAR = 1980;

    protected int size = 0; // in MB, e.g. a CD = 650MB, a DVD = 4500MB

    public Disk(String title, int size, int pubYear) {
        super(title, pubYear);
        setSize(size);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        if (Validations.isValid(size, 0, MAX_SIZE))
            this.size = size;
        else 
            throw new IllegalArgumentException("Disk size should be between 0 and " + MAX_SIZE + " MB.");
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
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.title);
        return hash;
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
        final Disk other = (Disk) obj;
        return this.title.equals(other.title);
    }

    public String toShortString() {
        return "Disk {Title: '" + getTitle() + "'}";
    }

    @Override
    public String toString() {
        return "Disk {Size: " + size + " MB " + super.toString();
    }

}
