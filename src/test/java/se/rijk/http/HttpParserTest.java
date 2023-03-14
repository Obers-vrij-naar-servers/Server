package se.rijk.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpParserTest {

    private HttpParser httpParser;

    @BeforeAll
    public void beforeClass() {

        httpParser = new HttpParser();
    }

    @Test
    void parse_GET_HttpRequest() throws HttpParsingException {
        try{
            HttpRequest request = httpParser.parseHttpRequest(generateValidGETTestCase());
            assertEquals(request.getMethod(),HttpMethod.GET);
        } catch (HttpParsingException e){
            fail(e);
        }
    }
    @Test
    void parse_GeT_HttpRequest() throws HttpParsingException {
        try{
            HttpRequest request = httpParser.parseHttpRequest(generateInvalidGETTestCase());
            fail();
        } catch (HttpParsingException e){
            assertEquals(e.getErrorCode(),HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }
    }
    @Test
    void parse_TooLongRequestName_Request() throws HttpParsingException{
        try{
            HttpRequest request = httpParser.parseHttpRequest(generateInvalidRequestTypeTestCase());
            fail();
        } catch (HttpParsingException e){
            assertEquals(e.getErrorCode(),HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }

    }
    @Test
    void parse_InvalidItemsRequestLine_Request() throws HttpParsingException{
        try{
            HttpRequest request = httpParser.parseHttpRequest(generateInvalidRequestItemsTestCase());
            fail();
        } catch (HttpParsingException e){
            assertEquals(e.getErrorCode(),HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }

    }
    @Test
    void parse_EmptyRequestLine_Request() throws HttpParsingException{
        try{
            HttpRequest request = httpParser.parseHttpRequest(generateEmptyRequestLineTestCase());
            fail();
        } catch (HttpParsingException e){
            assertEquals(e.getErrorCode(),HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }

    }
    @Test
    void parse_OnlyCR_NoLF_RequestLine_Request() throws HttpParsingException{
        try{
            HttpRequest request = httpParser.parseHttpRequest(generateOnlyCRnoLFTestCase());
            fail();
        } catch (HttpParsingException e){
            assertEquals(e.getErrorCode(),HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }

    }


    private InputStream generateValidGETTestCase() {
        String rawDataString = "GET /hello.htm HTTP/1.1\r\n" +
                "User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT)\r\n" +
                "Host: www.tutorialspoint.com\r\n" +
                "Accept-Language: en-us\r\n" +
                "Accept-Encoding: gzip, deflate\r\n" +
                "Connection: Keep-Alive";
        InputStream inputStream = new ByteArrayInputStream(rawDataString.getBytes(StandardCharsets.UTF_8));
        return inputStream;
    }
    private InputStream generateInvalidGETTestCase() {
        String rawDataString = "GeT /hello.htm HTTP/1.1\r\n" +
                "User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT)\r\n" +
                "Host: www.tutorialspoint.com\r\n" +
                "Accept-Language: en-us\r\n" +
                "Accept-Encoding: gzip, deflate\r\n" +
                "Connection: Keep-Alive";
        InputStream inputStream = new ByteArrayInputStream(rawDataString.getBytes(StandardCharsets.UTF_8));
        return inputStream;
    }
    private InputStream generateInvalidRequestTypeTestCase() {
        String rawDataString = "GETTETTETT /hello.htm HTTP/1.1\r\n" +
                "User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT)\r\n" +
                "Host: www.tutorialspoint.com\r\n" +
                "Accept-Language: en-us\r\n" +
                "Accept-Encoding: gzip, deflate\r\n" +
                "Connection: Keep-Alive";
        InputStream inputStream = new ByteArrayInputStream(rawDataString.getBytes(StandardCharsets.UTF_8));
        return inputStream;
    }
    private InputStream generateInvalidRequestItemsTestCase() {
        String rawDataString = "GET /first /hello.htm HTTP/1.1\r\n" +
                "User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT)\r\n" +
                "Host: www.tutorialspoint.com\r\n" +
                "Accept-Language: en-us\r\n" +
                "Accept-Encoding: gzip, deflate\r\n" +
                "Connection: Keep-Alive";
        InputStream inputStream = new ByteArrayInputStream(rawDataString.getBytes(StandardCharsets.UTF_8));
        return inputStream;
    }
    private InputStream generateEmptyRequestLineTestCase() {
        String rawDataString = "\r\n" +
                "User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT)\r\n" +
                "Host: www.tutorialspoint.com\r\n" +
                "Accept-Language: en-us\r\n" +
                "Accept-Encoding: gzip, deflate\r\n" +
                "Connection: Keep-Alive";
        InputStream inputStream = new ByteArrayInputStream(rawDataString.getBytes(StandardCharsets.UTF_8));
        return inputStream;
    }
    private InputStream generateOnlyCRnoLFTestCase() {
        String rawDataString = "GET /hello.htm HTTP/1.1\r" + /*no CR*/
                "User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT)\r\n" +
                "Host: www.tutorialspoint.com\r\n" +
                "Accept-Language: en-us\r\n" +
                "Accept-Encoding: gzip, deflate\r\n" +
                "Connection: Keep-Alive";
        InputStream inputStream = new ByteArrayInputStream(rawDataString.getBytes(StandardCharsets.UTF_8));
        return inputStream;
    }
}