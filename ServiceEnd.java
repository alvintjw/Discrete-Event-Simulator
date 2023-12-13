class ServiceEnd extends Events {
    private final Servers server;


    public ServiceEnd(double time, Customer customer, Servers server) {
        super(time,customer);
        this.server = server;
    }

    @Override
    public Events run(ImList<Servers> serverslist) {
        return this;        
    }

    @Override 
    ImList<Servers> updateServersList(ImList<Servers> serverslist) {

        int index = this.server.getIndex() - 1;        
        Servers updatedServer = serverslist.get(index);
        double restTime = this.server.getRestTime();
        double serverEndTime = restTime + this.getTime();
        updatedServer = updatedServer.getUpdatedServer(serverEndTime);   
        ImList<Servers> newServersList = serverslist.set(index,updatedServer);
        return newServersList;     
    }

    @Override 
    public String toString() {
        return String.format("%.3f %d done serving by %s",
        this.getTime(),this.customer.getIndex(),this.server.toString());
    }
    
}