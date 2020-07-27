package unitTests;

import api.ResponseDTO;
import com.github.javafaker.Faker;
import db.ResponseDAOImpl;
import db.entity.Methods;
import db.entity.ResponseEntity;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import service.ResponseService;
import service.ResponseServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UnitTests extends BaseUnitTest {

    private ResponseService service = new ResponseServiceImpl(new ResponseDAOImpl("jdbc:hsqldb:mem:testcase;shutdown=true", "sa", null));
    private Faker faker = new Faker();

    static {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @EnumSource(Methods.class)
    public void createRecordTest(Methods method) {
        ResponseDTO responseDTO = new ResponseDTO(faker.animal().name(), faker.random().nextInt(600), faker.gameOfThrones().character(), method.name());
        assertTrue(service.createResponse(responseDTO).isSuccessful());
        assertNotEquals(service.getResponse(responseDTO.getUrl(), responseDTO.getMethod()), Optional.empty());
        service.deleteResponse(responseDTO.getUrl(), responseDTO.getMethod());
    }

    @ParameterizedTest
    @EnumSource(Methods.class)
    public void createSameRecordTest(Methods method) {
        ResponseDTO responseDTO = new ResponseDTO(faker.animal().name(), faker.random().nextInt(600), faker.gameOfThrones().character(), method.name());
        assertTrue(service.createResponse(responseDTO).isSuccessful());
        assertNotEquals(service.getResponse(responseDTO.getUrl(), responseDTO.getMethod()), Optional.empty());
        assertFalse(service.createResponse(responseDTO).isSuccessful());
        service.deleteResponse(responseDTO.getUrl(), responseDTO.getMethod());
    }

    @ParameterizedTest
    @EnumSource(Methods.class)
    public void updateRecordTest(Methods method) {
        ResponseDTO responseDTO = new ResponseDTO(faker.animal().name(), faker.random().nextInt(600), faker.gameOfThrones().character(), method.name());
        ResponseDTO updateDTO = new ResponseDTO(responseDTO.getUrl(), faker.random().nextInt(600), faker.gameOfThrones().character(), responseDTO.getMethod());
        assertTrue(service.createResponse(responseDTO).isSuccessful());
        assertTrue(service.updateResponse(updateDTO).isSuccessful());
        Optional<ResponseEntity> response = service.getResponse(responseDTO.getUrl(), responseDTO.getMethod());
        assertTrue(response.isPresent());
        assertEquals(response.get().getResponseCode(), updateDTO.getResponseCode());
        assertEquals(response.get().getResponseBody(), updateDTO.getResponseBody());
        service.deleteResponse(responseDTO.getUrl(), responseDTO.getMethod());
    }

    @ParameterizedTest
    @EnumSource(Methods.class)
    public void deleteRecordTest(Methods method) {
        ResponseDTO responseDTO = new ResponseDTO(faker.animal().name(), faker.random().nextInt(600), faker.gameOfThrones().character(), method.name());
        assertTrue(service.createResponse(responseDTO).isSuccessful());
        assertNotEquals(service.getResponse(responseDTO.getUrl(), responseDTO.getMethod()), Optional.empty());
        assertTrue(service.deleteResponse(responseDTO.getUrl(), responseDTO.getMethod()).isSuccessful());
        assertEquals(service.getResponse(responseDTO.getUrl(), responseDTO.getMethod()), Optional.empty());
    }

    @ParameterizedTest
    @EnumSource(Methods.class)
    public void getRecordTest(Methods method) {
        ResponseDTO responseDTO = new ResponseDTO(faker.animal().name(), faker.random().nextInt(600), faker.gameOfThrones().character(), method.name());
        assertEquals(service.getResponse(responseDTO.getUrl(), responseDTO.getMethod()), Optional.empty());
        assertTrue(service.createResponse(responseDTO).isSuccessful());
        assertNotEquals(service.getResponse(responseDTO.getUrl(), responseDTO.getMethod()), Optional.empty());
        service.deleteResponse(responseDTO.getUrl(), responseDTO.getMethod());
    }
}
