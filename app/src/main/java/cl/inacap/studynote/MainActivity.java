package cl.inacap.studynote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import modelo.Entrada;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Variables de interfaz
    Button btnAgregar, btnEditar, btnEliminar;
    private ListView listView;
    //Lista para almacenar las entradas
    private List<Entrada> entradasList;
    //Adaptador para conectar la lista de entradas con la interfaz
    private ArrayAdapter<Entrada> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Vincular elementos
        vincularElementos();

        //Activar Listener
        activarListener();
        entradasList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, entradasList);
        listView.setAdapter(adapter);
        obtenerDatosFirebase();
    }
    //metodo para obtener datos de Firebase y actualizar la lista
    private void obtenerDatosFirebase() {
        //listener para actualizar la lista cuando haya cambios en la base de datos
        FirebaseFirestore.getInstance().collection("Entrada")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w("Error al obtener datos", error);
                        return;
                    }

                    //norrar la lista anterior y agregar los nuevos datos
                    entradasList.clear();
                    for (QueryDocumentSnapshot document : value) {
                        Entrada entrada = document.toObject(Entrada.class);
                        entradasList.add(entrada);
                    }

                    //notificar al adaptador que los datos han cambiado
                    adapter.notifyDataSetChanged();
                });
    }

    private void activarListener() {
        btnAgregar.setOnClickListener(this);
        btnEliminar.setOnClickListener(this);
        btnEditar.setOnClickListener(this);
    }

    private void vincularElementos() {
        btnAgregar = (Button) findViewById(R.id.btn_agregar);
        btnEditar = (Button) findViewById(R.id.btn_editar);
        btnEliminar = (Button) findViewById(R.id.btn_eliminar);
        listView = findViewById(R.id.listView);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_agregar){
            //Realizamos el intento
            Intent intento = new Intent(MainActivity.this, Formulario.class);
            startActivity(intento);
        }
        //Editar
        if (view.getId() == R.id.btn_editar){

        }
        //Eliminar
        if (view.getId() == R.id.btn_eliminar){

        }
    }
}