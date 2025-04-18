package com.demirlik.devices.devices.adddevice;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.demirlik.devices.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddDevice extends AppCompatActivity {
    EditText nameInput, releaseDateInput;
    Spinner typeSpinner, osSpinner;
    Button saveButton;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_device);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nameInput = findViewById(R.id.deviceName);
        releaseDateInput = findViewById(R.id.releaseDate);
        typeSpinner = findViewById(R.id.spinnerSelectType);
        osSpinner = findViewById(R.id.spinnerSelectOS);
        saveButton = findViewById(R.id.saveButton);
        ref = FirebaseDatabase.getInstance().getReference("devices");

        // Spinner tanımları
        typeSpinner = findViewById(R.id.spinnerSelectType);
        osSpinner = findViewById(R.id.spinnerSelectOS);

// Cihaz türü verileri
        String[] deviceTypes = {"Mobil Telefon", "Planşet", "Kompüter"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, deviceTypes);
        typeSpinner.setAdapter(typeAdapter);

// İşletim sistemi verileri
        String[] operatingSystems = {"Windows", "macOS", "Linux", "Android", "iOS", "FreeBSD", "UNIX"};
        ArrayAdapter<String> osAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, operatingSystems);
        osSpinner.setAdapter(osAdapter);


        saveButton.setOnClickListener(view -> {
            String name = nameInput.getText().toString();
            String type = typeSpinner.getSelectedItem().toString();
            String os = osSpinner.getSelectedItem().toString();
            String releaseDate = releaseDateInput.getText().toString();

            String id = ref.push().getKey();

            HashMap<String, Object> cihaz = new HashMap<>();
            cihaz.put("name", name);
            cihaz.put("type", type);
            cihaz.put("os", os);
            cihaz.put("releaseDate", releaseDate);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String email = user.getEmail();
                cihaz.put("email", email);
            }

            ref.child(id).setValue(cihaz).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Cihaz uğurla saxlanıldı!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        });


    }



}