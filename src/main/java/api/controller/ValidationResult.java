package api.controller;

import api.ResponseDTO;
import db.entity.Methods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;

public class ValidationResult {

    private static final Logger logger = LoggerFactory.getLogger(ValidationResult.class);


    private static Result isMethodValid(ResponseDTO responseDTO) {
        Result result;
        try {
            Methods.valueOf(responseDTO.getMethod());
            result = new Result(true, Optional.empty());
        } catch (IllegalArgumentException ex) {
            logger.error(ex.getMessage());
            result = new Result(false, Optional.of("Provided method is invalid"));
        }
        return result;
    }

    private static Result isUrlValid(ResponseDTO responseDTO) {
        Result result;
        Pattern pattern = Pattern.compile("(http://|https://)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[a-z]{3}.?([a-z]+)?");
        Matcher matcher = pattern.matcher(responseDTO.getUrl());

        if (responseDTO.getUrl().length() < 255 && matcher.find()) {
            result = new Result(true, Optional.empty());
        } else {
            result = new Result(false, Optional.of("URL is not valid"));
        }

        return result;
    }

    private static Result isBodyValid(ResponseDTO responseDTO) {
        Result result;
        if (responseDTO.getUrl().length() < 255 ) {
            result = new Result(true, Optional.empty());
        } else {
            result = new Result(false, Optional.of("Body is too long"));
        }

        return result;
    }

    public static Optional<Result> isNotValid(ResponseDTO responseDTO) {
        List<Result> results = asList(isMethodValid(responseDTO), isUrlValid(responseDTO), isBodyValid(responseDTO));
        return results.stream().filter(result -> result.message.isPresent()).findFirst();
    }
}

class Result {

    public boolean result;
    public Optional<String> message;

    public Result() {
    }

    public Result(boolean result, Optional<String> message) {
        this.result = result;
        this.message = message;
    }
}