import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by hanxi on 14/09/2015.
 */

public class EventCounter {
    private Hashtable<String, Integer> hashtable;
    final private int BUFFER_SIZE = 10000;

    public EventCounter() {
        hashtable = new Hashtable<String, Integer>();
    }

    public void inc(String event){
        if(hashtable.containsKey(event)){
            hashtable.put(event, hashtable.get(event)+1);
//            System.out.println(event + " " + hashtable.get(event));
        }
        else{
            hashtable.put(event, 1);
        }

        if(hashtable.size()>BUFFER_SIZE){
            for(Iterator itr = hashtable.keySet().iterator(); itr.hasNext();){
                String key = (String) itr.next();
                int value = (int) hashtable.get(key);
                System.out.println(key+"\t"+value);
            }
            hashtable.clear();
        }
    }

    public boolean isEmpty(){
        if(!hashtable.isEmpty()){
            return false;
        }
        else return true;
    }

    public Set getKeySet(){
        return hashtable.keySet();
    }

    public int getValue(String key){
        return hashtable.get(key);
    }

    public void clear(){
        hashtable.clear();
    }

    public boolean containsKey(String key){
        return hashtable.containsKey(key);
    }
}
