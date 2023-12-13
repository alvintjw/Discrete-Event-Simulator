import java.util.function.Supplier;

class SelfCheckout extends Servers {
    
    public SelfCheckout(int index, ImList<Customer> q,
        boolean avail, int qmax, double endTime, Supplier<Double> restTime) {
        super(index, q, avail, qmax, endTime, restTime);
    }

    @Override
    public String toString() {
        return String.format("self-check %d", this.getIndex());
    }

    @Override
    SelfCheckout changeAvail() {
        SelfCheckout updatedServer = new SelfCheckout(this.getIndex(), this.getQueuelist(),
            false, this.getqMax(), this.getEndTime(), this.getRestSupplier());
        return updatedServer;
    }
    
    //return the same server but one customer deque 
    @Override
    SelfCheckout deQueue() {
        ImList<Customer> updatedQueue = this.getQueuelist().remove(0);
        SelfCheckout updatedServer = new SelfCheckout(this.getIndex(),
            updatedQueue, false, this.getqMax(), this.getEndTime(),this.getRestSupplier());
        return updatedServer;
    }
    
    //return the same server but one customer entered the queue
    @Override
    SelfCheckout enQueue(Customer customer) {
        ImList<Customer> updatedQueue = this.getQueuelist().add(customer);
        SelfCheckout updatedServer = new SelfCheckout(this.getIndex(), 
            updatedQueue, false, this.getqMax(), this.getEndTime(),this.getRestSupplier());
        return updatedServer;
    }

    //return the same server but updated endTime
    @Override
    SelfCheckout getUpdatedServer(double endTime) {
        SelfCheckout updatedServer = new SelfCheckout(this.getIndex(), this.getQueuelist(),
            this.isAvail(), this.getqMax(), endTime, this.getRestSupplier());
        return updatedServer;
    }
}