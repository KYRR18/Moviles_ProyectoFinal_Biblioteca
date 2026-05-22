package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class AnadirLibroActivity extends AppCompatActivity {
    ImageView back;
    ImageView scan;
    TextInputEditText title,autor,editor,isbn;
    MaterialButton cancel,ok;
    ImageView ivList, ivHome, ivSearch, ivUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.anadir_libro);

        back = findViewById(R.id.btnBack);
        scan = findViewById(R.id.btnScan);
        title = findViewById(R.id.etTitle);
        autor = findViewById(R.id.etAuthor);
        editor = findViewById(R.id.etPublisher);
        isbn = findViewById(R.id.etIsbn);
        cancel = findViewById(R.id.btnCancel);
        ok = findViewById(R.id.btnConfirm);
        ivList = findViewById(R.id.iv_list);
        ivHome = findViewById(R.id.iv_home);
        ivSearch = findViewById(R.id.iv_search);
        ivUser = findViewById(R.id.iv_user);

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
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnadirLibroActivity.this, BuscarActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ivList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnadirLibroActivity.this, ListasActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnadirLibroActivity.this, BuscarActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnadirLibroActivity.this, PerfilActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void clearFields(){
        isbn.setText("");
        title.setText("");
        autor.setText("");
        editor.setText("");
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
        if (error.equals("")){crearLibro(isb,titulo,aut,editorial);}
        else{alerta(error, "Error");}
    }
    public void crearLibro(String isbn, String titulo, String autor, String editorial) {
        String url = getString(R.string.API_CREAR);
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean exito = response.getBoolean("exito");
                            if (exito){
                                Toast.makeText(AnadirLibroActivity.this, "¡Libro añadido con exito!", Toast.LENGTH_SHORT).show();
                                clearFields();
                                finish();
                            }
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