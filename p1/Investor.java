
public class Investor implements observer {
    double price;
    String name;
    String stockFollowed;
    public Investor(String name){
        this.name = name;
    }
    @Override
    public boolean Update(String stockName, double subjectState) {
        this.price = subjectState;
        this.stockFollowed = stockName;
        return true;
    }
    public double getState(){
        return price;
    }
    public void display(){
        System.out.println(name + " sabe que " + stockFollowed+ " vale " + String.format("%.2f", price));
    }
}   