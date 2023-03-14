package se.rijk.http;

public enum HttpMethod {
    GET,PUT,POST,DELETE,HEAD;
    public static final int MAX_LENGTH;

    static {
        int _maxValue = -1;
        for (HttpMethod method :values()){
            if (method.name().length() > _maxValue){
                _maxValue = method.name().length();
            }
        }
        MAX_LENGTH = _maxValue;
    }
}