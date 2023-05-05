package gr.cup.mathesis.elibrary.util;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;

/**
 * A ListSet is a List without duplicates
 * @param <E>
 */
public class ListSet<E> extends LinkedList<E> {

    @Override
    public boolean add(E e) {
        if (this.contains(e)) {
            return false;
        } else {
            return super.add(e);
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        Collection<E> copy = new LinkedHashSet<>(collection);
        copy.removeAll(this);
        return super.addAll(copy);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> collection) {
        Collection<E> copy = new LinkedHashSet<>(collection);
        copy.removeAll(this);
        return super.addAll(index, copy);
    }

    @Override
    public void add(int index, E element) {
        if (!this.contains(element)) {
            super.add(index, element);
        }
    }
}
