package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class BuscarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Carga tu diseño XML de búsqueda
        setContentView(R.layout.buscar_libro);

        // 1. Vincular componentes de la barra superior (TopBar)
        ImageView btnBack = findViewById(R.id.btnBack);
        ImageView btnPuntos = findViewById(R.id.btnPuntos);
        EditText etBuscar = findViewById(R.id.etBuscar);

        // 2. Vincular los 4 iconos de la barra inferior (BottomNav)
        ImageView navHome = findViewById(R.id.navHome);
        ImageView navLista = findViewById(R.id.navLista);
        ImageView navLupa = findViewById(R.id.navLupa);
        ImageView navPerfil = findViewById(R.id.navPerfil);

        // ================= ACCIÓN DE REGRESO =================
        // La flecha te regresa a la pantalla anterior del historial (usualmente Home)
        btnBack.setOnClickListener(v -> {
            finish();
        });

        // ================= CIRCUITO DE NAVEGACIÓN ENTRE PANTALLAS =================

        // 1. Ir a la pantalla de Home
        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(BuscarActivity.this, HomeActivity.class);
            startActivity(intent);
            finish(); // Cierra Buscar para que no se amontone en la memoria
        });

        // 2. Ir a la pantalla de Listas
        navLista.setOnClickListener(v -> {
            Intent intent = new Intent(BuscarActivity.this, ListaAdapter.class); // Cambia por el nombre exacto si se llama diferente tu clase de Listas
            startActivity(intent);
            finish();
        });

        // 3. Pantalla Actual (Buscar)
        navLupa.setOnClickListener(v -> {
            // Ya estás aquí. Opcional: puedes limpiar el buscador si el usuario le vuelve a picar
            etBuscar.setText("");
        });

        // 4. Ir a la pantalla de Perfil
        navPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(BuscarActivity.this, PerfilActivity.class);
            startActivity(intent);
            finish();
        });
    }
}