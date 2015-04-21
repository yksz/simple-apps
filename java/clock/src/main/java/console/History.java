package console;

import java.util.LinkedList;

class History<E> {

    private final LinkedList<E> list = new LinkedList<E>();
    private final int maxSize;
    private int position;

    public History(int maxSize) {
        this.maxSize = maxSize;
    }

    public void add(E e) {
        if (list.size() >= maxSize)
            list.remove();
        list.add(e);
        position = list.size();
    }

    public E undo() {
        if (list.isEmpty())
            return null;
        if (position <= 0)
            return null;
        position--;
        return list.get(position);
    }

    public E redo() {
        if (list.isEmpty())
            return null;
        if (position >= list.size() - 1)
            return null;
        position++;
        return list.get(position);
    }

    public E getLast() {
        if (list.isEmpty())
            return null;
        return list.getLast();
    }

}
