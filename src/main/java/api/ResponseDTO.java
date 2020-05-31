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

    public void setUrl(String url) {
        this.url = url;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

}
