class ServiceBegin extends Events {
    private final Servers server;
    private final double endTime;
    

    public ServiceBegin(double time, Customer customer, Servers server) {
        super(time, customer);
        this.server = server;
        this.endTime = this.getTime() + this.customer.getServiceTime();
    }
    

    @Override 
    public Events run(ImList<Servers> serverslist) {
        int index = this.server.getIndex() - 1;
        Servers updatedServer = serverslist.get(index);
        updatedServer = updatedServer.changeAvail().getUpdatedServer(this.endTime);
        return new ServiceEnd(endTime, this.customer, updatedServer);  
    }

    @Override 
    public ImList<Servers> updateServersList(ImList<Servers> serverslist) {
        //set the servers isAvail to false and change endTime
        int index = this.server.getIndex() - 1;
        Servers updatedServer = serverslist.get(index);
        updatedServer = updatedServer.changeAvail().getUpdatedServer(this.endTime);
        ImList<Servers> newserverslist = serverslist.set(index, updatedServer);
        return newserverslist;
    }

    @Override
    public String toString() {
        return String.format("%.3f %d serves by %s",
        this.getTime(),this.customer.getIndex(),this.server.toString());
    }

    @Override
    Pair<Integer, Integer> updateCounter(Pair<Integer, Integer> counter) {
        return new Pair<Integer, Integer>(counter.first() + 1, counter.second());
    }

    @Override 
    Double getTotalWaitingTime(Double totalWaitingtime) {
        Double newTotalWaitingTime = totalWaitingtime + this.time - this.customer.getArrivalTime();
        return newTotalWaitingTime;
    }
}