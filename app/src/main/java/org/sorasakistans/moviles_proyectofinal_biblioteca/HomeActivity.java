package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
                Intent intent = new Intent(HomeActivity.this, AnadirLibroActivity.class);
                intent.putExtra("estanteria_id", estanteria.getId());
                startActivity(intent);
            }

            @Override
            public void onAgregarEstanteria() {
                mostrarDialogoNuevaEstanteria();
            }
        });

        rvEstanterias.setLayoutManager(new LinearLayoutManager(this));
        rvEstanterias.setAdapter(adapter);

        // Los datos ahora se cargan en onResume() para que siempre estén frescos

        // ── Navegación inferior ──────────────────────────────────────────────
        navHome.setOnClickListener(v -> {recoverShelfs();});
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

    @Override
    protected void onResume() {
        super.onResume();
        // Esto se ejecuta cada vez que la pantalla vuelve al frente (ej. regresando de AnadirLibroActivity)
        recoverShelfs();
    }

    // Método público para que los Adapters puedan forzar una recarga si modifican algo en BD
    public void recargarDatos() {
        recoverShelfs();
    }

    // ── Diálogo para crear nueva estantería ─────────────────────────────────
    private void mostrarDialogoNuevaEstanteria() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_nueva_estanteria, null);
        TextInputEditText etNombre = dialogView.findViewById(R.id.etNombreEstanteria);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Nueva Estantería")
                .setView(dialogView)
                .setPositiveButton("Crear", null) // null para controlar el cierre manualmente
                .setNegativeButton("Cancelar", null)
                .create();

        // Sobrescribir el listener del botón positivo para poder validar ANTES de cerrar
        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String titulo = etNombre.getText() != null
                        ? etNombre.getText().toString().trim()
                        : "";

                if (titulo.isEmpty()) {
                    etNombre.setError("El nombre no puede estar vacío");
                    etNombre.requestFocus();
                } else {
                    dialog.dismiss();
                    crearEstanteria(titulo);
                }
            });
        });

        dialog.show();
    }

    // ── Llamada a la API para crear la estantería ────────────────────────────
    private void crearEstanteria(String titulo) {
        String url = getString(R.string.API_CREAR_ESTANTERIA);

        // Leer el usuario_id guardado en sesión
        SharedPreferences prefs = getSharedPreferences("sesion_usuario", MODE_PRIVATE);
        int usuarioId = prefs.getInt("usuario_id", -1);

        if (usuarioId == -1) {
            Toast.makeText(this, "Error: no hay sesión activa.", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject body = new JSONObject();
        try {
            body.put("titulo", titulo);
            body.put("usuario_id", usuarioId);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, body,
                response -> {
                    try {
                        if (response.getBoolean("exito")) {
                            int nuevoId = response.getInt("id");
                            Estanteria nueva = new Estanteria(nuevoId, titulo);
                            adapter.agregarEstanteria(nueva); // actualiza el RecyclerView
                            Toast.makeText(this, "Estantería \"" + titulo + "\" creada.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(this, "Error al crear la estantería.", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
    //Lamado API para popular lista
    private void recoverShelfs(){
        SharedPreferences prefs = getSharedPreferences("sesion_usuario", MODE_PRIVATE);
        int usuarioId = prefs.getInt("usuario_id", -1);

        if (usuarioId == -1) {
            Toast.makeText(this, "Error: no hay sesión activa.", Toast.LENGTH_SHORT).show();
            return;
        }

        // PHP uses $_GET so the id must be a query param in the URL, not a JSON body
        String url = getString(R.string.API_OBTENER_ESTANTERIAS_USUARIO) + "?usuario_id=" + usuarioId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        listaEstanterias.clear(); // Limpiar para evitar duplicados al recargar
                        
                        JSONArray jsonArray = response.getJSONArray("estanterias");

                        if (jsonArray.length() == 0) {
                            Toast.makeText(HomeActivity.this, "No tienes estanterías creadas.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject e = jsonArray.getJSONObject(i);
                            int ids     = e.getInt("id");
                            String nombres= e.getString("titulo");
                            Estanteria shelf = new Estanteria(ids, nombres);
                            listaEstanterias.add(shelf);
                            recoverBooks(shelf); // async: fills shelf.libros when response arrives
                        }
                        if (adapter != null) adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(HomeActivity.this, "Error al cargar estanterías.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(HomeActivity.this, "Error de red al cargar estanterías.", Toast.LENGTH_SHORT).show();
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
    // Async: fetches books for a shelf and sets them directly on the Estanteria object
    private void recoverBooks(Estanteria shelf) {
        String url = getString(R.string.API_OBTENER_LIBROS_ESTANTERIA) + "?estanteria_id=" + shelf.getId();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        if (!response.getBoolean("exito")) return; // empty shelf (404 case)

                        JSONArray jsonArray = response.getJSONArray("libros");
                        List<Libro> listaShelf = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String jsTitle    = obj.getString("titulo");
                            String jsAutor    = obj.getString("autor");
                            String jsEditorial = obj.getString("editorial");
                            String jsIsbn     = obj.getString("isbn");
                            listaShelf.add(new Libro(jsTitle, jsAutor, jsEditorial, jsIsbn));
                        }

                        shelf.setLibros(listaShelf);
                        if (adapter != null) adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace()
        );

        Volley.newRequestQueue(this).add(request);
    }
}