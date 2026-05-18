package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.button.MaterialButton;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private MaterialButton btnSignIn, btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // ¡Dejado en su nombre real!
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        // Acción al presionar "SIGN IN" -> Nos lleva al Login
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Acción al presionar "CREATE ACCOUNT" -> Nos lleva al Registro
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });
    }
}