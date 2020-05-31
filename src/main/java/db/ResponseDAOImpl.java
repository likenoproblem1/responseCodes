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

    @Override
    public boolean createResponseRecord(ResponseEntity entity) { // todo add Logger -> Sl4j + logger (Logback ...) and add transactions (commit,rollback strategy)
        logger.info("Create response record with {}", entity);
        try (Connection connection = getConnection()) {
            boolean success;
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_RESPONSE_QUERY)) {
                connection.setAutoCommit(false);
                preparedStatement.setString(1, entity.getUrl());
                preparedStatement.setString(2, entity.getMethod().name());
                preparedStatement.setInt(3, entity.getResponseCode());
                preparedStatement.setString(4, entity.getResponseBody());
                success = preparedStatement.executeUpdate() == 1;
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                success = false;
                try {
                    connection.rollback();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateResponseRecord(ResponseEntity entity) {
        logger.info("Update response record with {}", entity);
        try (Connection connection = getConnection()) {
            boolean success;
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_RESPONSE_QUERY)) {
                preparedStatement.setInt(1, entity.getResponseCode());
                preparedStatement.setString(2, entity.getResponseBody());
                preparedStatement.setString(3, entity.getUrl());
                preparedStatement.setString(4, entity.getMethod().name());
                success = preparedStatement.executeUpdate() == 1;
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                success = false;
                try {
                    connection.rollback();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteResponseRecord(String url, Methods method) {
        logger.info("Delete response record with url:{} and method:{}", url, method);
        try (Connection connection = getConnection()) {
            boolean success;
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_RESPONSE_QUERY)) {
                preparedStatement.setString(1, url);
                preparedStatement.setString(2, method.name());
                success = preparedStatement.executeUpdate() == 1;
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                success = false;
                try {
                    connection.rollback();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
            e.printStackTrace();

            return Optional.empty();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getDbConfig().host, getDbConfig().username, getDbConfig().password);
    }
}