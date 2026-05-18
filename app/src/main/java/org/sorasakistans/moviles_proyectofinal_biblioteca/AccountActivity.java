
package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AccountActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private Button btnSignUpSubmit;
    private TextView tvRegister;
    private ImageView ivTogglePassword;

    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignUpSubmit = findViewById(R.id.btnSignUpSubmit);
        tvRegister = findViewById(R.id.tvRegister);
        ivTogglePassword = findViewById(R.id.ivTogglePassword);

        // Lógica del Ojito en Registro
        ivTogglePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivTogglePassword.setImageResource(android.R.drawable.ic_menu_view);
                    isPasswordVisible = false;
                } else {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivTogglePassword.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                    isPasswordVisible = true;
                }
                etPassword.setSelection(etPassword.getText().length());
            }
        });

        // Botón de Registro (Validación triple)
        btnSignUpSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    etName.setError("El nombre es requerido");
                    etName.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    etEmail.setError("El correo electrónico es requerido");
                    etEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    etPassword.setError("La contraseña es requerida");
                    etPassword.requestFocus();
                    return;
                }

                Toast.makeText(AccountActivity.this, "¡Cuenta creada con éxito!", Toast.LENGTH_SHORT).show();

                // Redirigir al Login y cerrar esta pantalla
                Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Enlace inferior para regresar al Login si ya tiene cuenta
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}