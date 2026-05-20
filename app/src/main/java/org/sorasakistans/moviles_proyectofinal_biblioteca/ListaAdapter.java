package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ViewHolderLista>{
    ArrayList<Libro> lista;
    public ListaAdapter(ArrayList<Libro> list){this.lista = list;}
    @NonNull
    @Override
    public ViewHolderLista onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista, null, false);
        return new ViewHolderLista(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderLista holder, int position) {
        holder.asignardatos(lista.get(position));
    }

    @Override
    public int getItemCount() { return lista.size();}

    public class ViewHolderLista extends RecyclerView.ViewHolder{
        TextView titulo;
        TextView statut;//Provisionalmente Autor
        TextView valor; //Provisionalmente ID
        public ViewHolderLista(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.tvItemTitle);
            statut = itemView.findViewById(R.id.tvModifiedDate);
            valor = itemView.findViewById(R.id.tvStatusValue);
        }
        public void asignardatos(Libro book){
            titulo.setText(book.getTitulo());
            statut.setText(book.getAutor());
            valor.setText(book.getIsbn());
        }
    }
}
