package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {
    ImageView navLupa, navHome, navPerfil, navLista;
    ImageView btnMoreOptionsHome;
    FloatingActionButton fabAddLibreria1, fabAddLibreria2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        navHome = findViewById(R.id.navHome);
        navLista = findViewById(R.id.navLista);
        navLupa = findViewById(R.id.navLupa);
        navPerfil = findViewById(R.id.navPerfil);

        btnMoreOptionsHome = findViewById(R.id.btnMoreOptionsHome);
        fabAddLibreria1 = findViewById(R.id.fabAddLibreria1);
        fabAddLibreria2 = findViewById(R.id.fabAddLibreria2);

        // ================= CIRCUITO DE NAVEGACIÓN INFERIOR =================
        navHome.setOnClickListener(v -> {});
        navLista.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ListasActivity.class); // Verifica si se llama ListasActivity o ListaActivity
            startActivity(intent);
            finish(); // Cierra Home para liberar espacio en memoria
        });
        navLupa.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, BuscarActivity.class);
            startActivity(intent);
            finish();
        });
        navPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PerfilActivity.class);
            startActivity(intent);
            finish();
        });

        btnMoreOptionsHome.setOnClickListener(v -> {});

        fabAddLibreria1.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AnadirLibroActivity.class);
            startActivity(intent);
        });

        fabAddLibreria2.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AnadirLibroActivity.class);
            startActivity(intent);
        });

    }
}