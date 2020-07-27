package unitTests;

import org.junit.jupiter.api.BeforeAll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseUnitTest {

    @BeforeAll
    static void initDB() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:testcase;shutdown=true", "sa", null);
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE Responses (\n" +
                "      url varchar(255) NOT NULL ,\n" +
                "      method varchar(255) \n" +
                "      check (method in ('POST', 'PUT', 'DELETE', 'GET')) ,\n" +
                "      response_code int NOT NULL ,\n" +
                "      response_body VARCHAR(255),\n" +
                "      CONSTRAINT PK_Responses PRIMARY KEY (url,method)\n" +
                "  );");
    }
}
