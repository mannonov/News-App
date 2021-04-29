package uz.jaxadev.newsapp;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QueryUtils {

    public static List<News> fetchNews(String argUrl) {

        URL url = createUrl(argUrl);

        String response = null;
        try {
            response = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<News> newsList = parseJsonNews(response);

        return newsList;

    }

    private static String makeHttpRequest(URL url) throws IOException {

        String rawResponse = "";

        if (url == null) {
            return rawResponse;
        }

        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                rawResponse = readFromStream(inputStream);
            } else {
                Log.e("Fetching", "An error occured" + connection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }

        }

        return rawResponse;

    }

    private static List<News> parseJsonNews(String rawResponse) {

        if (rawResponse == null || rawResponse.isEmpty()) {
            return null;
        }

        List<News> parsedNews = new ArrayList<>();

        try {
            JSONObject rootObject = new JSONObject(rawResponse);

            JSONObject rootResponse = rootObject.getJSONObject("response");

            JSONArray resultsArray = rootResponse.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject news = resultsArray.getJSONObject(i);

                String newsTitle = news.getString("webTitle");
                String newsSection = news.getString("sectionName");
                String encodedDate = news.getString("webPublicationDate");
                String newsLink = news.getString("webUrl");

                String newsAuthor = "";

                JSONArray tagsContributors = news.getJSONArray("tags");
                if (tagsContributors != null && tagsContributors.length() != 0) {
                    JSONObject tagContributor = tagsContributors.getJSONObject(0);
                    newsAuthor = tagContributor.getString("webTitle");
                }

                String formattedDate = formatDate(encodedDate);

                parsedNews.add(new News(newsTitle, newsSection, newsLink, formattedDate, newsAuthor));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return parsedNews;

    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();

            while (line != null) {
                stringBuilder.append(line);
                line = reader.readLine();
            }
        }

        return stringBuilder.toString();
    }


    private static URL createUrl(String argUrl) {
        URL workUrl = null;

        try {
            workUrl = new URL(argUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return workUrl;
    }

    private static String formatDate(String encodedDate) {

        String encodingFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat formatter = new SimpleDateFormat(encodingFormat, Locale.US);

        try {
            Date date = formatter.parse(encodedDate);

            String formattedDatePattern = "MMM d, yyy";
            SimpleDateFormat format = new SimpleDateFormat(formattedDatePattern, Locale.US);
            return format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
