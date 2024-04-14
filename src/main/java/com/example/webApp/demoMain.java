package com.example.webApp;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class demoMain {

    public static void main(String[] args) {

        // Vòng lặp tạo đối tượng mới và thêm vào danh sách
        List<JSONObject> jsonObjectList = new ArrayList<>();

        for (int i = 0; i < 10000000; i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("key", "value");
            jsonObjectList.add(jsonObject);

//            jsonObject.clear();
        }


    }
}