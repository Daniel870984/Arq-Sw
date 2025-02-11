import java.util.*;
public class Stock implements subject{

    private double subjectState;
    private List<observer> list;
    public String name;

    public Stock(String name, double valorInicial){
        subjectState = valorInicial;
        this.name = name;
        list = new ArrayList<observer>();
    }
    public void attach(observer inversor){
        list.add(inversor);
    }
    public void detach(observer inversor){
        list.remove(inversor);
    }
    public void inform(){
        for(observer inversor : list){
            inversor.Update(subjectState);
        }
    }    
    //Devuelve el valor de subjectState
    public double getState(){
        return subjectState;
    }
    //Asigna al valor del sujeto el valor value
    public void setState(double value){
        subjectState = value;
    }
    //Cambia el valor del sujeto y notifica a los observadores
    public void cambiarPrecio(){
        double variación =  Math.random()*(10-(-5))+(-5);
        subjectState = subjectState - variación;
        inform();
        System.out.println("Ahora " + name + " vale " + subjectState);
    }
}   
