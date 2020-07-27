package api.controller;

import api.ResponseDTO;
import db.OperationResult;
import db.ResponseDAOImpl;
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
import java.util.stream.Collectors;

import static api.controller.ValidationResult.isNotValid;
import static util.Serialization.MAPPER;

public class BehaviourControllerImpl extends HttpServlet implements BehaviourController {

    private ResponseService service = new ResponseServiceImpl(new ResponseDAOImpl());
    private static final Logger logger = LoggerFactory.getLogger(BehaviourControllerImpl.class);

    @Override
    public ResponseEntity createBehaviour(ResponseDTO responseDTO) {
        logger.info("Create Response with DTO {}", responseDTO.toString());
        ResponseEntity responseEntity;
        Optional<Result> validationResult = isNotValid(responseDTO);
        if (validationResult.isPresent()) {
            responseEntity = new ResponseEntity(422, String.format("Unprocessable entity:%s", validationResult.get().message));
        } else {
            OperationResult result = service.createResponse(responseDTO);
            if (result.isSuccessful()) {
                responseEntity = new ResponseEntity(HttpURLConnection.HTTP_CREATED, "Created");
            } else if (result.hasDuplicateEntry()) {
                responseEntity = new ResponseEntity(HttpURLConnection.HTTP_CONFLICT, String.format("Record with url:%s and method:%s already exists", responseDTO.getUrl(), responseDTO.getMethod()));
            } else {
                responseEntity = new ResponseEntity(HttpURLConnection.HTTP_INTERNAL_ERROR, "INTERNAL ERROR \n" + result.getError().orElseThrow(() -> new Error("Unknown error")));
            }
        }
        return responseEntity;
    }

    @Override
    public ResponseEntity updateBehaviour(ResponseDTO responseDTO) {
        logger.info("Update Response with DTO {}", responseDTO.toString());
        ResponseEntity responseEntity;
        Optional<Result> validationResult = isNotValid(responseDTO);
        if (validationResult.isPresent()) {
            responseEntity = new ResponseEntity(422, String.format("Unprocessable entity:%s", validationResult.get().message));
        } else {
            OperationResult result = service.updateResponse(responseDTO);
            if (result.isSuccessful()) {
                responseEntity = new ResponseEntity(HttpURLConnection.HTTP_OK, "OK");
            } else if (result.hasDuplicateEntry()) {
                responseEntity = new ResponseEntity(HttpURLConnection.HTTP_CONFLICT, String.format("Record with url:%s and method:%s has duplicate entry", responseDTO.getUrl(), responseDTO.getMethod())); //todo record not exist message
            } else {
                responseEntity = new ResponseEntity(HttpURLConnection.HTTP_INTERNAL_ERROR, "INTERNAL ERROR \n" + result.getError().orElseThrow(() -> new Error("Unknown error")));
            }
        }
        return responseEntity;
    }

    @Override
    public ResponseEntity deleteBehaviour(ResponseDTO responseDTO) {
        logger.info("Delete Response with DTO {}", responseDTO.toString());
        ResponseEntity responseEntity;
        Optional<Result> validationResult = isNotValid(responseDTO);
        if (validationResult.isPresent()) {
            responseEntity = new ResponseEntity(422, String.format("Unprocessable entity:%s", validationResult.get().message));
        } else {
            OperationResult result = service.deleteResponse(responseDTO.getUrl(), responseDTO.getMethod());
            if (result.isSuccessful()) {
                responseEntity = new ResponseEntity(HttpURLConnection.HTTP_NO_CONTENT, "No Content");
            } else if (result.hasDuplicateEntry()) { // todo record not exist???
                responseEntity = new ResponseEntity(HttpURLConnection.HTTP_CONFLICT, String.format("Affected more than 1 record with url:%s and method:%s", responseDTO.getUrl(), responseDTO.getMethod()));
            } else {
                responseEntity = new ResponseEntity(HttpURLConnection.HTTP_INTERNAL_ERROR, "INTERNAL ERROR \n" + result.getError().orElseThrow(() -> new Error("Unknown error")));
            }
        }
        return responseEntity;
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final long startTime = System.nanoTime();
        logger.info("Received behaviour creation request {}", req);
        ResponseDTO responseDTO;
        ResponseEntity behaviour;
        try {
            responseDTO = convertServletRequestToResponseDTO(req);
            behaviour = createBehaviour(responseDTO);
        } catch (IOException e) {
            logger.error(e.getMessage());
            behaviour = new ResponseEntity(HttpURLConnection.HTTP_INTERNAL_ERROR, "Error during parsing\n" + e.getMessage());
        }
        resp.setStatus(behaviour.getResponseCode());
        PrintWriter writer = resp.getWriter();
        writer.print(behaviour.getResponseBody());
        logger.info("Sent response with {}", behaviour);
        final long endTime = System.nanoTime();
        logger.info("Created new behaviour in {} ms with resp {}", ((double) (endTime - startTime)) / 1000000, resp);
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Received behaviour creation request {}", req);
        ResponseDTO responseDTO;
        ResponseEntity behaviour;
        try {
            responseDTO = convertServletRequestToResponseDTO(req);
            behaviour = updateBehaviour(responseDTO);
        } catch (IOException e) {
            logger.error(e.getMessage());
            behaviour = new ResponseEntity(HttpURLConnection.HTTP_INTERNAL_ERROR, "Error during parsing\n" + e.getMessage());
        }
        resp.setStatus(behaviour.getResponseCode());
        PrintWriter writer = resp.getWriter();
        writer.print(behaviour.getResponseBody());
        logger.info("Sent response with {}", behaviour);
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Received behaviour creation request {}", req);
        ResponseDTO responseDTO;
        ResponseEntity behaviour;
        try {
            responseDTO = convertServletRequestToResponseDTO(req);
            behaviour = deleteBehaviour(responseDTO);
        } catch (IOException e) {
            logger.error(e.getMessage());
            behaviour = new ResponseEntity(HttpURLConnection.HTTP_INTERNAL_ERROR, "Error during parsing\n" + e.getMessage());
        }
        resp.setStatus(behaviour.getResponseCode());
        PrintWriter writer = resp.getWriter();
        writer.print(behaviour.getResponseBody());
        logger.info("Sent response with {}", behaviour);
    }

    private ResponseDTO convertServletRequestToResponseDTO(HttpServletRequest req) throws IOException {
        String body = req.getReader().lines().collect(Collectors.joining("\n"));
        return MAPPER.readValue(body, ResponseDTO.class);
    }
}
