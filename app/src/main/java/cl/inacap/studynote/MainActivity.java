package cl.inacap.studynote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import modelo.Entrada;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    //Variables de interfaz
    Button btnAgregar, btnEditar, btnEliminar;
    private ListView listView;
    //Lista para almacenar las entradas
    private List<Entrada> entradasList;
    //Adaptador para conectar la lista de entradas con la interfaz
    private ArrayAdapter<Entrada> adapter;
    // Variable para almacenar la entrada seleccionada
    private Entrada entradaSeleccionada;
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
        // Configurar el listener para clics en elementos de la lista
        listView.setOnItemClickListener(this);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        entradaSeleccionada = entradasList.get(position);
        //Prueba para ver que elemento estoy seleccionando y mostrarlo por la consola
        Log.d("MainActivity", "Elemento clickeado: " + entradaSeleccionada.toString());
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_agregar){
            Log.d("MainActivity", "agregar");
            //Realizamos el intento
            Intent intento = new Intent(MainActivity.this, Formulario.class);
            startActivity(intento);
        }
        //Editar
        if (view.getId() == R.id.btn_editar){

        }
        // Eliminar
        if (view.getId() == R.id.btn_eliminar){
            // Verificar si hay una entrada seleccionada y si tiene un ID valido
            if (entradaSeleccionada != null && entradaSeleccionada.getId() != null) {
                // Eliminar la entrada de la lista y de Firebase
                eliminarEntrada(entradaSeleccionada);
                // Limpiar la entrada seleccionada
                entradaSeleccionada = null;
            } else {
                // Manejar el caso en el que no hay una entrada seleccionada o el ID es nulo
                Log.e("MainActivity", "Error: Entrada seleccionada o ID nulo al intentar eliminar");
            }
        }
    }

    private void eliminarEntrada(Entrada entrada) {
        // Eliminar la entrada de la lista local
        entradasList.remove(entrada);
        // Notificar al adaptador que los datos han cambiado
        adapter.notifyDataSetChanged();

        // Eliminar la entrada de Firebase
        FirebaseFirestore.getInstance().collection("Entrada")
                .document(entrada.getId())
                .delete()
                .addOnSuccessListener(aVoid -> Log.d("MainActivity", "Entrada eliminada de Firebase"))
                .addOnFailureListener(e -> Log.e("MainActivity", "Error al eliminar entrada de Firebase", e));
    }
}