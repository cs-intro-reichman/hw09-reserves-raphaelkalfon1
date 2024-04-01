/** A linked list of character data objects.
 *  (Actually, a list of Node objects, each holding a reference to a character data object.
 *  However, users of this class are not aware of the Node objects. As far as they are concerned,
 *  the class represents a list of CharData objects. Likwise, the API of the class does not
 *  mention the existence of the Node objects). */
public class List {

    private Node first;
    private int size;

    public List() {
        first = null;
        size = 0;
    }

    public int getSize() {
        return size;
    }

    public CharData getFirst() {
        if (first != null) {
            return first.charData;
        } else {
            return null;
        }
    }

    public void addFirst(char chr) {
        CharData newCharData = new CharData(chr);
        Node newNode = new Node(newCharData);
        newNode.next = first;
        first = newNode;
        size++;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node current = first;
        while (current != null) {
            sb.append(current.charData.toString());
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        return sb.toString();
    }

    public int indexOf(char chr) {
        Node current = first;
        int index = 0;
        while (current != null) {
            if (current.charData.getChar() == chr) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    public void update(char chr) {
        int index = indexOf(chr);
        if (index != -1) {
            Node current = first;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            current.charData.incrementCount();
        } else {
            addFirst(chr);
            first.charData.incrementCount();
        }
    }

    public boolean remove(char chr) {
        if (first == null) {
            return false;
        }
        if (first.charData.getChar() == chr) {
            first = first.next;
            size--;
            return true;
        }
        Node current = first;
        while (current.next != null) {
            if (current.next.charData.getChar() == chr) {
                current.next = current.next.next;
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public CharData get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        Node current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.charData;
    }

    public CharData[] toArray() {
        CharData[] arr = new CharData[size];
        Node current = first;
        int i = 0;
        while (current != null) {
            arr[i++] = current.charData;
            current = current.next;
        }
        return arr;
    }

    public ListIterator listIterator(int index) {
        if (size == 0) {
            return null;
        }
        Node current = first;
        int i = 0;
        while (i < index) {
            current = current.next;
            i++;
        }
        return new ListIterator(current);
    }

    private class Node {
        private CharData charData;
        private Node next;

        public Node(CharData charData) {
            this.charData = charData;
            this.next = null;
        }
    }
}

