package se.rijk.afspserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.rijk.afsp.AfspHeader;
import se.rijk.afsp.AfspRequestParser;
import se.rijk.afsp.AfspRequest;
import se.rijk.common.util.AfspFileHandler;
import se.rijk.common.util.Json;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class AfspConnectionWorkerThread extends Thread {

    private final static Logger LOGGER = LoggerFactory.getLogger(AfspConnectionWorkerThread.class);

    private Socket socket;

    public AfspConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        AfspFileHandler fileHandler = new AfspFileHandler();

        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            int _byte;
            LOGGER.info(" ** AFSP request received **");
//            while ( (_byte = inputStream.read()) >=0) {
//                System.out.print((char) _byte);
//            }


            //TODO read
            AfspRequestParser parser = new AfspRequestParser();
            AfspRequest request = parser.parseAfspRequest(inputStream);
            List<AfspHeader> headerList = request.getHeaderList();
            if (!headerList.isEmpty()) {
                for (AfspHeader _header : headerList) {
                    LOGGER.info("**" + Json.stringify(Json.toJson(_header)) + "**");
                }
            }


            //TODO writing
//            String html = "<html><head><title>Title</title></head><body><h1>hi!</h1></body></html>";
//            final String CRLF = "\n\r"; //13,10 ASCII
//            String response = "HTTP/1.1 200 OK " + CRLF +
//                    "Content-Length: " + html.getBytes().length + CRLF +
//                    CRLF +
//                    html +
//                    CRLF + CRLF;
//
//            outputStream.write(response.getBytes());


            LOGGER.info(" * Connection processing finished...");
        } catch (Exception e) {

            LOGGER.error("Problem with communication", e);
            //TODO handle error
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }

        }
    }

}
