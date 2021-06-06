package Heaps;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CircularDoublyLinkedList<T> implements Iterable<T> {

    private ListNode start, end;
    private int size;

    private class ListNode {
        T data;
        private ListNode next, previous;

        public ListNode(T data) {
            this(data, null, null);
        }

        public ListNode(T data, ListNode next, ListNode previous) {
            this.data = data;
            this.next = next;
            this.previous = previous;
        }

        public T getData() {
            return data;
        }

        public ListNode getNext() {
            return next;
        }

        public void setNext(ListNode next) {
            this.next = next;
        }

        public ListNode getPrevious() {
            return previous;
        }

        public void setPrevious(ListNode previous) {
            this.previous = previous;
        }
    }

    public CircularDoublyLinkedList() {
        start = end = null;
        size = 0;
    }

    public void add(T value) {
        ListNode listNode = new ListNode(value);

        ListNode startOriginValue = start;
        insertNode(listNode);

        if (startOriginValue == null) {
            end = start;
        } else {
            end = listNode;
        }
    }

    public void insertAtStart(T value) {
        ListNode listNode = new ListNode(value);

        ListNode startOriginValue = start;
        insertNode(listNode);

        if (startOriginValue == null) {
            end = start;
        } else {
            start = listNode;
        }
    }

    private void insertNode(ListNode value) {
        if (start == null) {
            value.setNext(value);
            value.setPrevious(value);
            start = value;
        } else {
            value.setPrevious(end);
            end.setNext(value);
            start.setPrevious(value);
            value.setNext(start);
        }

        size++;
    }

    public T get(int index) {
        return this.getNode(index).data;
    }

    ListNode getNode(int index) {
        this.checkElementIndex(index);

        ListNode temp;
        int i;

        if (index < this.size >> 1) {
            temp = this.start;

            for(i = 0; i < index; i++) {
                temp = temp.next;
            }
        } else {
            temp = this.end;

            for(i = this.size - 1; i > index; i--) {
                temp = temp.previous;
            }
        }

        return temp;
    }

    public int indexOf(T value) {
        int index = 0;

        if (value == null) {
            for (T buffer : this) {
                if (buffer == null) {
                    return index;
                }

                index++;
            }
        } else {
            for (T buffer : this) {
                if (value.equals(buffer)) {
                    return index;
                }

                index++;
            }
        }

        return -1;
    }

    public int indexOfByAddress(T value) {
        int index = 0;

        if (value == null) {
            for (T buffer : this) {
                if (buffer == null) {
                    return index;
                }

                index++;
            }
        } else {
            for (T buffer : this) {
                if (value == buffer) {
                    return index;
                }

                index++;
            }
        }

        return -1;
    }

    public void delete(int index) {
        this.checkElementIndex(index);

        if (index == 0) {
            if (size == 1) {
                start = null;
                end = null;
                size = 0;
                return;
            }

            start = start.getNext();
            start.setPrevious(end);
            end.setNext(start);
            size--;
            return;

        } else if (index == size - 1) {
            end = end.getPrevious();
            end.setNext(start);
            start.setPrevious(end);
            size--;
            return;
        }

        ListNode temp = nextNode(start);
        int i = 1;
        while (temp != null) {
            if (i == index) {
                ListNode previous = temp.previous;
                ListNode next = temp.next;

                previous.setNext(next);
                next.setPrevious(previous);
                size--;
                return;
            }

            i++;
            temp = nextNode(temp);
        }
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void checkElementIndex(int index) {
        if (!(index >= 0 && index < this.size)) {
            throw new IndexOutOfBoundsException(
                    String.format("Index %d out of bounds for length %d", index, this.size));
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new CircularListIterator();
    }

    public Iterator<T> iterator(T from) {
        return new CircularListIterator(from);
    }

    private class CircularListIterator implements Iterator<T> {

        private ListNode cursor;
        private boolean isMoved;

        public CircularListIterator() {
            this.cursor = start;
            this.isMoved = false;
        }

        public CircularListIterator(T current) {
            int indexOf = indexOf(current);

            if (indexOf != -1) {
                this.cursor = getNode(indexOf);
            } else {
                this.cursor = start;
            }

            this.isMoved = false;
        }

        @Override
        public boolean hasNext() {
            if (isMoved) {
                return cursor.next != start;
            } else {
                return size != 0;
            }
        }

        @Override
        public T next() {
            if (hasNext() && !isMoved) {
                isMoved = true;
                return cursor.getData();
            } else if (hasNext()) {
                cursor = cursor.next;
                return cursor.getData();
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    private ListNode nextNode(ListNode currentNode) {
        if (currentNode.getNext() == start){
            return null;
        } else {
            return currentNode.getNext();
        }
    }

    @Override
    public String toString() {
        StringBuilder list = new StringBuilder();

        list.append('[');

        for (T buffer : this) {
            list.append(buffer).append(',').append(' ');
        }

        if (list.length() > 1) {
            list.delete(list.length() - 2, list.length());
        }

        list.append(']');

        return list.toString();
    }
}