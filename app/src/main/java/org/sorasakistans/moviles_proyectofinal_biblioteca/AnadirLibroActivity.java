package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
import org.json.JSONException;
import org.json.JSONObject;

public class AnadirLibroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.anadir_libro);
    }

    public void crearLibro(String isbn, String titulo, String autor, String editorial,View v) {
        String url = "http://10.0.2.2/api_biblioteca/api/crear_libro.php";//VM RECUERDA VM ONLY
        // 1. Crear el objeto JSON que enviaremos
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("isbn", isbn);
            jsonBody.put("titulo", titulo);
            jsonBody.put("autor", autor);
            jsonBody.put("editorial", editorial);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 2. Crear la petición
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean exito = response.getBoolean("exito");
                            String mensaje = response.getString("mensaje");
                            // Mostrar mensaje al usuario (ej. Toast)
                            //Toast tosty = new Toast(v.getContext());
                            //tosty.
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar error de red o servidor (400, 500, etc.)
                error.printStackTrace();
            }
        });
        // 3. Añadir la petición a la cola de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}