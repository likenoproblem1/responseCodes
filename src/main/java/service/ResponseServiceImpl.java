package service;

import api.ResponseDTO;
import db.ResponseDAO;
import db.entity.Methods;
import db.entity.ResponseEntity;

import java.util.Optional;

public class ResponseServiceImpl implements ResponseService {

    private final ResponseDAO responseDAO;

    public ResponseServiceImpl(ResponseDAO responseDAO) {
        this.responseDAO = responseDAO;
    }

    @Override
    public boolean createResponse(ResponseDTO dto) {
        return responseDAO.createResponseRecord(ResponseEntity.fromResponseDTO(dto));
    }

    @Override
    public boolean updateResponse(ResponseDTO dto) {
        return responseDAO.updateResponseRecord(ResponseEntity.fromResponseDTO(dto));
    }

    @Override
    public boolean deleteResponse(String url, String method) {
        return responseDAO.deleteResponseRecord(url, Methods.valueOf(method));
    }

    @Override
    public Optional<ResponseEntity> getResponse(String url, String method) {
        return responseDAO.getResponseRecord(url, Methods.valueOf(method));
    }
}
