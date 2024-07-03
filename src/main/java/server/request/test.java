package server.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import controller.CardController;
import model.role.Unit;
import server.User;

import java.io.IOException;

public class test {
    public test() throws JsonProcessingException {
    }

    public static void main(String[] args) throws IOException {

        CardController.load_data();
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User("muUsername", "passworddd", "jnjnj", "kjbk");
        User user1 = objectMapper.readValue(objectMapper.writeValueAsString(user), User.class);
    }
}
