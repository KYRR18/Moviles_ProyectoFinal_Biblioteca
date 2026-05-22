package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class ListasActivity extends AppCompatActivity {
    RecyclerView rv;
    ArrayList<Libro> listaLibros = new ArrayList<Libro>();
    ListaAdapter ad;
    ImageView backIV, refreshIV, optionsIV;
    ImageView homeIV,listIV,searchIV,userIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listas);
        rv = findViewById(R.id.rvFiles);
        rv.setLayoutManager(new GridLayoutManager(this, 1));
        ad = new ListaAdapter(listaLibros);
        obtenerLibros();
        rv.setAdapter(ad);

        backIV = findViewById(R.id.btnBack);
        refreshIV = findViewById(R.id.btnRefresh);
        optionsIV = findViewById(R.id.btnMoreOptions);

        homeIV = findViewById(R.id.navHome);
        listIV = findViewById(R.id.navLista);
        searchIV = findViewById(R.id.navBuscar);
        userIV = findViewById(R.id.navUser);

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {goToHome(view);}
        });
        refreshIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerLibros();
                Toast.makeText(ListasActivity.this, "Se Refresco la lista", Toast.LENGTH_SHORT).show();
            }
        });
        optionsIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {goToHome(view);}
        });
        homeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {goToHome(view);}
        });
        listIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerLibros();
                Toast.makeText(ListasActivity.this, "Se Refresco la lista", Toast.LENGTH_SHORT).show();
            }
        });
        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListasActivity.this, BuscarActivity.class);
                startActivity(intent);
                finish();
            }
        });
        userIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListasActivity.this, PerfilActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void goToHome(View v){
        Intent intent = new Intent(ListasActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
    public void obtenerLibros() {
        String url = getString(R.string.API_ALLBOOKS);
        // Creamos la petición GET. Como es GET, no enviamos cuerpo (pasamos null).
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // ¡AQUÍ ES DONDE LLEGA EL JSON!
                        // La variable 'response' contiene exactamente lo que devolvió PHP.
                        try {
                            // Extraemos el arreglo llamado "libros" del JSON de respuesta
                            JSONArray jsonArray = response.getJSONArray("libros");
                            // Limpiamos nuestra lista local antes de agregar los nuevos
                                listaLibros.clear();
                            // Recorremos el arreglo de libros uno por uno
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject libroJson = jsonArray.getJSONObject(i);
                                String isbn = libroJson.getString("isbn");
                                String titulo = libroJson.getString("titulo");
                                String autor = libroJson.getString("autor");
                                String editorial = libroJson.getString("editorial");
                                // Creamos nuestro objeto Java y lo añadimos a nuestra lista
                                listaLibros.add(new Libro(titulo, autor, editorial, isbn));
                            }
                            if (ad != null) ad.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Hubo un error procesando el JSON
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Este método se ejecuta si no hay internet o si el servidor está apagado/da error.
                error.printStackTrace();
            }
        });
        // Finalmente, añadimos la petición a la cola para que se ejecute
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}