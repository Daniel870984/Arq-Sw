
public class Main {
    public static void main(String[] args) {
        Stock googleStock = new Stock("Google",188);
        Stock amazonStock = new Stock( "Bitcoin",233);

        Investor investor1 = new Investor("Daniel");
        Investor investor2 = new Investor("Carlos");
        Investor investor3 = new Investor("Raul");

        googleStock.attach(investor1);
        googleStock.attach(investor2);

        amazonStock.attach(investor3);

        while(true){    
            googleStock.cambiarPrecio();
            amazonStock.cambiarPrecio();
            investor1.display();
            investor2.display();
            investor3.display();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            googleStock.detach(investor2);
            System.out.println();
            System.out.println();
        }
    }
}