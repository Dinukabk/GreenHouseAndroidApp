package gr.aueb.cf.agronitor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import gr.aueb.cf.agronitor.adapters.GreenhouseAdapter;
import gr.aueb.cf.agronitor.apiclient.ApiClient;
import gr.aueb.cf.agronitor.apiclient.IApiService;
import gr.aueb.cf.agronitor.apiclient.requests.AddGreenhouseRequest;
import gr.aueb.cf.agronitor.apiclient.responses.AddGreenhouseResponse;
import gr.aueb.cf.agronitor.models.Greenhouse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity for displaying the greenhouses related to a user.
 */
public class GreenhouseViewActivity extends AppCompatActivity {

    private String userId;
    private RecyclerView greenhousesRV;
    private FloatingActionButton addGreenhouse;
    private String newGreenhouseName = "";
    private ArrayList<Greenhouse> greenhouseList = new ArrayList<>();
    private GreenhouseAdapter greenhouseAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        greenhousesRV = findViewById(R.id.greenhousesRV);
        addGreenhouse = findViewById(R.id.addGreenhouse);
        userId = getIntent().getStringExtra("userId");
        getGreenhouses(userId);

        greenhouseAdapter = new GreenhouseAdapter(this, greenhouseList);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        greenhousesRV.setLayoutManager(linearLayoutManager);
        greenhousesRV.setAdapter(greenhouseAdapter);

        addGreenhouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GreenhouseViewActivity.this);
                final EditText input = new EditText(GreenhouseViewActivity.this);

                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setTitle("Name your new greenhouse")
                        .setView(input)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                newGreenhouseName = input.getText().toString();
                                if (!newGreenhouseName.equals("")) {
                                    AddGreenhouseRequest addGreenhouseRequest = new AddGreenhouseRequest(newGreenhouseName);
                                    addNewGreenhouse(userId, addGreenhouseRequest);
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
    }

    public void getGreenhouses(String userId) {
        IApiService apiService = ApiClient.getApiClient().create(IApiService.class);
        Call<List<Greenhouse>> call = apiService.getGreenhouses(userId);
        call.enqueue(new Callback<List<Greenhouse>>() {
            @Override
            public void onResponse(Call<List<Greenhouse>> call, Response<List<Greenhouse>> response) {
                if (response.isSuccessful()) {
                    List<Greenhouse> greenhouses = response.body();
                    if (greenhouses != null) {
                        ArrayList<Greenhouse> greenhouseArrayList = new ArrayList<>(greenhouses);
                        updateUi(greenhouseArrayList);
                    } else {
                        Toast.makeText(GreenhouseViewActivity.this, "No greenhouses found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Greenhouse>> call, Throwable t) {
                Toast.makeText(GreenhouseViewActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addNewGreenhouse(String userId, AddGreenhouseRequest addGreenhouseRequest) {
        IApiService apiService = ApiClient.getApiClient().create(IApiService.class);
        Call<AddGreenhouseResponse> call = apiService.addGreenhouse(userId, addGreenhouseRequest);
        call.enqueue(new Callback<AddGreenhouseResponse>() {
            @Override
            public void onResponse(Call<AddGreenhouseResponse> call, Response<AddGreenhouseResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(GreenhouseViewActivity.this, "New greenhouse added", Toast.LENGTH_SHORT).show();
                    getGreenhouses(userId);
                } else {
                    Toast.makeText(GreenhouseViewActivity.this, "Greenhouse was Not added", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddGreenhouseResponse> call, Throwable t) {
                Toast.makeText(GreenhouseViewActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUi(ArrayList<Greenhouse> greenhouseArrayList) {
        greenhouseAdapter.setGreenhouseArrayList(greenhouseArrayList);
        greenhouseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}