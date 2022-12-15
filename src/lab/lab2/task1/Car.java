package lab.lab2.task1;

public class Car extends Vehicle {
    private int maxSpeed;

    Car(int id, int price, int maxSpeed) {
        super(id, price);
        this.maxSpeed = maxSpeed;
    }

    @Override
    public void run() {
        System.out.println("Wow, I can Run (maxSpeed:" + maxSpeed + ")!");
    }

    @Override
    public int getPrice() {
        int price;
        if (maxSpeed < 1000) {
            price = super.getPrice();
        } else {
            price = super.getPrice() + 1000;
        }
        System.out.println("price is: " + price);
        return price;
    }
}
