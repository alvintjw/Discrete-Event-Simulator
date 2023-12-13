abstract class Events {
    protected final double time;
    protected final Customer customer;

    public Events(double time, Customer customer) {
        this.time = time;
        this.customer = customer;
    }

    public double getTime() {
        return this.time;
    }
    
    public double getCAT() {
        return this.customer.getIndex();
    }

    Pair<Integer, Integer> updateCounter(Pair<Integer, Integer> counter) {
        return new Pair<Integer, Integer>(counter.first(), counter.second());
    }
    
    abstract Events run(ImList<Servers> serverslist);

    ImList<Servers> updateServersList(ImList<Servers> serverslist) {
        ImList<Servers> currentServerslist = serverslist;
        return currentServerslist;
    }

    Double getTotalWaitingTime(Double totalWaitingtime) {
        Double newTotalWaitingTime = totalWaitingtime;
        return newTotalWaitingTime;
    }
}