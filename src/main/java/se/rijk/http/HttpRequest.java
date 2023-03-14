package se.rijk.http;

public class HttpRequest extends HttpMessage{

    private HttpMethod method;
    private String requestTarget;
    private String protocol;

    HttpRequest(){

    };

    HttpMethod getMethod() {
        return method;
    }

    String getRequestTarget() {
        return requestTarget;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) throws HttpParsingException {
        if (protocol == null || protocol.length() ==0){
            throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR);
        }
        this.protocol = protocol;
    }

    void setMethod(String  methodName) throws HttpParsingException {
        for(HttpMethod _method: HttpMethod.values()){
            if (methodName.equals(_method.name())){
                this.method = _method;
                return;
            }
        }
        throw new HttpParsingException(
                HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED
        );
    }

    public void setRequestTarget(String requestTarget) throws HttpParsingException {
        if(requestTarget == null || requestTarget.length() == 0){
            throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR);
        }
        this.requestTarget = requestTarget;
    }
}
