package controller.menuConrollers;

import com.fasterxml.jackson.core.type.TypeReference;
import model.game.GameHistory;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

public class GameHistoryController {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String convertToJson(ArrayList<GameHistory> gameHistories) {
        objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(gameHistories);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<GameHistory> fromJson(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, new TypeReference<ArrayList<GameHistory>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
