package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BusquedaAdapter extends RecyclerView.Adapter<BusquedaAdapter.ViewHolderBusqueda> {

    private ArrayList<Libro> booklist;

    public BusquedaAdapter(ArrayList<Libro> list) {
        this.booklist = list;
    }

    @NonNull
    @Override
    public ViewHolderBusqueda onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_libro_busqueda, parent, false);
        return new ViewHolderBusqueda(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderBusqueda holder, int position) {
        holder.asignarDatos(booklist.get(position));
    }

    @Override
    public int getItemCount() {
        return (booklist != null) ? booklist.size() : 0;
    }

    public class ViewHolderBusqueda extends RecyclerView.ViewHolder {
        TextView titulo, autor;
        ConstraintLayout layoutLibro;
        ImageView ivEditBook, ivAddShelf;

        public ViewHolderBusqueda(@NonNull View itemView) {
            super(itemView);
            titulo      = itemView.findViewById(R.id.tvTituloItem);
            autor       = itemView.findViewById(R.id.tvAutorItem);
            layoutLibro = itemView.findViewById(R.id.layoutLibro);
            ivEditBook  = itemView.findViewById(R.id.ivEditbook);
            ivAddShelf  = itemView.findViewById(R.id.ivAddshelf);
        }

        public void asignarDatos(Libro book) {
            titulo.setText(book.getTitulo());
            autor.setText(book.getAutor());

            // ── Editar libro: igual que en ListaAdapter ──────────────────────
            ivEditBook.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), ModificarLibroActivity.class);
                intent.putExtra("isbn", book.getIsbn());
                v.getContext().startActivity(intent);
            });

            // ── Agregar a estantería: cargar estanterías y mostrar diálogo ───
            ivAddShelf.setOnClickListener(v -> {
                cargarEstanteriasYMostrarDialogo(v.getContext(), book);
            });
        }
    }

    // ── Paso 1: Cargar las estanterías del usuario desde la API ─────────────
    private void cargarEstanteriasYMostrarDialogo(Context ctx, Libro book) {
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
                            Toast.makeText(ctx, "No tienes estanterías creadas.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Construir listas paralelas de ids y nombres
                        int[] ids       = new int[jsonArray.length()];
                        String[] nombres = new String[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject e = jsonArray.getJSONObject(i);
                            ids[i]     = e.getInt("id");
                            nombres[i] = e.getString("titulo");
                        }

                        mostrarDialogoEstanterias(ctx, book, ids, nombres);

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

        RequestQueue queue = Volley.newRequestQueue(ctx);
        queue.add(request);
    }

    // ── Paso 2: Mostrar AlertDialog con lista de selección única ────────────
    private void mostrarDialogoEstanterias(Context ctx, Libro book, int[] ids, String[] nombres) {
        final int[] seleccionado = {-1}; // índice seleccionado, empieza en -1 (ninguno)

        new AlertDialog.Builder(ctx)
                .setTitle("Agregar a estantería")
                // setSingleChoiceItems = lista radio-button: solo se puede elegir uno
                .setSingleChoiceItems(nombres, -1, (dialog, which) -> {
                    seleccionado[0] = which;
                })
                .setPositiveButton("Agregar", (dialog, which) -> {
                    if (seleccionado[0] == -1) {
                        Toast.makeText(ctx, "Selecciona una estantería.", Toast.LENGTH_SHORT).show();
                    } else {
                        int estanteriaId = ids[seleccionado[0]];
                        asignarLibroAEstanteria(ctx, book, estanteriaId);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // ── Paso 3: Llamar a la API para crear la relación libro-estantería ──────
    private void asignarLibroAEstanteria(Context ctx, Libro book, int estanteriaId) {
        // Necesitamos el libro_id — la API buscar_libros devuelve isbn pero no id.
        // Por eso primero buscamos el libro por isbn para obtener su id.
        String urlBuscar = ctx.getString(R.string.API_BUSCAR);
        JSONObject bodyBuscar = new JSONObject();
        try { bodyBuscar.put("isbn", book.getIsbn()); }
        catch (JSONException e) { e.printStackTrace(); return; }

        JsonObjectRequest buscarRequest = new JsonObjectRequest(
                Request.Method.POST, urlBuscar, bodyBuscar,
                responseBuscar -> {
                    try {
                        JSONArray libros = responseBuscar.getJSONArray("libros");
                        if (libros.length() == 0) {
                            Toast.makeText(ctx, "Libro no encontrado en la BD.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int libroId = libros.getJSONObject(0).getInt("id");

                        // Ahora sí enviamos la asignación
                        String urlAsignar = ctx.getString(R.string.API_ASIGNAR_LIBRO_ESTANTERIA);
                        JSONObject bodyAsignar = new JSONObject();
                        bodyAsignar.put("libro_id", libroId);
                        bodyAsignar.put("estanteria_id", estanteriaId);

                        JsonObjectRequest asignarRequest = new JsonObjectRequest(
                                Request.Method.POST, urlAsignar, bodyAsignar,
                                responseAsignar -> {
                                    try {
                                        if (responseAsignar.getBoolean("exito")) {
                                            Toast.makeText(ctx, "¡Libro añadido a la estantería!", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) { e.printStackTrace(); }
                                },
                                error -> {
                                    if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                                        Toast.makeText(ctx, "El libro ya está en esa estantería.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ctx, "Error al asignar el libro.", Toast.LENGTH_SHORT).show();
                                    }
                                    error.printStackTrace();
                                }
                        );
                        Volley.newRequestQueue(ctx).add(asignarRequest);

                    } catch (JSONException e) { e.printStackTrace(); }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(ctx, "Error al buscar el libro.", Toast.LENGTH_SHORT).show();
                }
        );

        Volley.newRequestQueue(ctx).add(buscarRequest);
    }
}
