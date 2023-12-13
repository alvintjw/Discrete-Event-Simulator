import java.util.function.Supplier;

class HumanServers extends Servers {
    
    public HumanServers(int index, ImList<Customer> q,
        boolean avail, int qmax, double endTime, Supplier<Double> restTime) {
        super(index, q, avail, qmax, endTime, restTime);
    } 

    @Override
    public String toString() {
        return String.format("%d", super.getIndex());
    }

    @Override
    HumanServers changeAvail() {
        HumanServers updatedServer = new HumanServers(this.getIndex(), this.getQueuelist(),
            false, this.getqMax(), this.getEndTime(), this.getRestSupplier());
        return updatedServer;
    }
    
    //return the same server but one customer deque 
    @Override
    HumanServers deQueue() {
        ImList<Customer> updatedQueue = this.getQueuelist().remove(0);
        HumanServers updatedServer = new HumanServers(this.getIndex(),
            updatedQueue, false, this.getqMax(), this.getEndTime(),this.getRestSupplier());
        return updatedServer;
    }
    
    //return the same server but one customer entered the queue
    @Override
    HumanServers enQueue(Customer customer) {
        ImList<Customer> updatedQueue = this.getQueuelist().add(customer);
        HumanServers updatedServer = new HumanServers(this.getIndex(), 
            updatedQueue, false, this.getqMax(), this.getEndTime(),this.getRestSupplier());
        return updatedServer;
    }

    //return the same server but updated endTime
    @Override
    HumanServers getUpdatedServer(double endTime) {
        HumanServers updatedServer = new HumanServers(this.getIndex(), this.getQueuelist(),
            this.isAvail(), this.getqMax(), endTime, this.getRestSupplier());
        return updatedServer;
    }



    



}