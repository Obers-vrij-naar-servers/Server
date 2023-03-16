package se.rijk.afspclient;

import ch.qos.logback.core.net.server.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.rijk.afspserver.AfspServer;
import se.rijk.afspserver.config.ConfigurationManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class AfspClient {
    private final static Logger LOGGER = LoggerFactory.getLogger(AfspServer.class);

    public static void main(String[] args) {
        var client = new AfspClient();
        client.run();
    }
    private void run(){

        String host = "localhost";
        int port = 8080;
        try {
            Socket socket = new Socket(host,port);
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            String rawDataString = "LIST / AFSP/1.0\r\n" +
                    "Content-length: 8192\r\n"+
                    "Content-length: 8192\r\n"+
                    "Content-length: 8192\r\n\r\n";
            out.write(rawDataString.getBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
