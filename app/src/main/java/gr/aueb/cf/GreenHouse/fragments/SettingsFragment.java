package gr.aueb.cf.agronitor.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import gr.aueb.cf.agronitor.GreenhouseViewActivity;
import gr.aueb.cf.agronitor.R;
import gr.aueb.cf.agronitor.apiclient.ApiClient;
import gr.aueb.cf.agronitor.apiclient.IApiService;
import gr.aueb.cf.agronitor.models.Greenhouse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A Fragment that displays settings for a specific greenhouse.
 */
public class SettingsFragment extends Fragment {

    private TextInputEditText renameGreenhouseET;
    private AppCompatButton saveChangesBTN;
    private AppCompatButton removeGreenhouseBTN;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Bundle bundle = getArguments();
        String greenhouseName = bundle.getString("greenhouseName");
        String greenhouseId = bundle.getString("greenhouseId");
        String userId = bundle.getString("userId");

        renameGreenhouseET = view.findViewById(R.id.renameGreenhouseET);
        renameGreenhouseET.setText(greenhouseName);
        saveChangesBTN = view.findViewById(R.id.saveChangesBTN);
        removeGreenhouseBTN = view.findViewById(R.id.removeGreenhouseBTN);

        saveChangesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Save Changes")
                        .setMessage("Are you sure you want to save changes?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String greenhouseNewName = renameGreenhouseET.getText().toString().trim();
                                if (!greenhouseNewName.isEmpty()) {
                                    renameGreenhouse(greenhouseId, greenhouseNewName);
                                } else {
                                    Toast.makeText(getContext(), "Name cannot be empty!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        removeGreenhouseBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Remove Greenhouse?")
                        .setMessage("Are you sure you want to remove this greenhouse?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeGreenhouse(userId, greenhouseId);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        return view;
    }

    private void renameGreenhouse(String greenhouseId, String greenhouseNewName) {
        IApiService apiService = ApiClient.getApiClient().create(IApiService.class);
        Call<Greenhouse> call = apiService.renameGreenhouse(greenhouseId, greenhouseNewName);
        call.enqueue(new Callback<Greenhouse>() {
            @Override
            public void onResponse(Call<Greenhouse> call, Response<Greenhouse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Greenhouse renamed to " + greenhouseNewName, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "There was a problem renaming this greenhouse", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Greenhouse> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeGreenhouse(String userId, String greenhouseId) {
        IApiService apiService = ApiClient.getApiClient().create(IApiService.class);
        Call<Greenhouse> call = apiService.deleteGreenhouse(greenhouseId);
        call.enqueue(new Callback<Greenhouse>() {
            @Override
            public void onResponse(Call<Greenhouse> call, Response<Greenhouse> response) {
                if (response.isSuccessful()) {
                    String greenhouseName = response.body().getGreenhouseName();
                    Toast.makeText(getContext(), "Greenhouse " + greenhouseName + " deleted successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), GreenhouseViewActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "There was a problem removing this greenhouse", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Greenhouse> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}