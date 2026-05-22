package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class EstanteriaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM   = 0;
    private static final int TYPE_FOOTER = 1;

    private final List<Estanteria> estanterias;
    private final OnEstanteriaListener listener;

    public interface OnEstanteriaListener {
        void onAgregarLibro(Estanteria estanteria);
        void onAgregarEstanteria();
    }

    public EstanteriaAdapter(List<Estanteria> estanterias, OnEstanteriaListener listener) {
        this.estanterias = estanterias;
        this.listener    = listener;
    }

    @Override
    public int getItemCount() {
        return estanterias.size() + 1; // +1 para el footer
    }

    @Override
    public int getItemViewType(int position) {
        return (position == estanterias.size()) ? TYPE_FOOTER : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_FOOTER) {
            View view = inflater.inflate(R.layout.item_footer_agregar, parent, false);
            return new FooterViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_estanteria, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            Estanteria estanteria = estanterias.get(position);
            ItemViewHolder h = (ItemViewHolder) holder;

            // Título
            h.tvTitulo.setText(estanteria.getTitulo());

            // FAB agregar libro
            h.fabAddLibro.setOnClickListener(v -> listener.onAgregarLibro(estanteria));

            // ── RecyclerView horizontal de libros ────────────────────────────
            // Inicializar el RecyclerView interno si aún no tiene LayoutManager
            if (h.rvLibros.getLayoutManager() == null) {
                h.rvLibros.setLayoutManager(
                        new LinearLayoutManager(holder.itemView.getContext(),
                                LinearLayoutManager.HORIZONTAL, false)
                );
            }

            List<Libro> libros = estanteria.getLibros();
            if (libros == null || libros.isEmpty()) {
                // Estado vacío: mostrar placeholder, ocultar RecyclerView
                h.tvSinLibros.setVisibility(View.VISIBLE);
                h.rvLibros.setVisibility(View.GONE);
            } else {
                h.tvSinLibros.setVisibility(View.GONE);
                h.rvLibros.setVisibility(View.VISIBLE);

                LibroEstanteriaAdapter libroAdapter = new LibroEstanteriaAdapter(libros, libro -> {
                    // TODO: manejar click en libro (abrir detalle, etc.)
                });
                h.rvLibros.setAdapter(libroAdapter);
            }

        } else if (holder instanceof FooterViewHolder) {
            ((FooterViewHolder) holder).btnAgregar
                    .setOnClickListener(v -> listener.onAgregarEstanteria());
        }
    }

    // ── Método utilitario: agregar estantería nueva en caliente ──────────────
    public void agregarEstanteria(Estanteria nueva) {
        estanterias.add(nueva);
        notifyItemInserted(estanterias.size() - 1);
    }

    // ── ViewHolder para ítem de estantería ───────────────────────────────────
    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo;
        TextView tvSinLibros;
        FloatingActionButton fabAddLibro;
        RecyclerView rvLibros;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo   = itemView.findViewById(R.id.tvTituloEstanteria);
            fabAddLibro = itemView.findViewById(R.id.fabAddLibro);
            rvLibros   = itemView.findViewById(R.id.rvLibrosEstanteria);
            tvSinLibros = itemView.findViewById(R.id.tvSinLibros);
        }
    }

    // ── ViewHolder para el footer ────────────────────────────────────────────
    static class FooterViewHolder extends RecyclerView.ViewHolder {
        View btnAgregar;

        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            btnAgregar = itemView.findViewById(R.id.btnAgregarEstanteria);
        }
    }
}
