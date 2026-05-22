package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PerfilActivity extends AppCompatActivity {
    ImageView btnBackPerfil, btnMoreOptionsPerfil;
    MaterialButton btnEditProfile, btnSettings, btnLogout;
    ImageView navPerfil, navHome, navLista, navLupa;
    TextView userAdr, userName;
    
    RecyclerView rvEstanterias;
    EstanteriaPerfilAdapter adapter;
    List<Estanteria> listaEstanterias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);

        btnBackPerfil = findViewById(R.id.btnBackPerfil);
        btnMoreOptionsPerfil = findViewById(R.id.btnMoreOptionsPerfil);

        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnSettings = findViewById(R.id.btnSettings);
        btnLogout = findViewById(R.id.btnLogout);

        navHome = findViewById(R.id.navHome);
        navLista = findViewById(R.id.navLista);
        navLupa = findViewById(R.id.navLupa);
        navPerfil = findViewById(R.id.navPerfil);

        userName = findViewById(R.id.tvUserName);
        userAdr = findViewById(R.id.tvUserEmail);

        rvEstanterias = findViewById(R.id.rvEstanteriasPerfil);

        // Cargar datos de sesión desde SharedPreferences
        AtomicReference<SharedPreferences> prefs = new AtomicReference<>(getSharedPreferences("sesion_usuario", MODE_PRIVATE));
        String username = prefs.get().getString("username", "Usuario");
        String email = prefs.get().getString("email", "");
        userName.setText(username);
        userAdr.setText(email);

        // ── Configurar RecyclerView ──────────────────────────────────────────
        listaEstanterias = new ArrayList<>();
        adapter = new EstanteriaPerfilAdapter(listaEstanterias, new EstanteriaPerfilAdapter.OnEstanteriaAccionListener() {
            @Override
            public void onEditar(Estanteria estanteria, int position) {
                mostrarDialogoEditar(estanteria, position);
            }

            @Override
            public void onEliminar(Estanteria estanteria, int position) {
                mostrarDialogoEliminar(estanteria, position);
            }
        });
        rvEstanterias.setLayoutManager(new LinearLayoutManager(this));
        rvEstanterias.setAdapter(adapter);

        // Cargar estanterías
        cargarEstanterias();

        btnBackPerfil.setOnClickListener(this::goToHome);

        // ================= CIRCUITO DE NAVEGACIÓN INFERIOR =================
        navHome.setOnClickListener(this::goToHome);
        navLista.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, ListasActivity.class); // Verifica si se llama ListasActivity o ListaActivity
            startActivity(intent);
            finish();
        });
        navLupa.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, BuscarActivity.class);
            startActivity(intent);
            finish();
        });
        navPerfil.setOnClickListener(v -> {});

        btnLogout.setOnClickListener(v -> {
            // Limpiar la sesión guardada
            prefs.set(getSharedPreferences("sesion_usuario", MODE_PRIVATE));
            prefs.get().edit().clear().apply();

            Intent intent = new Intent(PerfilActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PerfilActivity.this, EditPerfil.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void goToHome(View v){
        Intent intent = new Intent(PerfilActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    // ── Lógica de Estanterías ────────────────────────────────────────────────

    private void cargarEstanterias() {
        SharedPreferences prefs = getSharedPreferences("sesion_usuario", MODE_PRIVATE);
        int usuarioId = prefs.getInt("usuario_id", -1);

        if (usuarioId == -1) return;

        String url = getString(R.string.API_OBTENER_ESTANTERIAS_USUARIO) + "?usuario_id=" + usuarioId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        listaEstanterias.clear();
                        JSONArray jsonArray = response.getJSONArray("estanterias");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject e = jsonArray.getJSONObject(i);
                            listaEstanterias.add(new Estanteria(e.getInt("id"), e.getString("titulo")));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace()
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void mostrarDialogoEditar(Estanteria estanteria, int position) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_nueva_estanteria, null);
        TextInputEditText etNombre = dialogView.findViewById(R.id.etNombreEstanteria);
        etNombre.setText(estanteria.getTitulo());

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Editar Estantería")
                .setView(dialogView)
                .setPositiveButton("Guardar", null) // null para validación manual
                .setNegativeButton("Cancelar", null)
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String nuevoTitulo = etNombre.getText() != null ? etNombre.getText().toString().trim() : "";
                if (nuevoTitulo.isEmpty()) {
                    etNombre.setError("El nombre no puede estar vacío");
                    etNombre.requestFocus();
                } else if (nuevoTitulo.equals(estanteria.getTitulo())) {
                    dialog.dismiss(); // No hubo cambios
                } else {
                    dialog.dismiss();
                    renombrarEstanteriaEnBD(estanteria.getId(), nuevoTitulo, position);
                }
            });
        });
        dialog.show();
    }

    private void renombrarEstanteriaEnBD(int estanteriaId, String nuevoTitulo, int position) {
        String url = getString(R.string.API_EDITAR_ESTANTERIA);

        
        JSONObject body = new JSONObject();
        try {
            body.put("estanteria_id", estanteriaId);
            body.put("nuevo_titulo", nuevoTitulo);
        } catch (JSONException e) { e.printStackTrace(); return; }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT, url, body,
                response -> {
                    try {
                        if (response.getBoolean("exito")) {
                            listaEstanterias.set(position, new Estanteria(estanteriaId, nuevoTitulo));
                            adapter.notifyItemChanged(position);
                            Toast.makeText(this, "Nombre actualizado", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) { e.printStackTrace(); }
                },
                error -> Toast.makeText(this, "Error al editar", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void mostrarDialogoEliminar(Estanteria estanteria, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Estantería")
                .setMessage("¿Estás seguro de que deseas eliminar la estantería \"" + estanteria.getTitulo() + "\"? Todos los libros almacenados en ella se quitarán de esta lista.")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarEstanteriaEnBD(estanteria.getId(), position))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarEstanteriaEnBD(int estanteriaId, int position) {
        String url = getString(R.string.API_ELIMINAR_ESTANTERIA);
        
        JSONObject body = new JSONObject();
        try { body.put("estanteria_id", estanteriaId); } 
        catch (JSONException e) { e.printStackTrace(); return; }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, body,
                response -> {
                    try {
                        if (response.getBoolean("exito")) {
                            listaEstanterias.remove(position);
                            adapter.notifyItemRemoved(position);
                            // También notificar el cambio en el rango para que los índices internos del adapter se actualicen
                            adapter.notifyItemRangeChanged(position, listaEstanterias.size());
                            Toast.makeText(this, "Estantería eliminada", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) { e.printStackTrace(); }
                },
                error -> Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }
}