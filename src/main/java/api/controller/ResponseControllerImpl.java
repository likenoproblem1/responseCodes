package api.controller;

import db.ResponseDAOImpl;
import db.entity.Methods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ResponseService;
import service.ResponseServiceImpl;

import javax.servlet.http.HttpServlet;
import java.net.HttpURLConnection;
import java.util.Optional;

public class ResponseControllerImpl extends HttpServlet implements ResponseController {

    private static final Logger logger = LoggerFactory.getLogger(BehaviourControllerImpl.class);
    private ResponseService service = new ResponseServiceImpl(new ResponseDAOImpl());

    @Override
    public ResponseEntity getResponse(String url, Methods method) {  // negative scenario?
        logger.info("Get Response with url {} and method {}", url, method.name());
        Optional<db.entity.ResponseEntity> state = service.getResponse(url,method.name());
        return new ResponseEntity(HttpURLConnection.HTTP_OK, state.toString());
    }
}