package gr.aueb.cf.agronitor.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import gr.aueb.cf.agronitor.R;

/**
 * A Fragment that displays the stats for a specific greenhouse.
 */
public class StatsFragment extends Fragment {

//    Temperature
    private TextView currentTempTV;
    private TextView minTempTV;
    private TextView maxTempTV;
//    Humidity
    private TextView currentHumidityTV;
    private TextView minHumidityTV;
    private TextView maxHumidityTV;
//    Soil Hydration
    private TextView currentSoilHydrationTV;
    private TextView minSoilHydrationTV;
    private TextView maxSoilHydrationTV;
//    UV Radiation
    private TextView currentUVRadiationTV;
    private TextView minUVRadiationTV;
    private TextView maxUVRadiationTV;

    public StatsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        currentTempTV = view.findViewById(R.id.currentTempTV);
        minTempTV = view.findViewById(R.id.minTempTV);
        maxTempTV = view.findViewById(R.id.maxTempTV);
        currentHumidityTV = view.findViewById(R.id.currentHumidityTV);
        minHumidityTV = view.findViewById(R.id.minHumidityTV);
        maxHumidityTV = view.findViewById(R.id.maxHumidityTV);
        currentSoilHydrationTV = view.findViewById(R.id.currentSoilHydrationTV);
        minSoilHydrationTV = view.findViewById(R.id.minSoilHydrationTV);
        maxSoilHydrationTV = view.findViewById(R.id.maxSoilHydrationTV);
        currentUVRadiationTV = view.findViewById(R.id.currentUVRadiationTV);
        minUVRadiationTV = view.findViewById(R.id.minUVRadiationTV);
        maxUVRadiationTV = view.findViewById(R.id.maxUVRadiationTV);

        Bundle bundle = getArguments();
        String currentTemp = bundle.getString("currentTemp");
        String currentHum = bundle.getString("currentHum");
        String currentHydr = bundle.getString("currentHydr");
        String currentUV = bundle.getString("currentUV");
        String minTemp = bundle.getString("minTemp");
        String maxTemp = bundle.getString("maxTemp");
        String minHum = bundle.getString("minHum");
        String maxHum = bundle.getString("maxHum");
        String minHydr = bundle.getString("minHydr");
        String maxHydr = bundle.getString("maxHydr");
        String minUV = bundle.getString("minUV");
        String maxUV = bundle.getString("maxUV");

        currentTempTV.setText("Current Temperature: " + currentTemp + " C");
        currentHumidityTV.setText("Current Humidity: " + currentHum + "%");
        currentSoilHydrationTV.setText("Current Soil Hydration: " + currentHydr + "%");
        currentUVRadiationTV.setText("Current UV Radiation: " + currentUV + "");
        minTempTV.setText("min: " + minTemp);
        maxTempTV.setText("max: " + maxTemp);
        minHumidityTV.setText("min: " + minHum);
        maxHumidityTV.setText("max: " + maxHum);
        minSoilHydrationTV.setText("min: " + minHydr);
        maxSoilHydrationTV.setText("max: " + maxHydr);
        minUVRadiationTV.setText("min: " + minUV);
        maxUVRadiationTV.setText("max: " + maxUV);

        return view;
    }
}