package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public void buscarLibroPorTitulo(String tituloBuscado) {
        String url = "http://10.0.2.2/api_biblioteca/api/buscar_libros.php";
        // 1. Creamos el JSON con el título que queremos buscar
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("titulo", tituloBuscado);
            // NOTA: Si prefieres buscar por ISBN, cambia la línea de arriba por:
            // jsonBody.put("isbn", isbnBuscado);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 2. Usamos JsonObjectRequest con método POST
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // El PHP nos devuelve el mismo formato que obtener_libros.php
                            // Un JSON con un arreglo llamado "libros"
                            JSONArray jsonArray = response.getJSONArray("libros");

                            if(jsonArray.length() == 0) {
                                // No se encontró ningún libro con ese nombre
                                return;
                            }
                            // Recorremos los resultados
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject libroJson = jsonArray.getJSONObject(i);
                                String isbn = libroJson.getString("isbn");
                                String titulo = libroJson.getString("titulo");
                                String autor = libroJson.getString("autor");
                                String editorial = libroJson.getString("editorial");
                                // Aquí agregas el libro a tu lista y actualizas tu RecyclerView
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        // 3. Lo añadimos a la cola de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}