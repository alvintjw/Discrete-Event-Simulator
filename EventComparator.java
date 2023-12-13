import java.util.Comparator;

class EventComparator implements Comparator<Events>  {
    
    public int compare(Events e1, Events e2) {
        if (e1.getTime() == e2.getTime()) {
            return Double.compare(e1.getCAT(), e2.getCAT());
        } else {
            return Double.compare(e1.getTime(), e2.getTime());
        }
    }
}