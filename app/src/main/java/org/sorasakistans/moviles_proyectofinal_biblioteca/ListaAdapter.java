package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ViewHolderLista> {

    private List<Libro> listaLibros; // Tu lista de libros que viene de PHP/MySQL
    private Context context;

    // Constructor para pasarle los datos desde la Activity
    public ListaAdapter(List<Libro> listaLibros, Context context) {
        this.listaLibros = listaLibros;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderLista onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Enlazamos con el diseño individual de cada fila (por ejemplo: item_libro.xml)
        // Cambia 'item_libro' por el nombre exacto de tu XML para el renglón del libro
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.anadir_libro, parent, false);
        return new ViewHolderLista(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderLista holder, int position) {
        // Obtenemos el libro de la posición actual
        Libro libroActual = listas[position];

        // Rellenamos los textos del diseño con los datos reales del objeto
        holder.tvTituloItem.setText(libroActual.getTitulo());
        holder.tvAutorItem.setText(libroActual.getAutor());

        // ================= CONEXIÓN A MODIFICAR LIBRO =================
        // Cuando el usuario le dé clic a este libro en la lista...
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ModificarLibroActivity.class);

            // Le pasamos los datos del libro actual a la pantalla de Modificar
            intent.putExtra("ID_LIBRO", libroActual.getId());
            intent.putExtra("TITULO", libroActual.getTitulo());
            intent.putExtra("AUTOR", libroActual.getAutor());
            intent.putExtra("EDITORIAL", libroActual.getEditorial());
            intent.putExtra("ISBN", libroActual.getIsbn());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        // Le dice al RecyclerView cuántos elementos va a pintar
        return listaLibros != null ? listaLibros.size() : 0;
    }

    // El ViewHolder se encarga de mapear los componentes visuales de cada fila
    public class ViewHolderLista extends RecyclerView.ViewHolder {
        TextView tvTituloItem, tvAutorItem;

        public ViewHolderLista(@NonNull View itemView) {
            super(itemView);
            // Revisa cuáles son los IDs de los TextViews dentro de tu item_libro.xml
            tvTituloItem = itemView.findViewById(R.id.tvTituloItem);
            tvAutorItem = itemView.findViewById(R.id.tvAutorItem);
        }
    }
}
