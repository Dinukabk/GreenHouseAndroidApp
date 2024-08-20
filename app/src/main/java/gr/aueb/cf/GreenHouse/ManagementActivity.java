package gr.aueb.cf.agronitor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;

import gr.aueb.cf.agronitor.apiclient.ApiClient;
import gr.aueb.cf.agronitor.apiclient.IApiService;
import gr.aueb.cf.agronitor.apiclient.responses.MeasurementsResponse;
import gr.aueb.cf.agronitor.fragments.AlarmsFragment;
import gr.aueb.cf.agronitor.fragments.SettingsFragment;
import gr.aueb.cf.agronitor.fragments.StatsFragment;
import gr.aueb.cf.agronitor.fragments.StatsFragmentEmpty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity for managing a specific greenhouse.
 */
public class ManagementActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private NavigationBarView bottomNavView;
    private String currentTemp;
    private String currentHum;
    private String currentHydr;
    private String currentUV;
    private String minTemp;
    private String maxTemp;
    private String minHum;
    private String maxHum;
    private String minHydr;
    private String maxHydr;
    private String minUV;
    private String maxUV;

    boolean statsAreEmpty = true;

    StatsFragment statsFragment = new StatsFragment();
    StatsFragmentEmpty statsFragmentEmpty = new StatsFragmentEmpty();
    AlarmsFragment alarmsFragment = new AlarmsFragment();
    SettingsFragment settingsFragment = new SettingsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

        String greenhouseId = getIntent().getStringExtra("greenhouseId");
        String greenhouseName = getIntent().getStringExtra("greenhouseName");
        String userId = getIntent().getStringExtra("userId");
        getMeasurements(greenhouseId);

        Bundle infoBundle = new Bundle();
        infoBundle.putString("greenhouseId", greenhouseId);
        infoBundle.putString("greenhouseName", greenhouseName);
        infoBundle.putString("userId", userId);

        statsFragment.setArguments(infoBundle);
        alarmsFragment.setArguments(infoBundle);
        settingsFragment.setArguments(infoBundle);

        bottomNavView = findViewById(R.id.bottomNavView);
        bottomNavView.setOnItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, GreenhouseViewActivity.class);
        intent.putExtra("userId", getIntent().getStringExtra("userId"));
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.stats) {
            if (statsAreEmpty) {
                getSupportFragmentManager().beginTransaction().replace(R.id.FragmentFL, statsFragmentEmpty).commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.FragmentFL, statsFragment).commit();
            }
            return true;
        } else if (item.getItemId() == R.id.alarms) {
            getSupportFragmentManager().beginTransaction().replace(R.id.FragmentFL, alarmsFragment).commit();
            return true;
        } else if (item.getItemId() == R.id.settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.FragmentFL, settingsFragment).commit();
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    private void getMeasurements(String greenhouseId) {
        IApiService apiService = ApiClient.getApiClient().create(IApiService.class);
        Call<MeasurementsResponse> call = apiService.getMeasurements(greenhouseId);
        call.enqueue(new Callback<MeasurementsResponse>() {
            @Override
            public void onResponse(Call<MeasurementsResponse> call, Response<MeasurementsResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        currentTemp = response.body().getCurrentTemp();
                        currentHum = response.body().getCurrentHum();
                        currentHydr = response.body().getCurrentHydr();
                        currentUV = response.body().getCurrentUV();
                        minTemp = response.body().getMinTemp();
                        maxTemp = response.body().getMaxTemp();
                        minHum = response.body().getMinHum();
                        maxHum = response.body().getMaxHum();
                        minHydr = response.body().getMinHydr();
                        maxHydr = response.body().getMaxHydr();
                        minUV = response.body().getMinUV();
                        maxUV = response.body().getMaxUV();

                        Bundle measurements = new Bundle();
                        measurements.putString("currentTemp", currentTemp);
                        measurements.putString("currentHum", currentHum);
                        measurements.putString("currentHydr", currentHydr);
                        measurements.putString("currentUV", currentUV);
                        measurements.putString("minTemp", minTemp);
                        measurements.putString("maxTemp", maxTemp);
                        measurements.putString("minHum", minHum);
                        measurements.putString("maxHum", maxHum);
                        measurements.putString("minHydr", minHydr);
                        measurements.putString("maxHydr", maxHydr);
                        measurements.putString("minUV", minUV);
                        measurements.putString("maxUV", maxUV);
                        statsFragment.setArguments(measurements);

                        statsAreEmpty = false;
                        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentFL, statsFragment).commit();
                    } else {
                        statsAreEmpty = true;
                        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentFL, statsFragmentEmpty).commit();
                        Toast.makeText(ManagementActivity.this, "There are not measurements yet", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<MeasurementsResponse> call, Throwable t) {
                Toast.makeText(ManagementActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}