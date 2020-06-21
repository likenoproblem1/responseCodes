package api.controller;

import db.entity.Methods;

public interface ResponseController {

    ResponseEntity getResponse(String url, Methods method); //todo implement method that will return already created record

}
