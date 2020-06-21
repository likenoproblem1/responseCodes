package api.controller;

import api.ResponseDTO;
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
import java.util.stream.Collectors;

import static util.Serialization.MAPPER;

public class BehaviourControllerImpl extends HttpServlet implements BehaviourController {

    private static final Logger logger = LoggerFactory.getLogger(BehaviourControllerImpl.class);
    private ResponseService service = new ResponseServiceImpl(new ResponseDAOImpl());

    @Override
    public ResponseEntity createBehaviour(ResponseDTO responseDTO) { //todo add all scenarios (422,500,409) for negative cases | 409???
        logger.info("Create Response with DTO {}", responseDTO.toString());
        ResponseEntity responseEntity;
        boolean state = service.createResponse(responseDTO);
        if (state) {
            responseEntity = new ResponseEntity(HttpURLConnection.HTTP_CREATED, "Created");
        } else {
            if (isResponseDTOParamsInvalid(responseDTO)) {
                responseEntity = new ResponseEntity(422, "Unprocessable entity");
            } else {
                responseEntity = new ResponseEntity(HttpURLConnection.HTTP_INTERNAL_ERROR, "ERROR");
            }
        }
        return responseEntity;
    }

    @Override
    public ResponseEntity updateBehaviour(ResponseDTO responseDTO) {
        logger.info("Update Response with DTO {}", responseDTO.toString());
        ResponseEntity responseEntity;
        boolean state = service.updateResponse(responseDTO);
        if (state) {
            responseEntity = new ResponseEntity(HttpURLConnection.HTTP_OK, "OK");
        } else {
            if (isResponseDTOParamsInvalid(responseDTO)) {
                responseEntity = new ResponseEntity(422, "Unprocessable entity");
            } else {
                responseEntity = new ResponseEntity(HttpURLConnection.HTTP_INTERNAL_ERROR, "ERROR");
            }
        }
        return responseEntity;
    }

    @Override
    public ResponseEntity deleteBehaviour(ResponseDTO responseDTO) {
        logger.info("Delete Response with DTO {}", responseDTO.toString());
        ResponseEntity responseEntity;
        boolean state = service.deleteResponse(responseDTO.getUrl(), responseDTO.getMethod());
        if (state) {
            responseEntity = new ResponseEntity(HttpURLConnection.HTTP_NO_CONTENT, "No Content");
        } else {
            if (isResponseDTOParamsInvalid(responseDTO)) {
                responseEntity = new ResponseEntity(422, "Unprocessable entity");
            } else {
                responseEntity = new ResponseEntity(HttpURLConnection.HTTP_INTERNAL_ERROR, "ERROR");
            }
        }
        return responseEntity;
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final long startTime = System.nanoTime();
        logger.info("Received behaviour creation request {}", req);
        ResponseDTO responseDTO = convertServletRequestToResponseDTO(req);
        ResponseEntity behaviour = createBehaviour(responseDTO);
        resp.setStatus(behaviour.getResponseCode());
        PrintWriter writer = resp.getWriter();
        writer.print(behaviour.getResponseBody());
        final long endTime = System.nanoTime();
        logger.info("Created new behaviour in {} ms with resp {}", ((double) (endTime - startTime)) / 1000000, resp);
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    private ResponseDTO convertServletRequestToResponseDTO(HttpServletRequest req) throws IOException {
        String body = req.getReader().lines().collect(Collectors.joining("\n"));
        return MAPPER.readValue(body, ResponseDTO.class);
    }

    private boolean isResponseDTOParamsInvalid(ResponseDTO responseDTO) {
        boolean isMethodValid = true;
        try {
            Methods.valueOf(responseDTO.getMethod());
        } catch (IllegalArgumentException ex) {
            isMethodValid = false;
        }
        return responseDTO.getUrl().length() > 255 || !isMethodValid || (responseDTO.getResponseBody() != null && responseDTO.getResponseBody().length() > 255) || responseDTO.getResponseCode() >= Integer.MAX_VALUE;
    }
}
