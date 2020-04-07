package org.meerkatdev.redditroulette.net;

import android.net.Uri;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkUtils {

    private static final String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3";
    private static final String MOVIE_DB_POSTERS_BASE_PATH = "https://image.tmdb.org/t/p/";
    private static final String YOUTUBE_IMG_BASE_URL = "https://img.youtube.com/vi/";
    private static final String THUMBNAILS_DIMENSION = "w342";
    private static final String API_KEY_PARAM = "api_key";
    private static OkHttpClient client = new OkHttpClient();


    /**
     * The API KEY GOES HERE
     */
    private static final String API_KEY = "8d96c675fc882603db962b0152fd5ef7";

    /**
     * Builds the URL used to query a generic API in the Movie DB,
     * with no parameters other than the API key
     *
     * @return The URL built to be immediately sent in a request
     */
    public static Uri buildMovieRequestUrl(String path) {
        Uri requestUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath("movie")
                .appendPath(path)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        try {
            return requestUri;
        } catch (Exception e) {
            Log.d("NetworkUtils", String.valueOf(e));
            e.printStackTrace();
            return null;
        }

    }

    public static URL buildMovieRequestUrl(String id, String subObject) {
        String requestUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendPath("movie")
                .appendPath(id)
                .appendPath(subObject)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build().toString();
        URL url = null;

        try {
            url = new URL(requestUri);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static RequestCreator loadMoviePoster(String path) {
        String imageUri = Uri.parse(MOVIE_DB_POSTERS_BASE_PATH).buildUpon()
                .appendPath(THUMBNAILS_DIMENSION)
                .appendPath(path.substring(1)) // remove initial slash
                .build().toString();
        return Picasso.get().load(imageUri);
    }

    public static RequestCreator loadYoutubeThumbnail(String key) {
        String imageUri = Uri.parse(YOUTUBE_IMG_BASE_URL).buildUpon()
                .appendPath(key)
                .appendPath("0.jpg")
                .build().toString();
        Log.d("Network", "querying:" + imageUri);
        return Picasso.get().load(imageUri);
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

}
