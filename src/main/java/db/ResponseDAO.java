package db;

import db.entity.Methods;
import db.entity.ResponseEntity;

import java.util.Optional;

public interface ResponseDAO {

    OperationResult createResponseRecord(ResponseEntity entity);

    OperationResult updateResponseRecord(ResponseEntity entity);

    OperationResult deleteResponseRecord(String url, Methods method);

    Optional<ResponseEntity> getResponseRecord(String url, Methods method);

}
