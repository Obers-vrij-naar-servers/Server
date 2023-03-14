package se.rijk.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.rijk.simplehttpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpParser {

    private final Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    private static final int SP = 0x20;
    private static final int CR = 0x0D;
    private static final int LF = 0x0A;
    public HttpRequest parseHttpRequest(InputStream inputStream) throws HttpParsingException {
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        HttpRequest request = new HttpRequest();

        try {
            parseRequestLine(reader, request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        parseHeaders(reader, request);
        parseBody(reader,request);

        return request;
    }

    private void parseRequestLine(InputStreamReader reader, HttpRequest request) throws IOException, HttpParsingException {

        boolean methodParsed = false;
        boolean targetParsed = false;

        StringBuilder requestBuffer = new StringBuilder();
        int _byte;
        while((_byte = reader.read()) >= 0){
            if(_byte == CR){
                //check for lineFeed;
                _byte = reader.read();
                if (_byte == LF){
                    LOGGER.debug("Request Line to process : {}",requestBuffer.toString());
                    if(!methodParsed || !targetParsed){
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }
                    if (!requestBuffer.toString().equals("HTTP/1.1")) {
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    } else {
                        request.setProtocol(requestBuffer.toString());
                        requestBuffer.delete(0,requestBuffer.length());
                    }
                    return;
                } else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
            }
            if(_byte == SP){
                if(!methodParsed){
                    LOGGER.debug("Request Line METHOD to process : {}",requestBuffer.toString());
                    request.setMethod(requestBuffer.toString());
                    methodParsed = true;
                } else if(!targetParsed) {
                    LOGGER.debug("Request Line TARGET to process : {}",requestBuffer.toString());
                    request.setRequestTarget(requestBuffer.toString());
                    targetParsed = true;
                } else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
                requestBuffer.delete(0,requestBuffer.length());

            } else {
                requestBuffer.append((char)_byte);
                if(!methodParsed){
                    if(requestBuffer.length() > HttpMethod.MAX_LENGTH){
                        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
                    }
                }
            }
        }

    }
    private void parseHeaders(InputStreamReader reader, HttpRequest request){

    }
    private void parseBody(InputStreamReader reader, HttpRequest request){

    }
}
