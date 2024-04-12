public class Main {
    public static void main(String[] args) {
        int clients = 0;

        while (clients < 10) {
            Thread t = new Thread(new Client());
            t.start();
            clients++;
        }
    }
}