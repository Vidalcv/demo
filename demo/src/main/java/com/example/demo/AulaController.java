package com.example.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AulaController {

    @GetMapping("/aulas")
    public String getAulas(Model model) {
        String urlString = "http://serv-horarios.unsis.lan/api/aulas/capacidad/32";
        List<Map<String, Object>> aulasList = new ArrayList<>();

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) response.append(line);
            conn.disconnect();

            JSONArray jsonArray = new JSONArray(response.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject aula = jsonArray.getJSONObject(i);
                Map<String, Object> map = new HashMap<>();
                map.put("clave", aula.getInt("clave"));
                map.put("nombre", aula.getString("nombre"));
                map.put("capacidad", aula.getInt("capacidad"));
                map.put("tipo", aula.getString("tipo"));
                map.put("proyector", aula.optString("statusProyector", "N/A"));
                aulasList.add(map);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("aulas", aulasList); // Pasar los datos a la vista
        return "aulas"; // Nombre del archivo HTML
    }
}
