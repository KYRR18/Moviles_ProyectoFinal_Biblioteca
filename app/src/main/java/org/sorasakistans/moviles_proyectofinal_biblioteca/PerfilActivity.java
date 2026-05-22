package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

import java.util.concurrent.atomic.AtomicReference;

public class PerfilActivity extends AppCompatActivity {
    ImageView btnBackPerfil, btnMoreOptionsPerfil;
    MaterialButton btnEditProfile, btnSettings, btnLogout;
    ImageView navPerfil, navHome, navLista, navLupa;
    TextView userAdr, userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);

        btnBackPerfil = findViewById(R.id.btnBackPerfil);
        btnMoreOptionsPerfil = findViewById(R.id.btnMoreOptionsPerfil);

        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnSettings = findViewById(R.id.btnSettings);
        btnLogout = findViewById(R.id.btnLogout);

        navHome = findViewById(R.id.navHome);
        navLista = findViewById(R.id.navLista);
        navLupa = findViewById(R.id.navLupa);
        navPerfil = findViewById(R.id.navPerfil);

        userName = findViewById(R.id.tvUserName);
        userAdr = findViewById(R.id.tvUserEmail);

        // Cargar datos de sesión desde SharedPreferences
        AtomicReference<SharedPreferences> prefs = new AtomicReference<>(getSharedPreferences("sesion_usuario", MODE_PRIVATE));
        String username = prefs.get().getString("username", "Usuario");
        String email = prefs.get().getString("email", "");
        userName.setText(username);
        userAdr.setText(email);

        btnBackPerfil.setOnClickListener(this::goToHome);

        // ================= CIRCUITO DE NAVEGACIÓN INFERIOR =================
        navHome.setOnClickListener(this::goToHome);
        navLista.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, ListasActivity.class); // Verifica si se llama ListasActivity o ListaActivity
            startActivity(intent);
            finish();
        });
        navLupa.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, BuscarActivity.class);
            startActivity(intent);
            finish();
        });
        navPerfil.setOnClickListener(v -> {});

        btnLogout.setOnClickListener(v -> {
            // Limpiar la sesión guardada
            prefs.set(getSharedPreferences("sesion_usuario", MODE_PRIVATE));
            prefs.get().edit().clear().apply();

            Intent intent = new Intent(PerfilActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PerfilActivity.this, EditPerfil.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void goToHome(View v){
        Intent intent = new Intent(PerfilActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}