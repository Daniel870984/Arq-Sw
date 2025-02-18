public class Main {
    public static void main(String[] args) {
        StockMarket StockMarket = new StockMarket("La bolsa Nueva York");

        // Añadir acciones a la bolsa
        StockMarket.anyadirAccion("Google", 188.0);
        StockMarket.anyadirAccion("Amazon", 233.0);
        StockMarket.anyadirAccion("Nvidia", 150.0);

        // Crear inversores
        Investor investor1 = new Investor("Daniel");
        Investor investor2 = new Investor("Carlos");
        Investor investor3 = new Investor("Marcos");

        // Suscribir inversores a acciones específicas
        StockMarket.attach("Google", investor1);
        StockMarket.attach("Amazon", investor1);
        StockMarket.attach("Nvidia", investor1);
        StockMarket.attach("Amazon", investor2);
        StockMarket.attach("Nvidia", investor3);


        while (true) {
            try {
                // Cambiar el precio de las acciones de forma aleatoria
                StockMarket.cambiarPrecio("Google");
                StockMarket.cambiarPrecio("Amazon");
                StockMarket.cambiarPrecio("Nvidia");

                // Esperar 3 segundos entre actualizaciones
                Thread.sleep(3000);
                StockMarket.displayValores();
                investor1.display();
                investor2.display();
                investor3.display();
                System.out.println();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
