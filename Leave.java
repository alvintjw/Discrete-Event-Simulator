class Leave extends Events {

    public Leave(double time, Customer customer) {
        super(time,customer);
    }

    @Override
    public Events run(ImList<Servers> serverslist) {
        return this;
    }

    @Override
    public String toString() {
        return String.format("%.3f %d leaves",this.getTime(),this.customer.getIndex());
    }
    
    @Override
    Pair<Integer, Integer> updateCounter(Pair<Integer, Integer> counter) {
        return new Pair<Integer, Integer>(counter.first(), counter.second() + 1);
    }
}