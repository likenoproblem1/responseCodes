package service;

import api.ResponseDTO;
import db.OperationResult;
import db.entity.ResponseEntity;

import java.util.Optional;

public interface ResponseService {

    OperationResult createResponse (ResponseDTO dto);

    OperationResult updateResponse (ResponseDTO dto);

    OperationResult deleteResponse (String url, String method);

    Optional<ResponseEntity> getResponse (String url, String method);

}
