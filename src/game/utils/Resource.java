package game.utils;

public class Resource {
    private int amount;
    private int capacity;

    public Resource(int amount, int cap) {
        this.amount = amount;
        this.capacity = cap;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void addAmount(int resourceAmount) {
        if (resourceAmount + amount > capacity) {
            this.amount = capacity;
            return;
        }

        if (resourceAmount + amount < 0) { // SHOULD NEVER HAPPEN
            this.amount = 0;
            return;
        }

        this.amount += resourceAmount;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void addCapacity(int capacity) {
        this.capacity += capacity;
    }

    @Override
    public String toString() {
        return String.format("%d%d",amount, capacity);
    }
}
