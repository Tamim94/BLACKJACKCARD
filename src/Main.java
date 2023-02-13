public class Main implements Runnable {

    private long Time = System.nanoTime();
    public static boolean closed = false; // attribute to close the app
    public static int pWins = 0; // player wins
    public static int dWins = 0; // dealer wins

    //screen refresh rate
    public int Hz = 100;

    GUI gui = new GUI();

    public static void main(String[] args) {
        new Thread(new Main()).start();
    }

    @Override
    public void run() { //while the app is open
        while(closed == false) {
           if (System.nanoTime() - Time >= 100000000/Hz) {
                gui.refresher();
                gui.repaint();
                Time = System.nanoTime();
            }
        }
    }
}

