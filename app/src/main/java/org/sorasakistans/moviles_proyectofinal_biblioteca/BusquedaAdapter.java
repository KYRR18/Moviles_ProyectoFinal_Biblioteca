package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BusquedaAdapter extends RecyclerView.Adapter<BusquedaAdapter.ViewHolderBusqueda> {

    @NonNull
    @Override
    public ViewHolderBusqueda onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderBusqueda holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolderBusqueda extends RecyclerView.ViewHolder{

        public ViewHolderBusqueda(@NonNull View itemView) {
            super(itemView);
        }
    }
}
