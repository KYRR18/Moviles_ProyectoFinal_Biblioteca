package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ModificarLibroActivity extends AppCompatActivity {
    ImageView back, trash, options;
    TextInputEditText title,autor,editor,isbn;
    MaterialButton cancel,ok;
    ImageView ivList, ivHome, ivSearch, ivUser;
    String isbnpasado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.modificar_libro);
        super.onCreate(savedInstanceState);

        // Recuperar el ISBN enviado desde el adaptador
        if (getIntent() != null && getIntent().hasExtra("isbn")) {
            isbnpasado = getIntent().getStringExtra("isbn");
        }

        back = findViewById(R.id.btnBack);
        trash = findViewById(R.id.btnDeleteBook);
        options = findViewById(R.id.btnMoreOptions);
        title = findViewById(R.id.etTitleMod);
        autor = findViewById(R.id.etAuthorMod);
        editor = findViewById(R.id.etPublisherMod);
        isbn = findViewById(R.id.etIsbnMod);
        cancel = findViewById(R.id.btnCancelMod);
        ok = findViewById(R.id.btnConfirmMod);
        ivList = findViewById(R.id.iv_listMod);
        ivHome = findViewById(R.id.iv_homeMod);
        ivSearch = findViewById(R.id.iv_searchMod);
        ivUser = findViewById(R.id.iv_userMod);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {finish();}
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {finish();}
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificarData(view);
            }
        });

        if (isbnpasado != null) {
            recuperarLibro(isbnpasado);
        }else{
            //KEVIN TOAST HERE
            finish();
        }
    }
    public void recuperarLibro(String isbnBuscado){
        String url = "http://10.0.2.2/api_biblioteca/api/buscar_libros.php";
        // 1. Creamos el JSON con el título que queremos buscar
        JSONObject jsonBody = new JSONObject();
        try {jsonBody.put("isbn", isbnBuscado);
        } catch (JSONException e) {e.printStackTrace();}
        // 2. Usamos JsonObjectRequest con método POST
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Un JSON con un arreglo llamado "libros"
                            JSONArray jsonArray = response.getJSONArray("libros");

                            if(jsonArray.length() == 0) {
                                // No se encontró ningún libro con ese nombre
                                return;
                            }
                            //Solo buscamos uno agarraremos al primero
                                JSONObject libroJson = jsonArray.getJSONObject(0);
                                String is = libroJson.getString("isbn");
                                String titulo = libroJson.getString("titulo");
                                String au = libroJson.getString("autor");
                                String edi = libroJson.getString("editorial");
                                isbn.setText(is);
                                title.setText(titulo);
                                autor.setText(au);
                                editor.setText(edi);
                        } catch (JSONException e) {e.printStackTrace();}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {error.printStackTrace();}});
        // 3. Lo añadimos a la cola de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    public void verificarData(View view){
        String isb = isbn.getText().toString();
        String titulo =title.getText().toString();
        String aut = autor.getText().toString();
        String editorial = editor.getText().toString();
        String error = "";
        if (isb.isEmpty()){error+="ISBN vacio\n";}
        if (titulo.isEmpty()){error+="Titulo vacio\n";}
        if (aut.isEmpty()){error+="Autor vacio\n";}
        if (editorial.isEmpty()){error+="Editorial vacio\n";}
        if (error.equals("")){modificarLibro(isb,titulo,aut,editorial);}
        else{alerta(error, "Error");}
    }
    public void modificarLibro(String isbn, String titulo, String autor, String editorial) {
        String url = "http://10.0.2.2/api_biblioteca/api/modificar_libro.php";//VM RECUERDA VM ONLY
        // 1. Crear el objeto JSON que enviaremos
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("isbn", isbn);
            jsonBody.put("titulo", titulo);
            jsonBody.put("autor", autor);
            jsonBody.put("editorial", editorial);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 2. Crear la petición
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean exito = response.getBoolean("exito");
                            String mensaje = response.getString("mensaje");
                            // Mostrar mensaje al usuario (ej. Toast)
                            //Toast tosty = new Toast(v.getContext());
                            //tosty.
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejar error de red o servidor (400, 500, etc.)
                error.printStackTrace();
            }
        });
        // 3. Añadir la petición a la cola de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
    public void eliminarLibro(String isbn){
        String url = "http://10.0.2.2/api_biblioteca/api/eliminar_libro.php";
        // 1. Crear el objeto JSON que enviaremos
        JSONObject jsonBody = new JSONObject();
        try {jsonBody.put("isbn", isbn);
        } catch (JSONException e) {e.printStackTrace();}
        // 2. Crear la petición
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean exito = response.getBoolean("exito");
                            String mensaje = response.getString("mensaje");
                            // Mostrar mensaje al usuario (ej. Toast)
                            //Toast tosty = new Toast(v.getContext());
                            //tosty.
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        // 3. Añadir la petición a la cola de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
    public void alerta(String line , String titulo){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo)
                .setMessage(line)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //
                    }
                }).show();
    }



}
