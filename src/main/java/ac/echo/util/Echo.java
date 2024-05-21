package ac.echo.util;

import ac.echo.Main;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Echo {
    public static String getDownloadLink() {
        try {
            URL urlObj = new URL(Main.getInstance().getUrl());
            HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + Main.getInstance().getApiKey());
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                JsonObject jsonResponse = new Gson().fromJson(response.toString(), JsonObject.class);
                return jsonResponse.get("links").getAsJsonObject().get("minecraft").toString().replace("\"", "");
            } else {
                System.out.println("GET request failed. Response Code: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("Error making GET request: " + e.getMessage());
            e.printStackTrace();
        }

        return "N/A";
    }
}
