package api;

public class ResponseDTO {

    private String url;

    private int responseCode;

    private String responseBody;

    private String method;

    public ResponseDTO(String url, int responseCode, String responseBody, String method) {
        this.url = url;
        this.responseCode = responseCode;
        this.responseBody = responseBody;
        this.method = method.toUpperCase();
    }

    public ResponseDTO(String url, int responseCode, String method) {
        this.url = url;
        this.responseCode = responseCode;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public ResponseDTO setUrl(String url) {
        this.url = url;
        return this;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public ResponseDTO setResponseCode(int responseCode) {
        this.responseCode = responseCode;
        return this;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public ResponseDTO setResponseBody(String responseBody) {
        this.responseBody = responseBody;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public ResponseDTO setMethod(String method) {
        this.method = method;
        return this;
    }

}
