package server.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import controller.CardController;
import model.role.Faction;
import model.role.Unit;
import server.User;

import java.io.IOException;

public class test {
    public test() throws JsonProcessingException {
    }

    public static void main(String[] args) throws IOException {

        CardController.load_data();
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.readValue(objectMapper.writeValueAsString(Faction.NILFGAARDIAN_EMPIRE), Faction.class).equals(Faction.NILFGAARDIAN_EMPIRE));
    }
}
