package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 1. Enlazamos con tu diseño XML (activity_home.xml)
        setContentView(R.layout.home);

        // 2. Vinculamos los 4 iconos de la barra inferior (BottomNav)
        ImageView navHome = findViewById(R.id.navHome);
        ImageView navLista = findViewById(R.id.navLista);
        ImageView navLupa = findViewById(R.id.navLupa);
        ImageView navPerfil = findViewById(R.id.navPerfil);

        // 3. Vinculamos los componentes de la barra superior y botones flotantes
        ImageView btnMoreOptionsHome = findViewById(R.id.btnMoreOptionsHome);
        FloatingActionButton fabAddLibreria1 = findViewById(R.id.fabAddLibreria1);
        FloatingActionButton fabAddLibreria2 = findViewById(R.id.fabAddLibreria2);

        // ================= CIRCUITO DE NAVEGACIÓN INFERIOR =================

        // 1. Pantalla Actual (Home)
        navHome.setOnClickListener(v -> {
            // Ya estás aquí, no es necesario hacer nada
        });

        // 2. Viajar a la pantalla de Listas
        navLista.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ListaAdapter.class); // Verifica si se llama ListasActivity o ListaActivity
            startActivity(intent);
            finish(); // Cierra Home para liberar espacio en memoria
        });

        // 3. Viajar a la pantalla de Buscar
        navLupa.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, BuscarActivity.class);
            startActivity(intent);
            finish();
        });

        // 4. Viajar a la pantalla de Perfil
        navPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PerfilActivity.class);
            startActivity(intent);
            finish();
        });

        // ================= ACCIONES EXTRAS DE LA HOME =================

        btnMoreOptionsHome.setOnClickListener(v -> {
            // Aquí se programará el PopupMenu de opciones más adelante
        });

        fabAddLibreria1.setOnClickListener(v -> {
            // Cuando programes la inserción, te mandará al formulario
            Intent intent = new Intent(HomeActivity.this, AnadirLibroActivity.class);
            startActivity(intent);
        });

        fabAddLibreria2.setOnClickListener(v -> {
            // Lo mismo para la segunda librería
            Intent intent = new Intent(HomeActivity.this, AnadirLibroActivity.class);
            startActivity(intent);
        });

        // ================= ACCIONES EXTRAS DE LA HOME =================

// Al presionar el botón de añadir de la primera librería...
        fabAddLibreria1.setOnClickListener(v -> {
            // Creamos el viaje hacia la pantalla de añadir libro
            Intent intent = new Intent(HomeActivity.this, AnadirLibroActivity.class);
            startActivity(intent);
            // NOTA: Aquí NO ponemos finish(), para que cuando el usuario guarde o cancele,
            // pueda regresar a la Home de forma natural.
        });

// Si quieres que el segundo botón haga exactamente lo mismo:
        fabAddLibreria2.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AnadirLibroActivity.class);
            startActivity(intent);
        });
    }
}