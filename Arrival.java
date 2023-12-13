class Arrival extends Events {
    private final int numOfHumanServers;

    public Arrival(double time, Customer customer, int numOfHumanServers) {
        super(time,customer);
        this.numOfHumanServers = numOfHumanServers;
    }

    //Upon arrival check if can serve or not
    boolean availServer(ImList<Servers> serverslist, Customer customer) {
        for (int i = 0; i < serverslist.size(); i++) {
            if (serverslist.get(i).getEndTime() <= customer.getArrivalTime()) {
                return true;
            }
        }
        return false;
    }

    //return a server to a customer upon arrival if availServer = true;
    Servers getServer(ImList<Servers> serverslist) {
        for (int i = 0; i < serverslist.size(); i++) {
            if (serverslist.get(i).getEndTime() <= customer.getArrivalTime()) {
                return serverslist.get(i);
            }
        }
        //code should never reach here (dummy server)
        return new HumanServers(-1, new ImList<Customer>(), true, 1,-1, new DefaultRestTime());
    }
    
    //No avail server, loop throughs server queue and check if there is an empty queue
    boolean availHumanQueue(ImList<Servers> serverslist) {
        for (int i = 0; i < numOfHumanServers; i++) {
            if (!serverslist.get(i).isFull()) {
                return true;
            }
        }
        return false;
    }

    //return a server for customer to queue if availQueue = true
    Servers getHumanServer(ImList<Servers> serverslist) {
        for (int i = 0; i < numOfHumanServers; i++) {
            if (!serverslist.get(i).isFull()) {
                return serverslist.get(i);
            }
        }
        //code should never reach here 
        return new HumanServers(-1, new ImList<Customer>(), true, 1,-1, new DefaultRestTime());
    }

    boolean availSCOQueue(ImList<Servers> serverslist) {
        if (serverslist.size() == numOfHumanServers) {
            return false;
        } else {
            Servers firstSCO = serverslist.get(numOfHumanServers);
            return !firstSCO.isFull();
        }

    }

    Servers nextAvailSCO(ImList<Servers> serverslist) {
        Servers chosenSelfCheckout = serverslist.get(numOfHumanServers);
        double min = chosenSelfCheckout.getEndTime();
        for (int i = numOfHumanServers + 1; i < serverslist.size(); i++) {
            if (serverslist.get(i).getEndTime() < min) {
                chosenSelfCheckout = serverslist.get(i);
                min = chosenSelfCheckout.getEndTime();
            }
        }
        
        //System.out.println("Customer: " + this.customer.getIndex());
        //System.out.println("Arrival: SCO index: " + chosenSelfCheckout.getIndex());
        //System.out.println("min: " + min);
        

        return chosenSelfCheckout;
    }

    @Override
    public Events run(ImList<Servers> serverslist) {
        if (availServer(serverslist, this.customer)) {
            //return a new serve event with the chosen server
            Servers chosenServer = getServer(serverslist);
            return new ServiceBegin(this.getTime(), customer, chosenServer);
        } else if (!availServer(serverslist, this.customer) & availHumanQueue(serverslist)) {
            //return a Wait event with the chosen Human server
            Servers chosenServer = getHumanServer(serverslist);
            return new Wait(this.getTime(), customer, 
                chosenServer, chosenServer, numOfHumanServers);
        } else if (!availServer(serverslist, customer) & !availHumanQueue(serverslist)) {
            if (availSCOQueue(serverslist)) {
                //get the first SCO
                Servers firstSCO = serverslist.get(numOfHumanServers);
                //if (this.customer.getIndex() > 15) {
                //    System.out.println("Customer: " + customer.getIndex());
                //    System.out.println(" Queue length: " + firstSCO.length());
                //}
                //get the SCO with the lowest endTime
                Servers chosenSCO = nextAvailSCO(serverslist);
                return new Wait(this.getTime(), customer, chosenSCO, firstSCO, numOfHumanServers);
            } else {
                return new Leave(this.getTime(), customer);
            }
        } else {
            //no available server & no available queue, thus customer leaves
            return new Leave(this.getTime(), customer);
        }
    }

    @Override 
    public String toString() {
        return String.format("%.3f %d arrives",this.getTime(), this.customer.getIndex());
    }
 
}