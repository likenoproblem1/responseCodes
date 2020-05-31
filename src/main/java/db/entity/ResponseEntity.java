package db.entity;

import api.ResponseDTO;

public class ResponseEntity {

    private final String url;

    private int responseCode;

    private String responseBody;

    private final Methods method;

    public ResponseEntity(String url, int responseCode, String responseBody, Methods method) {
        this.url = url;
        this.responseCode = responseCode;
        this.responseBody = responseBody;
        this.method = method;
    }

    public static ResponseEntity fromResponseDTO(ResponseDTO dto) {
        return new ResponseEntity(dto.getUrl(), dto.getResponseCode(), dto.getResponseBody(), Methods.valueOf(dto.getMethod()));
    }

    public String getUrl() {
        return url;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public Methods getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return "ResponseEntity{" +
                "url='" + url + '\'' +
                ", responseCode=" + responseCode +
                ", responseBody='" + responseBody + '\'' +
                ", method=" + method +
                '}';
    }
}
