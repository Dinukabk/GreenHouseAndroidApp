package gr.aueb.cf.agronitor.apiclient;

import java.util.List;

import gr.aueb.cf.agronitor.apiclient.requests.AddGreenhouseRequest;
import gr.aueb.cf.agronitor.apiclient.requests.LoginRequest;
import gr.aueb.cf.agronitor.apiclient.responses.AddGreenhouseResponse;
import gr.aueb.cf.agronitor.apiclient.responses.LoginResponse;
import gr.aueb.cf.agronitor.apiclient.requests.RegisterRequest;
import gr.aueb.cf.agronitor.apiclient.responses.MeasurementsResponse;
import gr.aueb.cf.agronitor.apiclient.responses.RegisterResponse;
import gr.aueb.cf.agronitor.models.Greenhouse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * The service that handles the api calls
 */
public interface IApiService {

    @POST("users/register/")
    Call<RegisterResponse> registerUser (@Body RegisterRequest registerRequest);

    @POST("users/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @GET("greenhouses/user/{userId}")
    Call<List<Greenhouse>> getGreenhouses(@Path("userId") String userId);

    @POST("greenhouses/user/{userId}")
    Call<AddGreenhouseResponse> addGreenhouse(@Path("userId") String userId,
                                              @Body AddGreenhouseRequest addGreenhouseRequest);

    @GET("greenhouses/measurements/{greenhouseId}")
    Call<MeasurementsResponse> getMeasurements(@Path("greenhouseId") String greenhouseId);

    @DELETE("greenhouses/{greenhouseId}")
    Call<Greenhouse> deleteGreenhouse(@Path("greenhouseId") String greenhouseId);

    @PUT("greenhouses/{greenhouseId}")
    Call<Greenhouse> renameGreenhouse(@Path("greenhouseId") String greenhouseId,
                                      @Query("greenhouseNewName") String greenhouseNewName);
}
