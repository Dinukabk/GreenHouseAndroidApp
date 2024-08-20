package gr.aueb.cf.agronitor.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import gr.aueb.cf.agronitor.R;

/**
 * A fragment that displays alarm settings for a specific greenhouse.
 */
public class AlarmsFragment extends Fragment {

    private TextInputEditText setTempMinET;
    private TextInputEditText setTempMaxET;
    private SwitchCompat notificationsTempSW;
    private TextInputEditText setHumidityMinET;
    private TextInputEditText setHumidityMaxET;
    private SwitchCompat notificationsHumiditySW;
    private TextInputEditText setSoilMinET;
    private TextInputEditText setSoilMaxET;
    private SwitchCompat notificationsSoilSW;
    private SharedPreferences sharedPreferences;

    public AlarmsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarms, container, false);
        sharedPreferences = requireContext().getSharedPreferences("AlarmSettings", Context.MODE_PRIVATE);

        Bundle bundle = getArguments();
        String greenhouseId = bundle.getString("greenhouseId");

        setTempMinET = view.findViewById(R.id.setTempMinET);
        setTempMaxET = view.findViewById(R.id.setTempMaxET);
        notificationsTempSW = view.findViewById(R.id.notificationsTempSW);
        setHumidityMinET = view.findViewById(R.id.setHumidityMinET);
        setHumidityMaxET = view.findViewById(R.id.setHumidityMaxET);
        notificationsHumiditySW = view.findViewById(R.id.notificationsHumiditySW);
        setSoilMinET = view.findViewById(R.id.setSoilMinET);
        setSoilMaxET = view.findViewById(R.id.setSoilMaxET);
        notificationsSoilSW = view.findViewById(R.id.notificationsSoilSW);

        notificationsTempSW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String minTemp = setTempMinET.getText().toString().trim();
                String maxTemp = setTempMaxET.getText().toString().trim();
                if (!minTemp.isEmpty() && !maxTemp.isEmpty()) {
                    if (isChecked) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("minTemp_" + greenhouseId, minTemp);
                        editor.putString("maxTemp_" + greenhouseId, maxTemp);
                        editor.apply();
                    } else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("minTemp_" + greenhouseId);
                        editor.remove("maxTemp_" + greenhouseId);
                        editor.apply();
                        setTempMinET.setText("");
                        setTempMaxET.setText("");
                    }
                } else {
                    Toast.makeText(getContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    notificationsTempSW.setChecked(false);
                }
            }
        });

        notificationsHumiditySW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String minHum = setHumidityMinET.getText().toString().trim();
                String maxHum = setHumidityMaxET.getText().toString().trim();
                if (!minHum.isEmpty() && !maxHum.isEmpty()) {
                    if (isChecked) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("minHum_" + greenhouseId, minHum);
                        editor.putString("maxHum_" + greenhouseId, maxHum);
                        editor.apply();
                    } else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("minHum_" + greenhouseId);
                        editor.remove("maxHum_" + greenhouseId);
                        editor.apply();
                        setHumidityMinET.setText("");
                        setHumidityMaxET.setText("");
                    }
                } else {
                    Toast.makeText(getContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    notificationsHumiditySW.setChecked(false);
                }
            }
        });

        notificationsSoilSW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String minSoil = setSoilMinET.getText().toString().trim();
                String maxSoil = setSoilMaxET.getText().toString().trim();
                if (!minSoil.isEmpty() && !maxSoil.isEmpty()) {
                    if (isChecked) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("minSoil_" + greenhouseId, minSoil);
                        editor.putString("maxSoil_" + greenhouseId, maxSoil);
                        editor.apply();
                    } else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("minSoil_" + greenhouseId);
                        editor.remove("maxSoil_" + greenhouseId);
                        editor.apply();
                        setSoilMinET.setText("");
                        setSoilMaxET.setText("");
                    }
                } else {
                    Toast.makeText(getContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    notificationsSoilSW.setChecked(false);
                }
            }
        });

        return view;
    }
}