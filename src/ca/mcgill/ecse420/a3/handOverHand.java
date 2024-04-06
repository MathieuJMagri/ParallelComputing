package ca.mcgill.ecse420.a3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class handOverHand<T> {
    private Node<T> head;

    private class Node<T> {
        T item;
        int key;
        Node<T> next;
        boolean marked;
        Lock lock = new ReentrantLock();

        Node(T item) {
            this.item = item;
            this.key = (item == null) ? 0 : item.hashCode();
            this.next = null;
            this.marked = false;
        }

        void lock() {
            lock.lock();
        }

        void unlock() {
            lock.unlock();
        }
    }

    public handOverHand() {
        // Initializing the head with a null item, indicating a dummy node
        this.head = new Node<>(null);
        // Same for the tail
        this.head.next = new Node<>(null);
    }

    public boolean contains(T item) {
        // We need to ensure item is not null before calling hashCode
        int key = (item == null) ? 0 : item.hashCode();
        head.lock();
        Node<T> pred = head; // predecessor node
        try {
            Node<T> curr = pred.next;
            curr.lock();
            try {
                while (curr.key < key) {
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                }
                return curr.key == key && !curr.marked;
            } finally {
                if (curr != null) { // Ensure curr is not null before unlocking
                    curr.unlock();
                }
            }
        } finally {
            pred.unlock();
        }
    }
}

