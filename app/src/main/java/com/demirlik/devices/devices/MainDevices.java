package com.demirlik.devices.devices;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.demirlik.devices.R;
import com.demirlik.devices.devices.adddevice.AddDevice;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class MainDevices extends AppCompatActivity {

    private FloatingActionButton addDevicenewActivityBtn;

    private ListView listView;
    ArrayAdapter<String> adapter;

    private ArrayList<String> devicesList = new ArrayList<>();
    private DatabaseReference databaseReference;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_devices);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addDevicenewActivityBtn = findViewById(R.id.addDevicenewActivityBtn);
        listView = findViewById(R.id.devicesList);
        devicesList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, devicesList);
        listView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("devices");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                devicesList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) snap.getValue();
                    if (map != null) {
                        String name = map.get("name").toString();
                        String type = map.get("type").toString();
                        devicesList.add(type + " - " + name);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainDevices.this, "Məlumat Toplana bilmir.", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.addDevicenewActivityBtn).setOnClickListener(view -> {
            startActivity(new Intent(this, AddDevice.class));
        });
    }
}