package se.rijk.afsp;

import java.util.List;

public class AfspResponse {

    private final String protocol = AfspProtocolVersions.AFSP_1_0.toString();
    private int statusCode;
    private String message;
    private List<AfspHeader> headerlist = null;

    public AfspResponse(AfspStatusCode status){
        this.statusCode = status.STATUS_CODE;
        this.message = status.MESSAGE;
    }
    public AfspResponse(AfspStatusCode status, List<AfspHeader> headers){
        this(status);
        this.headerlist = headers;
    }

    @Override
    public String toString() {
        String responseString;
        responseString =  protocol + " " +
                statusCode + " " +
                message + "\r\n";
        if (this.headerlist != null && !this.headerlist.isEmpty()){
            responseString += printHeaders();
        }
        return responseString;
    }


    private String printHeaders(){
        if (headerlist == null || headerlist.isEmpty()) return "";
        String headerString = "";
        for (AfspHeader _header:headerlist){
            headerString += _header.getHeaderType().toString() + ": " + _header.getHeaderContent() +"\r\n";
        }
        return headerString;
    }
}
