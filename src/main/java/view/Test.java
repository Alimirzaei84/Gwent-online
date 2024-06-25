package view;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        ArrayList<String> myList = new ArrayList<>();
        myList.add("Apple");
        myList.add("Banana");
        myList.add("Cherry");

        Gson gson = new Gson();
        String jsonString = gson.toJson(myList);

        System.out.println(jsonString);

        ArrayList<String> restoredList = gson.fromJson(jsonString, new TypeToken<ArrayList<String>>() {}.getType());

        System.out.println(restoredList.toString());
    }
}