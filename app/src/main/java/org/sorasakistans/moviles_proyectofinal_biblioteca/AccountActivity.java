package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Vincula con tu archivo account.xml
        setContentView(R.layout.account);

        TextView tvRegister = findViewById(R.id.tvRegister);

        // Al hacer clic en el texto inferior de Registro, volvemos al Login
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Finaliza esta actividad para no saturar el historial de pantallas
            }
        });
    }
}