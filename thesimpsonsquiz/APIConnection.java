package com.darylhowedevs.thesimpsonsquiz;


import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIConnection {

    public APIConnection() {
    }

    /**
     * A method which calls an API and returns the response as a string.
     * @param APIUrl
     * @return
     */
    public String callAPI(String APIUrl) {

        StringBuffer APIResponse = new StringBuffer();
        String result = "";
        HttpURLConnection apiConnection = null;

        try {

            URL url = new URL(APIUrl);
            apiConnection = (HttpURLConnection) url.openConnection();
            InputStream in = apiConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();

            // once there is data incomming
            while (data != -1) {
                char current = (char) data;
                result += current;
                data = reader.read();
            }

        } catch(FileNotFoundException fileNotFoundException){

            // A file not found exception can be thrown when the simpsons API has recieved too many requests.
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
                apiConnection.disconnect();
        }

        return result;
    }

    /**
     * A method which extracts the JSON data of a specific key and returns as a string.
     * @param JSONResponse A string in JSON format.
     */
    public String extractJSONData(String JSONResponse, String key) {

        String data = "-1";

        try {

            JSONArray jArray = new JSONArray(JSONResponse.toString());
            JSONObject jsonPart = jArray.getJSONObject(0);
            data = jsonPart.getString(key);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
}
