    import java.util.function.Supplier;

class Simulator {

    private final int numOfServers;
    private final int numOfSelfChecks;
    private final int qmax;
    private final ImList<Double> arrivalTimes;
    private final Supplier<Double> serviceTime;
    private final Supplier<Double> restTime;

    public Simulator(int numOfServers, int numOfSelfChecks, int qmax, 
        ImList<Double> arrivalTimes, Supplier<Double> serviceTime, Supplier<Double> restTime) {
        this.numOfServers = numOfServers;
        this.numOfSelfChecks = numOfSelfChecks;
        this.qmax = qmax;
        this.arrivalTimes = arrivalTimes;
        this.serviceTime = serviceTime;
        this.restTime = restTime;
    }

    //create servers list
    public ImList<Servers> createServerlist(ImList<Customer> q) {
        Supplier<Double> noRestTime  = () -> 0.0;
        ImList<Servers> serverslist = new ImList<Servers>();
        //create human servers
        for (int i = 0; i < numOfServers; i++) {
            serverslist = serverslist.add(new HumanServers(i + 1, q, true, qmax,0,this.restTime));
        }

        //create the first selfcheckout that would contain all the queues
        if (numOfSelfChecks != 0) {
            //System.out.println("hello");
            serverslist = serverslist.add(new SelfCheckout(1 + numOfServers, q,
            true, qmax,0,noRestTime));
        }
    
        //create the rest of the self checkout
        for (int j = 0; j < numOfSelfChecks - 1; j++) {
            //System.out.println("byebye");
            serverslist = serverslist.add(new SelfCheckout(j + 2 + numOfServers, 
            q, true, 0, 0, noRestTime));
        }
        return serverslist;
    }

    //method that creates PQ
    public PQ<Events> createPQ() {
        PQ<Events> pq = new PQ<Events>(new EventComparator());
        return pq;
    }

    //create customer list (this is for the actual customer)
    public ImList<Customer> createCustomerlist() {
        ImList<Customer> customerlist = new ImList<Customer>();
        for (int i = 0; i < arrivalTimes.size(); i++) {
            customerlist = customerlist.add(new Customer(i + 1, arrivalTimes.get(i), serviceTime));
        }
        return customerlist;
    }

    //create a customer "queue" (this is for the customer)
    public ImList<Customer> createCustomerQueue() {
        ImList<Customer> customerQueue = new ImList<Customer>();
        return customerQueue;
    }

    //Simulate method that prints output
    public String simulate() {
        //Create PQ of arrival Events
        PQ<Events> pq = this.createPQ();
        for (int i = 0; i < arrivalTimes.size(); i++) {
            Customer customer = new Customer(i + 1, arrivalTimes.get(i), serviceTime);
            pq = pq.add(new Arrival(arrivalTimes.get(i), customer,numOfServers));
        }

        //every server will have a Customer queue of 0 at the start
        ImList<Customer> customerQueue = this.createCustomerQueue();

        //creating the serverslist
        ImList<Servers> serverslist = this.createServerlist(customerQueue);

        //Creating the output string
        ImList<String> output = new ImList<String>();
        String solution = "";      

        //creating the counter to track
        Pair<Integer, Integer> counter = new Pair<Integer, Integer>(0, 0);

        //create waiting time tracker
        Double totalWaitingtime = 0.00;

        //simulate the PQ until it is empty
        while (!pq.isEmpty()) {
            //take the first event that needs to be simulated
            Events currentEvent = pq.poll().first();
            Events nextEvent = currentEvent.run(serverslist);
            //update serverslist 
            serverslist = currentEvent.updateServersList(serverslist);
            counter = currentEvent.updateCounter(counter);
            totalWaitingtime = currentEvent.getTotalWaitingTime(totalWaitingtime);
            output = output.add(currentEvent.toString());
            pq = pq.poll().second();
            
            if (currentEvent != nextEvent) {
                pq = pq.add(nextEvent);
            }         
        }

        for (int j = 0; j < output.size(); j++) {
            if (j == output.size() - 1) {
                solution += output.get(j) + "\n";
                break;
            } else if (output.get(j).toString() != "") {
                solution += output.get(j).toString() + "\n";
            } else {
                continue;
            }
        }
        double averageWaitingtime = 0.000;
        if (counter.first() == 0) {
            averageWaitingtime = 0.000;
        } else {
            averageWaitingtime = totalWaitingtime / counter.first();
        }

        solution += String.format("[%.3f %d %d]", averageWaitingtime, 
        counter.first(), counter.second());
        return solution;
    }
}