public class ClientThread {
    static int excepCount = 0;

    public static void main(String[] args) throws Exception {
        new Thread(new QuizClient()).start();
        
        Thread.sleep(100000);

    }
}
