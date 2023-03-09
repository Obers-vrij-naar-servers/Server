package se.rijk.http;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;


public class HttpParserTest {

    private HttpParser httpParser;

    @BeforeAll
    void beforeClass(){
        httpParser = new HttpParser();
    }

    @Test
    public void parseHttpRequest() {
    }
}