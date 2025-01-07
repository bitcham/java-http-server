package was.v2;

public class ServerMainV2 {

    private static final int PORT = 12346;

    public static void main(String[] args) throws Exception {
        HttpServerV2 server = new HttpServerV2(PORT);
        server.start();
    }
}
