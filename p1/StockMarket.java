import java.util.*;

public class StockMarket {
    private String name;
    private Map<String, Double> acciones; // Valores actuales de las acciones
    private Map<String, List<observer>> observadores; // Observadores por acción, a unas acciones pueden estar suscritos varios observadores

    public StockMarket(String nom) {
        name = nom;
        acciones = new HashMap<>();
        observadores = new HashMap<>();
    }

    public String getName(){
        return name;
    }

    // Añadir una nueva acción
    public void anyadirAccion(String stockName, double initialValue) {
        acciones.put(stockName, initialValue);
        observadores.put(stockName, new ArrayList<>());
    }

    // Registrar un observador para una acción específica, si no encuentra la empresa no se puede suscribir
    public void attach(String stockName, observer investor) {
        if (observadores.containsKey(stockName)) {
            observadores.get(stockName).add(investor);
        } else {
            System.out.println("La acción " + stockName + " no existe");
        }
    }

    // Eliminar un observador para una acción específica
    public void detach(String stockName, observer investor) {
        if (observadores.containsKey(stockName)) {
            observadores.get(stockName).remove(investor);
        }
    }

    // Cambiar el precio de una acción y notificar a los observadores interesados
    public void cambiarPrecio(String stockName) {
        if (acciones.containsKey(stockName)) {
            double oldValue = acciones.get(stockName);
            double variación =  Math.random()*(50-(-50))+(-50);
            double newValue = oldValue + variación;
            if (newValue < 0) {
                newValue = 0;
            }
            acciones.put(stockName, newValue);
            inform(stockName, newValue);
            
        } else {
            System.out.println("La acción " + stockName + " no existe");
        }
    }

    // Notificar a los observadores interesados
    private void inform(String stockName, double newValue) {
        if (observadores.containsKey(stockName)) {
            for (observer inversor : observadores.get(stockName)) {
                inversor.Update(stockName, newValue);
            }
        }
    }

    // Obtener el precio actual de una acción
    public double getPrice(String stockName) {
        return acciones.getOrDefault(stockName, 0.0);
    }

    public void displayValores() {
        System.out.println("=== Estado Actual de " + getName() + " ===");
        for (Map.Entry<String, Double> entry : acciones.entrySet()) {
            System.out.print("Acción: " + entry.getKey() + " | Valor: $" + String.format("%.2f", entry.getValue()) + " | Gráfico: ");
            for (int i = 0; i < entry.getValue() / 10; i++) { // Dividir por 2 para evitar que la línea sea demasiado larga
                System.out.print("*");
                }
            System.out.println();
        }
        System.out.println("=================================");
    }    
}

