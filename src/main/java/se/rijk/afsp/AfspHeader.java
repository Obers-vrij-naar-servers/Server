package se.rijk.afsp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AfspHeader {
    private final Logger LOGGER = LoggerFactory.getLogger(AfspHeader.class);

    private HeaderType headerType;
    private String headerContent;

    AfspHeader(HeaderType headerType) {
        this.headerType = headerType;
    }
    AfspHeader(String headerName) throws AfspParsingException{
        for(HeaderType _headerType: HeaderType.values()){
            if(headerName.equals(_headerType.toString())){
                this.headerType = _headerType;
                return;
            }
        }
        throw new AfspParsingException(
                AfspStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR
        );

    }

    public HeaderType getHeaderType(){
        return headerType;
    }

    public String getHeaderContent() {

        return headerContent;
    }

    public void setHeaderContent(String headerContent) {
        this.headerContent = headerContent;
    }

    enum HeaderType {
        CONTENT_LENGTH{
            @Override
            public String toString() {
                return "Content-length";
            }
        },
        CHARSET{
            @Override
            public String toString(){
                return "Charset";
            }
        },
        BUFFER_SIZE{
            @Override
            public String toString() {
                return "Buffer-Size";
            }
        },
        TIME_OUT{
            @Override
            public String toString() {
                return "Time-out";
            }
        },
        FILE_SIZE{
            @Override
            public String toString() {
                return "File-Size";
            }
        },
        IDENTIFIER{
            @Override
            public String toString() {
                return "Identifier";
            }
        }
    }
}
