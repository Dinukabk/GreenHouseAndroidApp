package gr.aueb.cf.agronitor.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import gr.aueb.cf.agronitor.ManagementActivity;
import gr.aueb.cf.agronitor.R;
import gr.aueb.cf.agronitor.models.Greenhouse;

/**
 * Adapter class for the RecyclerView displaying a list of greenhouses.
 */
public class GreenhouseAdapter extends RecyclerView.Adapter<GreenhouseAdapter.MyViewHolder> {

    private static String greenhouseId;
    private final Context context;
    private ArrayList<Greenhouse> greenhouseArrayList;

    public GreenhouseAdapter(Context context, ArrayList<Greenhouse> greenhouseArrayList) {
        this.context = context;
        this.greenhouseArrayList = greenhouseArrayList;
    }

    public void setGreenhouseArrayList(ArrayList<Greenhouse> greenhouseArrayList) {
        this.greenhouseArrayList = greenhouseArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.greenhouse_card_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Greenhouse greenhouse = greenhouseArrayList.get(position);
        greenhouseId = greenhouse.getId().toString();
        holder.greenhouseNameTV.setText(greenhouse.getGreenhouseName());
        holder.greenhouseCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),
                        holder.greenhouseNameTV.getText().toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ManagementActivity.class);
                intent.putExtra("greenhouseId", greenhouse.getId().toString());
                intent.putExtra("greenhouseName", greenhouse.getGreenhouseName());
                intent.putExtra("userId", greenhouse.getUserId().toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return greenhouseArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public final TextView greenhouseNameTV;
        public final MaterialCardView greenhouseCardView;

        public MyViewHolder(@NotNull View itemView) {
            super(itemView);
            this.greenhouseNameTV = itemView.findViewById(R.id.greenhouseNameTV);
            this.greenhouseCardView = itemView.findViewById(R.id.greenhouseCardView);
        }
    }
}
