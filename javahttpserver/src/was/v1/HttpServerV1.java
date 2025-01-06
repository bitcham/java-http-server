package was.v1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static java.nio.charset.StandardCharsets.UTF_8;
import static util.MyLogger.log;

public class HttpServerV1 {

    private final int port;

    public HttpServerV1(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        log("Server started at port " + port);

        while(true){
            Socket socket = serverSocket.accept();
            process(socket);
        }
    }

    private void process(Socket socket) throws IOException {
        try (socket;
             BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), UTF_8));
             PrintWriter pw = new PrintWriter(socket.getOutputStream(), false, UTF_8);
        ) {
            String requestString = requestToString(br);
            if(requestString.contains("/favicon.ico")){
                log("favicon requested");
                return;
            }

            log("HTTP request printed below");
            System.out.println(requestString);

            log("HTTP response is creating...");
            sleep(5000);
            responseToClient(pw);
            log("HTTP response is sent");
        }

    }

    private void responseToClient(PrintWriter pw) {

        String body = "<h1>Hello, World!</h1>";
        int length = body.getBytes(UTF_8).length;

        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK\r\n");
        sb.append("Content-Type: text/html\r\n");
        sb.append("Content-Length: ").append(length).append("\r\n");
        sb.append("\r\n"); // header, body 구분 라인
        sb.append(body);

        log("HTTP response printed below");
        System.out.println(sb.toString());
        pw.print(sb.toString());
        pw.flush();
    }

    private void sleep(int mills) {
        try{
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private String requestToString(BufferedReader br) throws IOException{
        StringBuilder request = new StringBuilder();
        String line;
        while((line = br.readLine()) != null){
            if(line.isEmpty()){
                break;
            }
            request.append(line).append("\r\n");
        }
        return request.toString();
    }
}
