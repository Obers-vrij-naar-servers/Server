package se.rijk.afsp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AfspRequestParser {

    private final Logger LOGGER = LoggerFactory.getLogger(AfspRequestParser.class);

    private static final int SP = 0x20;
    private static final int CR = 0x0D;
    private static final int LF = 0x0A;

    private static final int COL = 0x3A;

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
            parseHeaders(reader, request);
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
            if (_byte == CR) {
                //check for lineFeed;
                _byte = reader.read();
                if (_byte == LF) {
                    LOGGER.debug("Request Line to process : {}", requestBuffer.toString());
                    if (!methodParsed || !targetParsed) {
                        throw new AfspParsingException(AfspStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }
                    //only support AFSP/1.0
                    if (!requestBuffer.toString().equals(AfspProtocolVersions.AFSP_1_0.toString())) {
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
            if (_byte == SP) {
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

    private void parseHeaders(InputStreamReader reader, AfspRequest request) throws IOException, AfspParsingException {
        LOGGER.debug(" ** Parsing Headers ** ");
        List<AfspHeader> headerList = new ArrayList<>();
        StringBuilder requestBuffer = new StringBuilder();
        int _byte;
        boolean crlfFound = false;
        //Start reading the incoming stream
        while ((_byte = reader.read()) >= 0) {
            if (_byte == CR) {
                //check for lineFeed;
                _byte = reader.read();
                if (_byte == LF) {
                    //check for double CRLF
                    _byte = reader.read();
                    if (_byte == CR) {
                        _byte = reader.read();
                        //end of headers, save headerList to request and exit parsing headers
                        if (_byte == LF) {
                            headerList.get(headerList.size() - 1).setHeaderContent(requestBuffer.toString());
                            requestBuffer.delete(0, requestBuffer.length());
                            request.setHeaderList(headerList);
                            return;
                        }
                    } else  // save header to local list{
                        headerList.get(headerList.size() - 1).setHeaderContent(requestBuffer.toString());
                        requestBuffer.delete(0, requestBuffer.length());
                    }
                }

            //validate HeaderType
            if (_byte == COL) {
                String currentHeaderType = requestBuffer.toString();
                boolean validHeaderType = false;
                for (AfspHeader.HeaderType _headerType : AfspHeader.HeaderType.values()) {
                    if (_headerType.toString().equals(currentHeaderType)) {
                        validHeaderType = true;
                        requestBuffer.delete(0, requestBuffer.length());
                        break;
                    }
                }
                if (!validHeaderType) {
                    LOGGER.debug(" ** INVALID HEADER : " + currentHeaderType + " ** ");
                    throw new AfspParsingException(AfspStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
                headerList.add(new AfspHeader(currentHeaderType));
            } else {
                requestBuffer.append((char) _byte);
            }
        }
    }

    private void parseBody(InputStreamReader reader, AfspRequest request) {

    }
}
