package p3b;

import java.io.Serializable;
import java.util.ArrayList;

public class Servicios implements Serializable {
    private ArrayList<String> servicios;

    public Servicios() {
        servicios = new ArrayList<>();
    }

    public void addServicio(String servicio) {
        servicios.add(servicio);
    }

    public ArrayList<String> getServicios() {
        return servicios;
    }
    
    @Override
    public String toString() {
        return servicios.toString();
    }
}
