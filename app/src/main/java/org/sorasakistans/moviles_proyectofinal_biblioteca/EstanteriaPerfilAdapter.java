package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EstanteriaPerfilAdapter extends RecyclerView.Adapter<EstanteriaPerfilAdapter.ViewHolder> {

    private final List<Estanteria> estanterias;
    private final OnEstanteriaAccionListener listener;

    public interface OnEstanteriaAccionListener {
        void onEditar(Estanteria estanteria, int position);
        void onEliminar(Estanteria estanteria, int position);
    }

    public EstanteriaPerfilAdapter(List<Estanteria> estanterias, OnEstanteriaAccionListener listener) {
        this.estanterias = estanterias;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_estanteria_perfil, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Estanteria estanteria = estanterias.get(position);
        holder.tvTitulo.setText(estanteria.getTitulo());

        holder.ivEditar.setOnClickListener(v -> listener.onEditar(estanteria, holder.getAdapterPosition()));
        holder.ivEliminar.setOnClickListener(v -> listener.onEliminar(estanteria, holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return estanterias.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo;
        ImageView ivEditar, ivEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo   = itemView.findViewById(R.id.tvTituloEstanteriaPerfil);
            ivEditar   = itemView.findViewById(R.id.ivEditShelf);
            ivEliminar = itemView.findViewById(R.id.ivDeleteShelf);
        }
    }
}
