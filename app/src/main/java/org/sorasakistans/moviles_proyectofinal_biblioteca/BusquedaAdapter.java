package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BusquedaAdapter extends RecyclerView.Adapter<BusquedaAdapter.ViewHolderBusqueda> {
    private ArrayList<Libro> booklist;

    public BusquedaAdapter(ArrayList<Libro> list) {
        this.booklist = list;
    }

    @NonNull
    @Override
    public ViewHolderBusqueda onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_libro_busqueda, parent, false);
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
        RelativeLayout layoutLibro;

        public ViewHolderBusqueda(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.tvTituloItem);
            autor = itemView.findViewById(R.id.tvAutorItem);
            layoutLibro = itemView.findViewById(R.id.layoutLibro);
        }

        public void asignarDatos(Libro book) {
            titulo.setText(book.getTitulo());
            autor.setText(book.getAutor());
            layoutLibro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ModificarLibroActivity.class);
                    intent.putExtra("isbn", book.getIsbn());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
