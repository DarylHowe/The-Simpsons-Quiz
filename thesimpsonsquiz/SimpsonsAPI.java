package com.darylhowedevs.thesimpsonsquiz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimpsonsAPI {

    private APIConnection apiConnection;

    private String API = "https://thesimpsonsquoteapi.glitch.me/quotes";
    private String response = "";

    private String quote = "";
    private String character = "";
    private String imageURL = "";

    private Bitmap myBitmap;

    private String[] possibleAnswers = new String[4];
    private Bitmap[] possibleCharacters = new Bitmap[4];

    public SimpsonsAPI() {
        apiConnection = new APIConnection();
        response = apiConnection.callAPI(API);
    }

    public void extractData() {

        quote = apiConnection.extractJSONData(response, "quote");
        character = apiConnection.extractJSONData(response, "character");
        imageURL = apiConnection.extractJSONData(response, "image");

        try {
            URL url = new URL(imageURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream in = connection.getInputStream();
            myBitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A method to generate and add 3 non matching possible answers to the possible answer lists.
     */
    public void generatePossibleAnswers() {

        possibleAnswers[0] = character;
        possibleCharacters[0] = myBitmap;

        SimpsonsAPI simpsonsAPI = new SimpsonsAPI();
        simpsonsAPI.extractData();
        possibleAnswers[1] = simpsonsAPI.getCharacter();
        possibleCharacters[1] = simpsonsAPI.getCharacterBitmap();

        simpsonsAPI = new SimpsonsAPI();
        simpsonsAPI.extractData();
        possibleAnswers[2] = simpsonsAPI.getCharacter();
        possibleCharacters[2] = simpsonsAPI.getCharacterBitmap();

        simpsonsAPI = new SimpsonsAPI();
        simpsonsAPI.extractData();
        possibleAnswers[3] = simpsonsAPI.getCharacter();
        possibleCharacters[3] = simpsonsAPI.getCharacterBitmap();

        checkPossibleAnswersForMatch();
    }

    /**
     * A method to check if any of the possible answers are duplicated. EG There are two homers.
     * The possible answers list show not contain duplicates as the user would not know which two
     * of the correct answers are actaully correct.
     */
    private void checkPossibleAnswersForMatch(){
        // Check none of the answers match else there will be two of the same answer.
        for (int i = 0; i < possibleAnswers.length; i++) {
            for (int j = 1; j < possibleAnswers.length; j++) {

                // Don't campere the same element to the same element or else it will match!
                if (i != j) {

                    if (possibleAnswers[i].equals(possibleAnswers[j])) {
                        generatePossibleAnswers();
                    }
                }
            }
        }
    }

    public String getQuote() {
        return quote;
    }

    public String getCharacter() {
        return character;
    }

    public String[] getPossibleAnswers() {
        return possibleAnswers;
    }

    public Bitmap[] getPossibleCharacters(){return possibleCharacters;}

    public String getResponse() {
        return response;
    }

    public Bitmap getCharacterBitmap(){
        return myBitmap;
    }

}
