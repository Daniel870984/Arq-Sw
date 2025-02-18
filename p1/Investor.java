import java.util.HashMap;
import java.util.Map;

public class Investor implements observer {
    String name;
    private Map<String, Double> acciones; // Valores que conocemos de las acciones

    public Investor(String name){
        this.name = name;
        acciones = new HashMap<>();
    }

    public void seguir(String stockName, double valorActual){
        acciones.put(stockName,valorActual);
    }
    @Override
    public boolean Update(String stockName, double subjectState) {
        if (acciones.containsKey(stockName)) {
            acciones.put(stockName, subjectState);
        } else {
            System.out.println("La acción " + stockName + " no existe");
        }
        return true;
    }

    public void display() {
        if (acciones.isEmpty()) {
            System.out.println(name + " no sigue ninguna acción.");
            return;
        }
        System.out.println(name + " sigue las siguientes acciones:");
        for (Map.Entry<String, Double> entry : acciones.entrySet()) {
            System.out.println("- " + entry.getKey() + " vale " + String.format("%.2f", entry.getValue()));
        }
    }
}   