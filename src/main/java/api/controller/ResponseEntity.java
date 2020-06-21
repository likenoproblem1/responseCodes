package api.controller;

public class ResponseEntity {

    private final int responseCode;

    private final String responseBody;

    public ResponseEntity(int responseCode) {
        this.responseCode = responseCode;
        responseBody = "";
    }

    public ResponseEntity(int responseCode, String responseBody) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

}
