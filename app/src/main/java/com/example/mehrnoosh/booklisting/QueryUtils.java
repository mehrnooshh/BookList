package com.example.mehrnoosh.booklisting;

/**
 * Created by Mehrnoosh on 12/17/2017.
 */

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving book data from GoogleBook.
 */
public final class QueryUtils {

    /** Tag for the log messages */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    /**
     * Return a list of {@link Book} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Book> extractFeatureFromJson(String bookJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding bookItems to
        List<Book> bookItems = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(bookJSON);

            // Extract the JSONArray associated with the key called "items",
            // which represents a list of items (or books).
            JSONArray bookItemsJsonArray = baseJsonResponse.getJSONArray("items");

            // For each book in the bookItemsJsonArray, create an {@link Book} object
            for (int i = 0; i < bookItemsJsonArray.length(); i++) {

                // Get a single book at position i within the list of books
                JSONObject currentBook = bookItemsJsonArray.getJSONObject(i);

                // For a given book, extract the JSONObject associated with the
                // key called "volumeInfo", which represents a list of all volume information
                // for that book.
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                // Extract the value for the key called "id"
                String googleId = currentBook.getString("id");

                // Extract the value for the key called "title"
                String title = volumeInfo.getString("title");

                // Extract the value for the key called "publisher"
                String publisher = "";
                if (volumeInfo.has("publisher")) {
                    publisher = volumeInfo.getString("publisher");
                }
               // Extract the value for the jsonObject associated with the key called "imageLinks"
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                String smallThumbnail = "";
                if (imageLinks.has("smallThumbnail")) {
                    smallThumbnail = imageLinks.getString("thumbnail");
                }
                // Extract the value for the key called "publishedDate"
                String publishedDate = "";
                if (volumeInfo.has("publishedDate")) {
                    publishedDate = volumeInfo.getString("publishedDate");
                }

                // Extract the value for the key called "infoLink"
                String infoUrl = volumeInfo.getString("infoLink");


                List<String> authors = new ArrayList<>();
                // Extract the value for the key called "authors"
                if (volumeInfo.has("authors")) {
                    JSONArray authorsJsonArray = volumeInfo.getJSONArray("authors");
                    for (int j = 0; j < authorsJsonArray.length(); j++) {
                        authors.add(authorsJsonArray.getString(j));
                    }
                }


                // Create a new {@link Book} object with the title, publisher, authors, googleId, date,
                // and infoUrl from the JSON response.
                Book book = new Book(title, publisher, googleId, publishedDate, infoUrl, authors, smallThumbnail);

                // Add the new {@link Book} to the list of bookItems.
                bookItems.add(book);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of bookItems
        return bookItems;
    }

    /**
     * Query the GoogleBook dataset and return a list of {@link Book} objects.
     */
    public static List<Book> fetchBookData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Book}s
        List<Book> books = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Boook}s
        return books;
    }
}