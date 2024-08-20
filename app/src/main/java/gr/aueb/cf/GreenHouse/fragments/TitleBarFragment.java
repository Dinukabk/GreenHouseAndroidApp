package gr.aueb.cf.agronitor.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import gr.aueb.cf.agronitor.LoginActivity;
import gr.aueb.cf.agronitor.R;

/**
 * A Fragment that displays the title bar of the app.
 */
public class TitleBarFragment extends Fragment implements View.OnClickListener{

    private AppCompatImageButton popupMenuBtn;

    public TitleBarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_title_bar, container, false);

        popupMenuBtn = view.findViewById(R.id.popupMenuBtn);
        popupMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), popupMenuBtn);
                popupMenu.inflate(R.menu.popup_settings_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.action_logout) {
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Logout")
                                    .setMessage("Are you sure you want to logout?")
                                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(getContext(), LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton(R.string.no, null)
                                    .show();
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {

    }
}