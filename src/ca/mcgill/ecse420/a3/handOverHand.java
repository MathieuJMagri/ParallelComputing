package ca.mcgill.ecse420.a3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HandOverHand<T> {
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

    public HandOverHand() {
        // Initializing the head and the tail with a dummy node (null)
        this.head = new Node<>(null);
        this.head.next = new Node<>(null);
    }

    public static void main(String args[]) {
        System.out.println("HAND-OVER-HAND TESTS:");
        HandOverHand<Integer> list = new HandOverHand<>();

        // Test with an empty list
        boolean result = list.contains(1);
        System.out.println("Test with an empty list (expect false): " + result);

        // Add elements to the list and test
        list.insert(1); // You'll need to implement this method
        list.insert(2); // You'll need to implement this method
        list.insert(3); // You'll need to implement this method

        // Test for an existing element
        result = list.contains(2);
        System.out.println("Test for existing element 2 (expect true): " + result);

        // Test for a non-existing element
        result = list.contains(4);
        System.out.println("Test for non-existing element 4 (expect false): " + result);

        // Test for elements at the boundaries
        result = list.contains(1);
        System.out.println("Test for boundary element 1 (expect true): " + result);
        result = list.contains(3);
        System.out.println("Test for boundary element 3 (expect true): " + result);
    }

    
    // Method that returns true if the argument is contained within the list
    public boolean contains(T item) {
        int key = (item == null) ? 0 : item.hashCode();
        Node<T> pred = null, curr = null;
        head.lock();
        try {
            pred = head;
            curr = pred.next;
            while (curr != null && curr.key < key) {
                curr.lock();
                try {
                    if (pred != head) {
                        pred.unlock();
                    }
                    pred = curr;
                    curr = curr.next;
                } finally {
                    if (curr != pred.next) {
                        curr.unlock();
                    }
                }
            }
            return curr != null && curr.key == key && !curr.marked;
        } finally {
            //release the locks after value is returned
            if (pred != null && pred != head) {
                pred.unlock();
            }
            if (curr != null && curr != pred.next) {
                curr.unlock();
            }
        }
    }

    //INSERT AND REMOVE METHODS (that are by default part of the hand-over-hand algo)
    // Method to insert an item
    public boolean insert(T item) {
        int key = item.hashCode();
        Node<T> pred = null, curr = null;
        head.lock();
        try {
            pred = head;
            curr = pred.next;
            while (curr != null && curr.key < key) {
                curr.lock();
                try {
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                } finally {
                    if (curr != pred.next) {
                        curr.unlock();
                    }
                }
            }
            if (curr == null || curr.key != key) {
                Node<T> newNode = new Node<>(item);
                newNode.next = curr;
                pred.next = newNode;
                return true;
            } else {
                return false;
            }
        } finally {
            pred.unlock();
            if (curr != null && curr != pred.next) {
                curr.unlock();
            }
        }
    }

    // Method to remove an item
    public boolean remove(T item) {
        int key = item.hashCode();
        head.lock();
        Node<T> pred = head;
        try {
            Node<T> curr = pred.next;
            while (curr != null && curr.key < key) {
                curr.lock();
                try {
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                } finally {
                    if (curr != pred.next) {
                        curr.unlock();
                    }
                }
            }
            // Check if the node to remove is present
            if (curr != null && curr.key == key) {
                curr.lock();
                try {
                    // Mark the node and adjust pointers to remove it
                    curr.marked = true;
                    pred.next = curr.next;
                    return true;
                } finally {
                    curr.unlock();
                }
            } else {
                return false;
            }
        } finally {
            pred.unlock();
        }
    }
}

    


