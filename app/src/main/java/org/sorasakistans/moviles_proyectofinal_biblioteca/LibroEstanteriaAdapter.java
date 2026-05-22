package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LibroEstanteriaAdapter extends RecyclerView.Adapter<LibroEstanteriaAdapter.ViewHolder> {

    private final List<Libro> libros;
    private final int estanteriaActualId; // Necesario para decirle a la DB de qué estantería lo vamos a quitar

    public LibroEstanteriaAdapter(List<Libro> libros, int estanteriaActualId) {
        this.libros = libros;
        this.estanteriaActualId = estanteriaActualId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_libro_estanteria, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Libro libro = libros.get(position);
        holder.tvTitulo.setText(libro.getTitulo());
        holder.tvAutor.setText(libro.getAutor());

        // Al pulsar sobre el libro, abrir el primer diálogo de opciones
        holder.itemView.setOnClickListener(v -> mostrarDialogoOpciones(v.getContext(), libro));
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvAutor;
        ShapeableImageView imgPortada;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo   = itemView.findViewById(R.id.tvTituloItem);
            tvAutor    = itemView.findViewById(R.id.tvAutorItem);
            imgPortada = itemView.findViewById(R.id.imgPortadaItem);
        }
    }

    // ── 1. Primer Diálogo: Elegir acción ─────────────────────────────────────
    private void mostrarDialogoOpciones(Context ctx, Libro libro) {
        String[] opciones = {"Editar libro", "Mover a otra estantería"};

        new AlertDialog.Builder(ctx)
                .setTitle("Opciones: " + libro.getTitulo())
                .setItems(opciones, (dialog, which) -> {
                    if (which == 0) {
                        // Opción Editar
                        Intent intent = new Intent(ctx, ModificarLibroActivity.class);
                        intent.putExtra("isbn", libro.getIsbn());
                        ctx.startActivity(intent);
                    } else if (which == 1) {
                        // Opción Mover
                        cargarEstanteriasYMostrarDialogo(ctx, libro);
                    }
                })
                .show();
    }

    // ── 2. Cargar estanterías para el segundo diálogo ────────────────────────
    private void cargarEstanteriasYMostrarDialogo(Context ctx, Libro libro) {
        SharedPreferences prefs = ctx.getSharedPreferences("sesion_usuario", Context.MODE_PRIVATE);
        int usuarioId = prefs.getInt("usuario_id", -1);

        if (usuarioId == -1) {
            Toast.makeText(ctx, "Error: no hay sesión activa.", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = ctx.getString(R.string.API_OBTENER_ESTANTERIAS_USUARIO) + "?usuario_id=" + usuarioId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("estanterias");
                        if (jsonArray.length() == 0) {
                            Toast.makeText(ctx, "No tienes más estanterías.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Filtrar la estantería actual para no mostrarla en la lista de destino
                        int count = 0;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (jsonArray.getJSONObject(i).getInt("id") != estanteriaActualId) count++;
                        }

                        if (count == 0) {
                            Toast.makeText(ctx, "No hay otras estanterías disponibles.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int[] ids       = new int[count];
                        String[] nombres = new String[count];
                        int index = 0;

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject e = jsonArray.getJSONObject(i);
                            int id = e.getInt("id");
                            if (id != estanteriaActualId) {
                                ids[index]     = id;
                                nombres[index] = e.getString("titulo");
                                index++;
                            }
                        }

                        mostrarDialogoMover(ctx, libro, ids, nombres);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ctx, "Error al cargar estanterías.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(ctx, "Error de red al cargar estanterías.", Toast.LENGTH_SHORT).show();
                }
        );

        Volley.newRequestQueue(ctx).add(request);
    }

    // ── 3. Segundo Diálogo: Elegir estantería destino ────────────────────────
    private void mostrarDialogoMover(Context ctx, Libro libro, int[] ids, String[] nombres) {
        final int[] seleccionado = {-1};

        new AlertDialog.Builder(ctx)
                .setTitle("Mover a...")
                .setSingleChoiceItems(nombres, -1, (dialog, which) -> {
                    seleccionado[0] = which;
                })
                .setPositiveButton("Mover", (dialog, which) -> {
                    if (seleccionado[0] == -1) {
                        Toast.makeText(ctx, "Selecciona una estantería.", Toast.LENGTH_SHORT).show();
                    } else {
                        moverLibroEnBD(ctx, libro, ids[seleccionado[0]]);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // ── 4. Mover libro: Buscar libro_id y luego actualizar BD ────────────────
    private void moverLibroEnBD(Context ctx, Libro libro, int nuevaEstanteriaId) {
        String urlBuscar = ctx.getString(R.string.API_BUSCAR);
        JSONObject bodyBuscar = new JSONObject();
        try { bodyBuscar.put("isbn", libro.getIsbn()); }
        catch (JSONException e) { e.printStackTrace(); return; }

        JsonObjectRequest buscarRequest = new JsonObjectRequest(
                Request.Method.POST, urlBuscar, bodyBuscar,
                responseBuscar -> {
                    try {
                        JSONArray arr = responseBuscar.getJSONArray("libros");
                        if (arr.length() == 0) return;
                        int libroId = arr.getJSONObject(0).getInt("id");

                        String urlMover = ctx.getString(R.string.API_CAMBIAR_ESTANTERIA);
                        JSONObject bodyMover = new JSONObject();
                        bodyMover.put("libro_id", libroId);
                        bodyMover.put("vieja_estanteria_id", estanteriaActualId);
                        bodyMover.put("nueva_estanteria_id", nuevaEstanteriaId);

                        JsonObjectRequest moverRequest = new JsonObjectRequest(
                                Request.Method.PUT, urlMover, bodyMover,
                                responseMover -> {
                                    try {
                                        if (responseMover.getBoolean("exito")) {
                                            Toast.makeText(ctx, "Libro movido con éxito", Toast.LENGTH_SHORT).show();
                                            if (ctx instanceof HomeActivity) {
                                                ((HomeActivity) ctx).recargarDatos();
                                            }
                                        }
                                    } catch (JSONException e) { e.printStackTrace(); }
                                },
                                error -> {
                                    Toast.makeText(ctx, "Error al mover el libro.", Toast.LENGTH_SHORT).show();
                                    error.printStackTrace();
                                }
                        );
                        Volley.newRequestQueue(ctx).add(moverRequest);

                    } catch (JSONException e) { e.printStackTrace(); }
                },
                error -> error.printStackTrace()
        );

        Volley.newRequestQueue(ctx).add(buscarRequest);
    }
}
