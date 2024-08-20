package gr.aueb.cf.agronitor.apiclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Represents the api client that manages the api calls
 */
public class ApiClient {

    private static final String BASE_URL = "http://10.0.2.2:8080/api/";
//    private static final String BASE_URL = "http://192.168.1.197:8080/api/";
    private static Retrofit retrofit = null;

    static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                                                    .connectTimeout(20, TimeUnit.SECONDS)
                                                    .writeTimeout(20, TimeUnit.SECONDS)
                                                    .readTimeout(30, TimeUnit.SECONDS)
                                                    .build();

    public static Retrofit getApiClient() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder().setLenient().create();

            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                                             .addConverterFactory(GsonConverterFactory
                                             .create(gson))
                                             .client(okHttpClient)
                                             .build();
        }
        return retrofit;
    }
}
