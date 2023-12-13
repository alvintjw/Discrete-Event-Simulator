class Wait extends Events {
    private final int numOfHumanServers;
    private final Servers server;
    private final Servers dummyServer;

    public Wait(double time, Customer customer, Servers server, Servers dummyServer,
        int numOfHumanServers) {
        super(time,customer);
        this.server = server;
        this.dummyServer = dummyServer;
        this.numOfHumanServers = numOfHumanServers;
    }

    @Override 
    public Events run(ImList<Servers> serverslist) {
        //we need to enqueue the first SCO but pass the time of nextAvailSCO 
        int index = this.dummyServer.getIndex() - 1;
        //this is enqueueing the server
        Servers updatedServer = serverslist.get(index).enQueue(this.customer);

        if (index >= numOfHumanServers) {
            //get the SCO with the min endTime
            Servers chosenSelfCheckout = serverslist.get(numOfHumanServers);
            double min = chosenSelfCheckout.getEndTime();
            for (int i = numOfHumanServers + 1; i < serverslist.size(); i++) {
                if (serverslist.get(i).getEndTime() < min) {
                    chosenSelfCheckout = serverslist.get(i);
                    min = chosenSelfCheckout.getEndTime();
                }
            } 
            
            return new PendingServe(min, customer, chosenSelfCheckout,
              updatedServer, numOfHumanServers);
        }

        return new PendingServe(updatedServer.getEndTime(), this.customer, updatedServer,
         updatedServer, numOfHumanServers);
    }

    @Override
    public ImList<Servers> updateServersList(ImList<Servers> serverslist) {
        int index = this.dummyServer.getIndex() - 1;
        Servers updatedServer = serverslist.get(index).enQueue(this.customer);
        ImList<Servers> newServersList = serverslist.set(index, updatedServer);
        return newServersList;
    }


    @Override
    public String toString() {
        return String.format("%.3f %d waits at %s",
        this.getTime(),this.customer.getIndex(),this.dummyServer.toString());
    }

}