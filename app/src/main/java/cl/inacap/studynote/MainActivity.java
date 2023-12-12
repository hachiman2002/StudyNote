package cl.inacap.studynote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Variable de interfaz
    Button btnAgregar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Vincular elementos
        vincularElementos();

        //Activar Listener
        activarListener();
    }

    private void activarListener() {
        btnAgregar.setOnClickListener(this);
    }

    private void vincularElementos() {
        btnAgregar = (Button) findViewById(R.id.btn_agregar);
    }

    @Override
    public void onClick(View view) {
        //Realizamos el intento
        Intent intento = new Intent(MainActivity.this, Formulario.class);
        startActivity(intento);
    }
}