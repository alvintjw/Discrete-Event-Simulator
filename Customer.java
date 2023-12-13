import java.util.function.Supplier;

class Customer {
    private final int index;
    private final double arrivalTimes;
    private final Supplier<Double> serviceTime;

    Customer(int index, double arrivalTimes, Supplier<Double> serviceTime) {
        this.index = index;
        this.arrivalTimes = arrivalTimes;
        this.serviceTime = serviceTime;
    }

    Customer() {
        this.index = 0;
        this.arrivalTimes = 0;
        this.serviceTime = new DefaultServiceTime();
    }

    public int getIndex() {
        return this.index;
    }

    public double getArrivalTime() {
        return this.arrivalTimes;
    }

    public double getServiceTime() {
        return this.serviceTime.get();
    }


    @Override
    public String toString() {
        return String.format(" " + this.index);
    }


}