public class Test1 {
    /** Main method */
    public static void main(String[] args) {
        new Test1();
    }

    public Test1() {
        // Create threads
        PrintChar printA = new PrintChar('a', 4);
        PrintChar printB = new PrintChar('b', 4);
        // Start threads
        printA.run();
        printB.run();
    }

    class PrintChar implements Runnable {
        private char charToPrint; // The character to print
        private int times; // The times to repeat

        /**
         * Construct a thread with specified character and number of
         * times to print the character
         */
        public PrintChar(char c, int t) {
            charToPrint = c;
            times = t;
        }

        /**
         * Override the run() method to tell the system
         * what the thread will do
         */
        public void run() {
            for (int i = 0; i < times; i++)
                System.out.print(charToPrint);
        }
    }
}