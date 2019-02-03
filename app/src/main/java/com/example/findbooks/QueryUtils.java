package com.example.findbooks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.graphics.BitmapFactory.decodeStream;

public final class QueryUtils {

    private QueryUtils() {
    }

    private static URL createUrlObject(String stringUrl) {
        URL urlOject = null;
        try {
            urlOject = new URL(stringUrl);
        } catch (MalformedURLException e) {
            //
        }
        return urlOject;
    }

    private static String makeHttpRequest(URL urlOject) throws IOException {

        String jsonResponse = "";

        if (urlOject == null) {
            return jsonResponse;
        }

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) urlOject.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromInputStream(inputStream);
            }
        } catch (IOException e) {
            //
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    private static List<BookDetails> extractDataFromJson(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        List<BookDetails> books = new ArrayList<>();

        try {
            JSONObject baseJsonObject = new JSONObject(jsonResponse);
            JSONArray itemsArray = baseJsonObject.getJSONArray("items");

            for (int i = 1; i < itemsArray.length(); i++) {
                JSONObject currentBook = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                JSONObject searchInfo = currentBook.getJSONObject("searchInfo");
                String tileOfBook = volumeInfo.getString("title");
                JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                String authors = authorsArray.getString(0);
                if (authorsArray.length() > 1) {
                    authors = authors + " &.....";
                }

                String description = volumeInfo.optString("description");
                if (description == null) {
                    description = searchInfo.getString("textSnippet");
                }
                String publishedDate = volumeInfo.getString("publishedDate");
                String imageURL = "";
                if (volumeInfo.has("imageLinks")) {
                    JSONObject imageLinksObject = volumeInfo.getJSONObject("imageLinks");
                    if (imageLinksObject.has("thumbnail")) {
                        imageURL = imageLinksObject.getString("thumbnail");

                    }

                }
                String infoURL = volumeInfo.getString("infoLink");

                BookDetails currentBookDetails = new BookDetails(imageURL, tileOfBook, authors, description, publishedDate, infoURL);

                // BookDetails currentBookDetails=new BookDetails(imageURL,tileOfBook,authors,description,publishedDate,infoURL);
                books.add(currentBookDetails);

            }
        } catch (JSONException e) {
            //
        }

        return books;
    }

    public static List<BookDetails> fetchBooksData(String requestURL) {
        URL url = createUrlObject(requestURL);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            //
        }
        List<BookDetails> books = extractDataFromJson(jsonResponse);
        return books;
    }

}

