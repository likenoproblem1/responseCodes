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
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.Optional;

public class ResponseControllerImpl extends HttpServlet implements ResponseController {

    private static final Logger logger = LoggerFactory.getLogger(ResponseControllerImpl.class);
    private ResponseService service = new ResponseServiceImpl(new ResponseDAOImpl());

    @Override
    public ResponseEntity getResponse(String url, Methods method) {  //todo add negative scenario
        logger.info("Get Response with url {} and method {}", url, method.name());
        Optional<db.entity.ResponseEntity> entity = service.getResponse(url, method.name());
        return entity.map(x -> new ResponseEntity(x.getResponseCode(), x.getResponseBody()))
                .orElseGet(() -> new ResponseEntity(HttpURLConnection.HTTP_NOT_FOUND, "Not found"));
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Received behaviour creation request {}", req);
        ResponseEntity response = getResponse(req.getRequestURI(), Methods.GET);
        resp.setStatus(response.getResponseCode());
        PrintWriter writer = resp.getWriter();
        writer.print(response.getResponseBody());
        logger.info("Sent response with {}", response);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Received behaviour creation request {}", req);
        ResponseEntity response = getResponse(req.getRequestURI(), Methods.GET);
        resp.setStatus(response.getResponseCode());
        PrintWriter writer = resp.getWriter();
        writer.print(response.getResponseBody());
        logger.info("Sent response with {}", response);
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Received behaviour creation request {}", req);
        ResponseEntity response = getResponse(req.getRequestURI(), Methods.GET);
        resp.setStatus(response.getResponseCode());
        PrintWriter writer = resp.getWriter();
        writer.print(response.getResponseBody());
        logger.info("Sent response with {}", response);
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Received behaviour creation request {}", req);
        ResponseEntity response = getResponse(req.getRequestURI(), Methods.GET);
        resp.setStatus(response.getResponseCode());
        PrintWriter writer = resp.getWriter();
        writer.print(response.getResponseBody());
        logger.info("Sent response with {}", response);
    }
}
