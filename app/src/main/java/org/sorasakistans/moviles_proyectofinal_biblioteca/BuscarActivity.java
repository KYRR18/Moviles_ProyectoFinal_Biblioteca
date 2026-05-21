package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BuscarActivity extends AppCompatActivity {
    ArrayList<Libro> listalibros = new ArrayList<>();
    RecyclerView rv;
    //EditText searchbar;
    BusquedaAdapter ad;
    ImageView btnBack, btnPuntos;
    EditText etBuscar;
    ImageView navHome, navLista, navLupa, navPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buscar_libro);
        btnBack = findViewById(R.id.btnBack);
        btnPuntos = findViewById(R.id.btnPuntos);
        etBuscar = findViewById(R.id.etBuscar);

        navHome = findViewById(R.id.navHome);
        navLista = findViewById(R.id.navLista);
        navLupa = findViewById(R.id.navLupa);
        navPerfil = findViewById(R.id.navPerfil);

        btnBack.setOnClickListener(this::goToHome);
        navHome.setOnClickListener(this::goToHome);

        navLista.setOnClickListener(v -> {
            Intent intent = new Intent(BuscarActivity.this, ListaAdapter.class); // Cambia por el nombre exacto si se llama diferente tu clase de Listas
            startActivity(intent);
            finish();
        });

        navLupa.setOnClickListener(v -> {etBuscar.setText("");});

        navPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(BuscarActivity.this, PerfilActivity.class);
            startActivity(intent);
            finish();
        });

        rv = findViewById(R.id.rvResultadosBusqueda);
        rv.setLayoutManager(new LinearLayoutManager(this));
        ad = new BusquedaAdapter(listalibros);
        rv.setAdapter(ad);

        // Listener para cambios de texto en tiempo real
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Llamamos a la función de búsqueda con el texto actual
                buscarLibroPorTitulo(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    public void goToHome(View v){
        Intent intent = new Intent(BuscarActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
    public void buscarLibroPorTitulo(String tituloBuscado) {
        String url = "http://10.0.2.2/api_biblioteca/api/buscar_libros.php";

        listalibros.clear();

        if (tituloBuscado.trim().isEmpty()) {
            ad.notifyDataSetChanged();
            return;
        }

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("titulo", tituloBuscado);
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

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject libroJson = jsonArray.getJSONObject(i);
                                String isbn = libroJson.getString("isbn");
                                String titulo = libroJson.getString("titulo");
                                String autor = libroJson.getString("autor");
                                String editorial = libroJson.getString("editorial");
                                Libro nuevoLibro = new Libro(titulo, autor, editorial, isbn);
                                listalibros.add(nuevoLibro);
                            }
                            ad.notifyDataSetChanged();
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