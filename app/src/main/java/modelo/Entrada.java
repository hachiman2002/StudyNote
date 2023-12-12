package modelo;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.Date;
public class Entrada implements Serializable {
    String nombre;
    String ramo;
    Date fecha;
    String categoria;
    @Exclude
    private String id;
    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }
    public Entrada(String nombre, String ramo, Date fecha, String categoria) {
        this.nombre = nombre;
        this.ramo = ramo;
        this.fecha = fecha;
        this.categoria = categoria;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRamo() {
        return ramo;
    }

    public void setRamo(String ramo) {
        this.ramo = ramo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
