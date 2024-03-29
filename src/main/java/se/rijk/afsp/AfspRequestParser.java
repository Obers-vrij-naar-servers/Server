package se.rijk.afsp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class AfspRequestParser {

    private final Logger LOGGER = LoggerFactory.getLogger(AfspRequestParser.class);


    public AfspRequest parseAfspRequest(InputStream inputStream) throws AfspParsingException {
        LOGGER.info("** Start Parsing Request **");

        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        AfspRequest request = new AfspRequest();

        try {
            parseRequestLine(reader, request);
        } catch (IOException e) {
            throw new AfspParsingException(AfspStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR);
        }

        try {
            AfspHeader.parseHeaders(reader, request);
        } catch (IOException e) {
            throw new AfspParsingException(AfspStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR);
        }

        parseBody(reader, request);

        return request;
    }

    private void parseRequestLine(InputStreamReader reader, AfspRequest request) throws IOException, AfspParsingException {

        boolean methodParsed = false;
        boolean targetParsed = false;

        StringBuilder requestBuffer = new StringBuilder();
        int _byte;
        //Start reading the incoming stream
        while ((_byte = reader.read()) >= 0) {
            if (_byte == ByteCode.CR.code) {
                if (!methodParsed || !targetParsed) {
                    throw new AfspParsingException(AfspStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
                //check for lineFeed;
                _byte = reader.read();
                if (_byte == ByteCode.LF.code) {
                    LOGGER.debug("Request Line to process : {}", requestBuffer.toString());

                    //only support AFSP/1.0
                    if (!requestBuffer.toString().equals(AfspProtocolVersion.AFSP_1_0.toString())) {
                        throw new AfspParsingException(AfspStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    } else {
                        request.setProtocol(requestBuffer.toString());
                        requestBuffer.delete(0, requestBuffer.length());
                    }
                    return;
                } else {
                    throw new AfspParsingException(AfspStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
            }
            if (_byte == ByteCode.SP.code) {
                if (!methodParsed) {
                    LOGGER.debug("Request Line METHOD to process : {}", requestBuffer.toString());
                    request.setMethod(requestBuffer.toString());
                    methodParsed = true;
                } else if (!targetParsed) {
                    LOGGER.debug("Request Line TARGET to process : {}", requestBuffer.toString());
                    request.setRequestTarget(requestBuffer.toString());
                    targetParsed = true;
                } else {
                    throw new AfspParsingException(AfspStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
                requestBuffer.delete(0, requestBuffer.length());

            } else {
                requestBuffer.append((char) _byte);
                if (!methodParsed) {
                    if (requestBuffer.length() > AfspMethod.MAX_LENGTH) {
                        throw new AfspParsingException(AfspStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
                    }
                }
            }
        }

    }

    private void parseBody(InputStreamReader reader, AfspRequest request) {

        //TODO or //REMOVE

    }
}
