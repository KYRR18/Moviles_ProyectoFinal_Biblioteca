package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

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

        btnBackPerfil.setOnClickListener(this::goToHome);

        // ================= CIRCUITO DE NAVEGACIÓN INFERIOR =================
        navHome.setOnClickListener(this::goToHome);
        navLista.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, ListaAdapter.class); // Verifica si se llama ListasActivity o ListaActivity
            startActivity(intent);
            finish();
        });
        navLupa.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, BuscarActivity.class);
            startActivity(intent);
            finish();
        });
        navPerfil.setOnClickListener(v -> {});

        // El botón de cerrar sesión te saca por completo y limpia el historial para ir al Login
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
    public void goToHome(View v){
        Intent intent = new Intent(PerfilActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}