package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ViewHolderLista>{
    @NonNull
    @Override
    public ViewHolderLista onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderLista holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolderLista extends RecyclerView.ViewHolder{

        public ViewHolderLista(@NonNull View itemView) {
            super(itemView);
        }
    }
}
