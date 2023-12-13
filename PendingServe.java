class PendingServe extends Events {
    private final Servers server;
    private final Servers dummyServer;
    private final int numOfHumanServers;

    PendingServe(double time, Customer customer, Servers server, 
        Servers dummyServer, int numOfHumanServers) {
        super(time,customer);
        this.server = server;
        this.dummyServer = dummyServer;
        this.numOfHumanServers = numOfHumanServers;
    }

    @Override
    public Events run(ImList<Servers> serverslist) {
        //we need to get the first SCO 
        int index = this.dummyServer.getIndex() - 1;
        Servers updatedServer = serverslist.get(index); 
        //clearSystem.out.println("customer: " + this.customer.getIndex() + "index: " + 
        //(this.server.getIndex() - 1 )+ " numOfHumanServers: " + numOfHumanServers); 
        
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
            
            //System.out.println("Customer: " + this.customer.getIndex());
            //System.out.println("PS: SCO index: " + chosenSelfCheckout.getIndex());
            //System.out.println("min: " + min);
            
    
            if (this.customer == updatedServer.getQueuelist().get(0) 
                && this.getTime() == min)  {
                return new ServiceBegin(chosenSelfCheckout.getEndTime(), 
                this.customer, chosenSelfCheckout);
            } else {
                return new PendingServe(chosenSelfCheckout.getEndTime(), this.customer, 
                this.server, this.dummyServer, numOfHumanServers);
                    
            }
        }

        //this.getTime() equal to the time when server is done serving current customer
        //However, server may choose to take a break after. Hence, we should check if he is
        if (this.getTime() >= updatedServer.getEndTime() 
                && this.customer == updatedServer.getQueuelist().get(0)) {
            updatedServer = updatedServer.deQueue();
            return new ServiceBegin(this.getTime(), 
                    this.customer, updatedServer);
        } else {
            return new PendingServe(updatedServer.getEndTime(), this.customer, updatedServer,
             updatedServer, numOfHumanServers);
        }

    }


    @Override 
    public ImList<Servers> updateServersList(ImList<Servers> serverslist) {
        int index = this.dummyServer.getIndex() - 1;
        Servers updatedServer = serverslist.get(index);

        if (index >= numOfHumanServers) {
            Servers chosenSelfCheckout = serverslist.get(numOfHumanServers);
            double min = chosenSelfCheckout.getEndTime();
            for (int i = numOfHumanServers + 1; i < serverslist.size(); i++) {
                if (serverslist.get(i).getEndTime() < min) {
                    chosenSelfCheckout = serverslist.get(i);
                    min = chosenSelfCheckout.getEndTime();
                }
            }
    
            if (this.customer == updatedServer.getQueuelist().get(0) 
                && this.getTime() == min) {
                updatedServer = updatedServer.deQueue();
            }

            ImList<Servers> newserverslist = serverslist.set(index, updatedServer);
            return newserverslist;
        }

        if (this.getTime() >= updatedServer.getEndTime()
            && this.customer == updatedServer.getQueuelist().get(0)) {
            updatedServer = updatedServer.deQueue();
        }
        ImList<Servers> newserverslist = serverslist.set(index, updatedServer);
        return newserverslist;

    }

    @Override 
    public String toString() {
        return "";
    }

}