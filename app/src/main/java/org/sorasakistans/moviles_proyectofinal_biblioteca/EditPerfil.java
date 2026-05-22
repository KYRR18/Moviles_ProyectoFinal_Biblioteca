package org.sorasakistans.moviles_proyectofinal_biblioteca;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class EditPerfil extends AppCompatActivity {
    ImageView back, eye;
    TextView email;
    TextInputEditText username, userpass;
    MaterialButton btnCancel, btnConfirm;
    ImageView navHome, navLista, navLupa, navPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_perfil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        back = findViewById(R.id.btnBack);
        email = findViewById(R.id.tvUserEmail);
        username = findViewById(R.id.etUser);
        userpass = findViewById(R.id.etUserPw);
        eye = findViewById(R.id.ivTogglePassword);
        btnCancel = findViewById(R.id.btnCancelEP);
        btnConfirm = findViewById(R.id.btnConfirmEP);
        navHome = findViewById(R.id.navHome);
        navLista = findViewById(R.id.navLista);
        navLupa = findViewById(R.id.navLupa);
        navPerfil = findViewById(R.id.navPerfil);

    }
}