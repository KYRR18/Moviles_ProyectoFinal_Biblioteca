package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class EstanteriaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Tipos de vista
    private static final int TYPE_ITEM   = 0;
    private static final int TYPE_FOOTER = 1;

    private final List<Estanteria> estanterias;
    private final OnEstanteriaListener listener;

    // Interfaz para comunicar eventos al Activity/Fragment
    public interface OnEstanteriaListener {
        void onAgregarLibro(Estanteria estanteria);   // FAB dentro de la card
        void onAgregarEstanteria();                   // Botón del footer
    }

    public EstanteriaAdapter(List<Estanteria> estanterias, OnEstanteriaListener listener) {
        this.estanterias = estanterias;
        this.listener = listener;
    }

    // ─── Totale de ítems = lista + 1 footer ───────────────────────────────────
    @Override
    public int getItemCount() {
        return estanterias.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == estanterias.size()) ? TYPE_FOOTER : TYPE_ITEM;
    }

    // ─── Inflar la vista correcta según el tipo ────────────────────────────────
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

    // ─── Enlazar datos ─────────────────────────────────────────────────────────
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            Estanteria estanteria = estanterias.get(position);
            ItemViewHolder itemHolder = (ItemViewHolder) holder;

            itemHolder.tvTitulo.setText(estanteria.getTitulo());
            itemHolder.fabAddLibro.setOnClickListener(v -> listener.onAgregarLibro(estanteria));

        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            footerHolder.btnAgregar.setOnClickListener(v -> listener.onAgregarEstanteria());
        }
    }

    // ─── ViewHolder para un ítem de estantería ─────────────────────────────────
    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo;
        FloatingActionButton fabAddLibro;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo    = itemView.findViewById(R.id.tvTituloEstanteria);
            fabAddLibro = itemView.findViewById(R.id.fabAddLibro);
        }
    }

    // ─── ViewHolder para el footer (botón agregar) ─────────────────────────────
    static class FooterViewHolder extends RecyclerView.ViewHolder {
        View btnAgregar;

        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            btnAgregar = itemView.findViewById(R.id.btnAgregarEstanteria);
        }
    }

    // ─── Método utilitario para agregar una estantería en caliente ─────────────
    public void agregarEstanteria(Estanteria nueva) {
        estanterias.add(nueva);
        notifyItemInserted(estanterias.size() - 1);
    }
}
