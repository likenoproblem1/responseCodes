package service;

import api.ResponseDTO;
import db.entity.ResponseEntity;

import java.util.Optional;

public interface ResponseService {

    boolean createResponse (ResponseDTO dto);

    boolean updateResponse (ResponseDTO dto);

    boolean deleteResponse (String url, String method);

    Optional<ResponseEntity> getResponse (String url, String method);

}
