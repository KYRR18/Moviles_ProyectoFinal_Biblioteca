package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.os.Bundle;

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
    ArrayList<Libro> listaLibros = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rv = findViewById(R.id.rvFiles);
        rv.setLayoutManager(new GridLayoutManager(this, 1));
        obtenerLibros();
        ListaAdapter ad = new ListaAdapter(listaLibros);
        rv.setAdapter(ad);
    }

    public void obtenerLibros() {
        String url = "http://10.0.2.2/api_biblioteca/api/obtener_libros.php";
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
                                // Obtenemos el libro actual de la posición 'i'
                                JSONObject libroJson = jsonArray.getJSONObject(i);
                                // Extraemos cada campo
                                String isbn = libroJson.getString("isbn");
                                String titulo = libroJson.getString("titulo");
                                String autor = libroJson.getString("autor");
                                String editorial = libroJson.getString("editorial");
                                // Creamos nuestro objeto Java y lo añadimos a nuestra lista
                                Libro nuevoLibro = new Libro(titulo, autor, editorial, isbn);
                                listaLibros.add(nuevoLibro);
                            }
                            // Al terminar de cargar, avisamos a nuestro RecyclerView/ListView que se actualice
                            // miAdapter.notifyDataSetChanged();
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