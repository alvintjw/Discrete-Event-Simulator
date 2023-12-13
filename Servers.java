import java.util.function.Supplier;

abstract class Servers {
    private final int index;
    private final ImList<Customer> q;
    private final boolean avail;
    private final int qmax;
    private final double endTime;
    private final Supplier<Double> restTime;


    public Servers(int index, ImList<Customer> q,
        boolean avail, int qmax, double endTime, Supplier<Double> restTime) {
        this.avail = avail;
        this.index = index;
        this.q = q;
        this.qmax = qmax;
        this.endTime = endTime;
        this.restTime = restTime;
       
    }

    public Double getRestTime() {
        return this.restTime.get();
    }

    public Double getEndTime() {
        return this.endTime;
    }

    public ImList<Customer> getQueuelist() {
        return this.q;
    }

    public int getqMax() {
        return this.qmax;
    }

    public Supplier<Double> getRestSupplier() {
        return this.restTime;
    }

    public int getIndex() {
        return this.index;
    }

    public boolean isAvail() {
        return this.avail;
    }
     
    //return the same server but now occupied
    abstract Servers changeAvail();
    
    //return the same server but one customer deque 
    abstract Servers deQueue();
    
    //return the same server but one customer entered the queue
    abstract Servers enQueue(Customer customer);

    //return the same server but updated endTime
    abstract Servers getUpdatedServer(double endTime);
    

    public boolean isFull() {
        return qmax == this.q.size();
    }
    
    public boolean isEmpty() {
        return this.q.isEmpty();
    }

    public int length() {
        return this.q.size();
    }


}