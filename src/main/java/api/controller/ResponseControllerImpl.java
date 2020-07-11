package api.controller;

import db.ResponseDAOImpl;
import db.entity.Methods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ResponseService;
import service.ResponseServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Optional;

public class ResponseControllerImpl extends HttpServlet implements ResponseController {

    private static final Logger logger = LoggerFactory.getLogger(ResponseControllerImpl.class);
    private ResponseService service = new ResponseServiceImpl(new ResponseDAOImpl());

    @Override
    public ResponseEntity getResponse(String url, Methods method) {  //todo add negative scenario
        logger.info("Get Response with url {} and method {}", url, method.name());
        Optional<db.entity.ResponseEntity> state = service.getResponse(url, method.name());
        return new ResponseEntity(HttpURLConnection.HTTP_OK, state.toString()); //todo add method and body, override doPost and etc., implement for all methods
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Received behaviour creation request {}", req);

        logger.info("Sent response with {}");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Received behaviour creation request {}", req);

        logger.info("Sent response with {}");
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Received behaviour creation request {}", req);

        logger.info("Sent response with {}");
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Received behaviour creation request {}", req);

        logger.info("Sent response with {}");
    }
    //getResponse call ()
}
