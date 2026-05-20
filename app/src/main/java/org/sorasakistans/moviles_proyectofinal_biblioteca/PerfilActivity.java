package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class PerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 1. Cargamos el diseño XML de tu Perfil
        setContentView(R.layout.perfil);

        // 2. Vinculamos la barra superior (TopBar)
        ImageView btnBackPerfil = findViewById(R.id.btnBackPerfil);
        ImageView btnMoreOptionsPerfil = findViewById(R.id.btnMoreOptionsPerfil);

        // 3. Vinculamos los botones de opciones del cuerpo del perfil
        MaterialButton btnEditProfile = findViewById(R.id.btnEditProfile);
        MaterialButton btnSettings = findViewById(R.id.btnSettings);
        MaterialButton btnLogout = findViewById(R.id.btnLogout);

        // 4. Vinculamos los 4 iconos de la barra inferior (BottomNav)
        ImageView navHome = findViewById(R.id.navHome);
        ImageView navLista = findViewById(R.id.navLista);
        ImageView navLupa = findViewById(R.id.navLupa);
        ImageView navPerfil = findViewById(R.id.navPerfil);

        // ================= ACCIONES DE LA BARRA SUPERIOR =================

        // La flecha izquierda te saca de perfil y te regresa a la pantalla anterior
        btnBackPerfil.setOnClickListener(v -> {
            finish();
        });

        // ================= CIRCUITO DE NAVEGACIÓN INFERIOR =================

        // 1. Viajar a la pantalla de Home
        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, HomeActivity.class);
            startActivity(intent);
            finish(); // Cierra Perfil para liberar memoria
        });

        // 2. Viajar a la pantalla de Listas
        navLista.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, ListaAdapter.class); // Verifica si se llama ListasActivity o ListaActivity
            startActivity(intent);
            finish();
        });

        // 3. Viajar a la pantalla de Buscar
        navLupa.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, BuscarActivity.class);
            startActivity(intent);
            finish();
        });

        // 4. Pantalla Actual (Perfil)
        navPerfil.setOnClickListener(v -> {
            // Ya estás aquí, no es necesario recargar la pantalla
        });

        // ================= ACCIONES DE BOTONES EXTRAS =================

        // El botón de cerrar sesión te saca por completo y limpia el historial para ir al Login
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}