public class ClientThread {
    static int excepCount = 0;

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 100; i++) {
            new Thread(new QuizClient()).start();
        }
        Thread.sleep(100000);

    }
}
