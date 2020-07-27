package db;

import db.entity.Methods;
import db.entity.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Optional;

import static config.ConfigService.getDbConfig;


public class ResponseDAOImpl implements ResponseDAO {
    private static final String CREATE_RESPONSE_QUERY = "INSERT INTO RESPONSES (url,method,response_code,response_body) VALUES (?,?,?,?)";
    private static final String UPDATE_RESPONSE_QUERY = "UPDATE RESPONSES SET response_code = ?, response_body = ?  WHERE url = ? and method = ?";
    private static final String DELETE_RESPONSE_QUERY = "DELETE FROM RESPONSES WHERE url = ? and method = ?";
    private static final String GET_RESPONSE_QUERY = "SELECT * FROM RESPONSES WHERE url = ? and method = ?";
    private static final Logger logger = LoggerFactory.getLogger(ResponseDAOImpl.class);

    private String host;
    private String username;
    private String password;

    public ResponseDAOImpl() {
        this.host = getDbConfig().host;
        this.username = getDbConfig().username;
        this.password = getDbConfig().password;
    }

    public ResponseDAOImpl(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }

    @Override
    public OperationResult createResponseRecord(ResponseEntity entity) {
        logger.info("Create response record with {}", entity);
        OperationResult operationResult;
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_RESPONSE_QUERY)) {
                connection.setAutoCommit(false);
                preparedStatement.setString(1, entity.getUrl());
                preparedStatement.setString(2, entity.getMethod().name());
                preparedStatement.setInt(3, entity.getResponseCode());
                preparedStatement.setString(4, entity.getResponseBody());
                operationResult = preparedStatement.executeUpdate() == 1 ? OperationResult.success() : OperationResult.duplicateEntry(); //todo test this with value 0
                connection.commit();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
                operationResult = OperationResult.failed(e);
                    try {
                    connection.rollback();
                } catch (Exception ex) {
                        logger.error(ex.getMessage());
                }
            }
            return operationResult;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return OperationResult.failed(e);
        }
    }

    @Override
    public OperationResult updateResponseRecord(ResponseEntity entity) {
        logger.info("Update response record with {}", entity);
        OperationResult operationResult;
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_RESPONSE_QUERY)) {
                preparedStatement.setInt(1, entity.getResponseCode());
                preparedStatement.setString(2, entity.getResponseBody());
                preparedStatement.setString(3, entity.getUrl());
                preparedStatement.setString(4, entity.getMethod().name());
                operationResult = preparedStatement.executeUpdate() == 1 ? OperationResult.success() : OperationResult.duplicateEntry();
                connection.commit();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
                operationResult = OperationResult.failed(e);
                try {
                    connection.rollback();
                } catch (Exception ex) {
                    logger.error(ex.getMessage());
                }
            }
            return operationResult;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return OperationResult.failed(e);
        }
    }

    @Override
    public OperationResult deleteResponseRecord(String url, Methods method) {
        logger.info("Delete response record with url:{} and method:{}", url, method);
        try (Connection connection = getConnection()) {
            OperationResult operationResult;
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_RESPONSE_QUERY)) {
                preparedStatement.setString(1, url);
                preparedStatement.setString(2, method.name());
                operationResult = preparedStatement.executeUpdate() == 1 ? OperationResult.success() : OperationResult.duplicateEntry(); //todo add result that record not found
                connection.commit();
            } catch (SQLException e) {
                logger.error(e.getMessage());
                operationResult = OperationResult.failed(e);
                try {
                    connection.rollback();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(),e);
                }
            }
            return operationResult;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return OperationResult.failed(e);
        }
    }

    @Override
    public Optional<ResponseEntity> getResponseRecord(String url, Methods method) {
        try (Connection connection = getConnection()) {
            logger.info("Get response record with url:{} and method:{}", url, method);
            PreparedStatement preparedStatement = connection.prepareStatement(GET_RESPONSE_QUERY);
            preparedStatement.setString(1, url);
            preparedStatement.setString(2, method.name());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String urlResult = resultSet.getString("url");
                String methodResult = resultSet.getString("method");
                int codeResult = resultSet.getInt("response_code");
                String bodyResult = resultSet.getString("response_body");
                return Optional.of(new ResponseEntity(urlResult, codeResult, bodyResult, Methods.valueOf(methodResult)));
            }
            return Optional.empty();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(host, username, password);
    }
}