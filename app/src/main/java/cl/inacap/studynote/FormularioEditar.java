package cl.inacap.studynote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import modelo.Entrada;

public class FormularioEditar extends AppCompatActivity implements View.OnClickListener {
    //VARIABLES DE INTERFAZ
    EditText edtNombre, edtRamo, edtFechaEntrega;
    Spinner spnPrioridad;
    Button btnEditar;
    //creamos una variable de objeto de la clase FirebaseFirestore
    private FirebaseFirestore firebaseFirestore;
    //variables clase Entrada
    Entrada entrada;
    private String nombre, ramo, categoria;
    private Date fecha;
    private String idUpdate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_editar);
        // Obtener la entrada seleccionada del Intent
        entrada = (Entrada) getIntent().getSerializableExtra("entradaSeleccionada");
        
        // Verificar si la entrada es nula antes de acceder a sus propiedades
        if (entrada == null) {
            // Manejar el caso en el que no se proporciona una entrada
            Toast.makeText(FormularioEditar.this, "Error: Entrada no encontrada", Toast.LENGTH_LONG).show();
            finish(); // Cerrar la actividad en caso de error
            return;
        }

        firebaseFirestore = FirebaseFirestore.getInstance();
        //Vincular elementos
        vincularElementos();
        //Habilitar Listener
        habilitarListener();

        //Obtenemos datos
        edtNombre.setText(entrada.getNombre());
        edtRamo.setText(entrada.getRamo());
        edtFechaEntrega.setText(entrada.getFecha().toString());

        idUpdate = entrada.getId();
        Toast.makeText(FormularioEditar.this, "ID " + entrada.getId(), Toast.LENGTH_SHORT).show();
    }
    private void habilitarListener() {
        btnEditar.setOnClickListener(this);
    }
    private void vincularElementos() {
        edtNombre = findViewById(R.id.edt_nombre);
        edtRamo = findViewById(R.id.edt_ramo);
        edtFechaEntrega = findViewById(R.id.edt_fecha_entrega);
        btnEditar = findViewById(R.id.btn_editar);
        spnPrioridad = findViewById(R.id.spn_prioridad);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spn_prioridad, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPrioridad.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_editar) {
            // Obtener los valores de los campos de edición
            nombre = edtNombre.getText().toString().trim();
            ramo = edtRamo.getText().toString().trim();

            // Validar que los campos no estén vacíos
            if (nombre.isEmpty() || ramo.isEmpty() ) {
                Toast.makeText(FormularioEditar.this, "No pueden quedar campos vacios", Toast.LENGTH_LONG).show();
                return;
            }
            String fechaEntrega = edtFechaEntrega.getText().toString();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = format.parse(fechaEntrega);
                fecha = date;
                System.out.println(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            // Obtener la prioridad seleccionada del Spinner
            categoria = spnPrioridad.getSelectedItem().toString();

            // Actualizar los valores en la entrada existente
            entrada.setNombre(nombre);
            entrada.setRamo(ramo);
            entrada.setFecha(fecha);
            entrada.setCategoria(categoria);

            // Actualizar la entrada en Firebase
            firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseFirestore.collection("Entrada")
                    .document(entrada.getId())
                    .set(entrada)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(FormularioEditar.this, "Entrada actualizada con éxito", Toast.LENGTH_SHORT).show();
                        finish(); // Cerrar la actividad de edición después de guardar
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(FormularioEditar.this, "Error al actualizar", Toast.LENGTH_LONG).show();
                    });
        }
    }
}