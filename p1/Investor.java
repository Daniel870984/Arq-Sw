
public class Investor implements observer {
    double price;
    String name;
    public Investor(String name){
        this.name = name;
    }
    @Override
    public boolean Update(double subjectState) {
        this.price = subjectState;
        return true;
    }
    public double getState(){
        return price;
    }
    public void display(){
        System.out.println(name + " sabe que vale " + price);
    }
}   