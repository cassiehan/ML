import java.io.*;
import java.util.Iterator;

/**
 * Created by hanxi on 14/09/2015.
 */
public class NBTrain {
    public static void main(String[] args){
        EventCounter eventCounter = new EventCounter();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String input;
            //String input = "hu\tangolan armed forces faa headed chief staff who reports minister defense there three components army navy marinha de guerra mdg foru00e7a au00e9rea nacional angolana fana total manpower 65000 army far largest services 55000 men women navy numbers 1000 operates several small patrol craft barges air force personnel total 3500 equipment includes russianmanufactured fighters transport planes small number faa personnel stationed democratic republic congo kinshasa republic congo brazzaville 2005 fapla total 90000 active personnel army 100000 members whose major equipment included over 300 main battle tanks 600 reconnaissance vehicles over 250 armored infantry fighting vehicles 170 armored personnel carriers more than 1396 artillery pieces 199091 army ten military regions estimated 73 'brigades' each mean strength 1000 comprising inf tank apc artillery aa units required iiss military balance 1990 1991 navy estimated 2400 personnel whose major naval units consisted nine patrolcoastal vessels air force air defense forces 6000 personnel 90 combat capable aircraft including 22 fighters 59 fighter ground attack aircraft 16 attack helicopters defense budget 2005 totalled 116 billion";

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

            while ((input = br.readLine()) != null) {
                String[] part = input.split("\t");
                String[] y = part[0].split(",");
                String[] w = part[1].split(" ");
//        System.out.println(y.length + "\t"+w.length);

                for(int i=0; i<y.length; i++){

                    if(!eventCounter.containsKey("Y^"+y[i])){
                        eventCounter.inc("Y^"+"y*");
                    }
                    eventCounter.inc("Y^"+y[i]);
                    for(int j=0; j<w.length; j++){
                        eventCounter.inc(w[j] + "\t" + "Y^"+y[i]);
                    }
                }

                if(!eventCounter.isEmpty()){
                    for(Iterator itr = eventCounter.getKeySet().iterator(); itr.hasNext();){
                        String key = (String) itr.next();
                        int value = (int) eventCounter.getValue(key);
                        bw.write(key+"\t"+value+"\n");
                        bw.flush();
                    }
                    eventCounter.clear();
                }
            }
            br.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
