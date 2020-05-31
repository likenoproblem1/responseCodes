package db;

import db.entity.Methods;
import db.entity.ResponseEntity;

import java.util.Optional;

public interface ResponseDAO {

    boolean createResponseRecord(ResponseEntity entity);

    boolean updateResponseRecord(ResponseEntity entity);

    boolean deleteResponseRecord(String url, Methods method);

    Optional<ResponseEntity> getResponseRecord(String url, Methods method);

}
