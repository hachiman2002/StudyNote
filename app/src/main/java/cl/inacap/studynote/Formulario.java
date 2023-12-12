package cl.inacap.studynote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import modelo.Entrada;


public class Formulario extends AppCompatActivity implements View.OnClickListener {
    //Creamos objetos para asociar layout
    EditText edtNombre, edtRamo, edtFechaEntrega;
    Spinner spnPrioridad;
    Button btnAgregar;
    //creamos una variable de objeto de la clase FirebaseFirestore
    private FirebaseFirestore firebaseFirestore;
    //variables clase Entrada
    Entrada entrada;
    String nombre, ramo, categoria;
    Date fecha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        //Vincular elementos
        vincularElementos();

        //Habilitar Listener
        habilitarListener();

        //iniciamos firestore
        iniciarFirestore();
    }

    private void iniciarFirestore() {
        //instanciamos objeto de la clase FirebaseFirestore
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void habilitarListener() {
        btnAgregar.setOnClickListener(this);
    }

    private void vincularElementos() {
        edtNombre = (EditText) findViewById(R.id.edt_nombre);
        edtRamo = (EditText) findViewById(R.id.edt_nombre);
        edtFechaEntrega = (EditText) findViewById(R.id.edt_fecha_entrega);
        btnAgregar = (Button) findViewById(R.id.btn_agregar);
        spnPrioridad = (Spinner) findViewById(R.id.spn_prioridad);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spn_prioridad, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Asociamos arraylist al spinner
        spnPrioridad.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        //verificar si hace click en boton agregar
        if (view.getId() == R.id.btn_agregar){
            //obtener valores EditText
            nombre = edtNombre.getText().toString();
            ramo = edtRamo.getText().toString();
            //obtener valor spinner seleccionado
            categoria = spnPrioridad.getSelectedItem().toString();
            //obtener fecha entrega
            String fechaEntrega = edtFechaEntrega.getText().toString();
            //establecer formato fecha, yyyy-mm-dd
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date fechaFormateada = formatoFecha.parse(fechaEntrega);
                fecha = fechaFormateada;
            } catch (ParseException e) {
                //e.printStackTrace();
                e.getMessage();
            }

            //Enviamos a Firestone
            agregarFirestore(nombre, ramo, categoria, fecha);
        }
    }

    private void agregarFirestore(String nombre, String ramo, String categoria, Date fecha) {
        //coleccion en firestore
        CollectionReference coleccionEntradas = firebaseFirestore.collection("Entrada");//Generamos objeto de la clase entrada
        //Generamos objeto de la clase entrada
        entrada = new Entrada(nombre, ramo, fecha, categoria);
        //intentamos agregar el registro a la coleccion
        coleccionEntradas.add(entrada).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                //En caso de éxito en el registro, obtenemos el ID asignado por Firestore
                String idDocumento = documentReference.getId();
                //Actualizamos el campo id de la entrada con el ID asignado
                entrada.setId(idDocumento);
                //Actualizamos la entrada en Firestore con el ID asignado
                documentReference.update("id", idDocumento)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Mostramos mensaje al usuario
                                Toast.makeText(Formulario.this, "Registro efectuado correctamente", Toast.LENGTH_LONG).show();

                                // Realizamos intento para que vuelva a la pantalla principal
                                Intent intento = new Intent(Formulario.this, MainActivity.class);
                                startActivity(intento);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Formulario.this, "Error al actualizar ID en Firestore", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Formulario.this, "Error al registrar", Toast.LENGTH_LONG).show();
            }
        });

    }
}