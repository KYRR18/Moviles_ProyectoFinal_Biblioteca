package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    ImageView navLupa, navHome, navPerfil, navLista;
    ImageView btnMoreOptionsHome;
    RecyclerView rvEstanterias;
    EstanteriaAdapter adapter;
    List<Estanteria> listaEstanterias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        navHome            = findViewById(R.id.navHome);
        navLista           = findViewById(R.id.navLista);
        navLupa            = findViewById(R.id.navLupa);
        navPerfil          = findViewById(R.id.navPerfil);
        btnMoreOptionsHome = findViewById(R.id.btnMoreOptionsHome);
        rvEstanterias      = findViewById(R.id.rvEstanterias);

        // ── Configurar RecyclerView ──────────────────────────────────────────
        listaEstanterias = new ArrayList<>();
        adapter = new EstanteriaAdapter(listaEstanterias, new EstanteriaAdapter.OnEstanteriaListener() {

            @Override
            public void onAgregarLibro(Estanteria estanteria) {
                // Abrir AnadirLibroActivity pasando el id de la estantería destino
                Intent intent = new Intent(HomeActivity.this, AnadirLibroActivity.class);
                intent.putExtra("estanteria_id", estanteria.getId());
                startActivity(intent);
            }

            @Override
            public void onAgregarEstanteria() {
                // TODO: mostrar un diálogo para pedir el título y llamar crear_estanteria.php
                Toast.makeText(HomeActivity.this, "Agregar estantería (próximamente)", Toast.LENGTH_SHORT).show();
            }
        });

        rvEstanterias.setLayoutManager(new LinearLayoutManager(this));
        rvEstanterias.setAdapter(adapter);

        // TODO: cargar las estanterías del usuario desde la API aquí

        // ── Navegación inferior ──────────────────────────────────────────────
        navHome.setOnClickListener(v -> {});
        navLista.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, ListasActivity.class));
            finish();
        });
        navLupa.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, BuscarActivity.class));
            finish();
        });
        navPerfil.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, PerfilActivity.class));
            finish();
        });

        btnMoreOptionsHome.setOnClickListener(v -> {});
    }
}