import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * Created by hanxi on 25/10/2015.
 */
public class LR {
    private static Hashtable<String, HashMap<String, Integer>> B = new Hashtable<String, HashMap<String, Integer>>();
    private static Hashtable<String, Integer> A = new Hashtable<String, Integer>();

    public static void main(String[] args){
        int initialLearningRate = Integer.parseInt(args[1]);//0.5
        int coef = Integer.parseInt(args[2]);//0.1
        int maxIteration = Integer.parseInt(args[3]);//20 Yes you should stop training at max_iterations.
        int trainingSize = Integer.parseInt(args[4]);//1000
        String testFileName = args[5];
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        /* Training Pharse*/
        int k = 0;
        int t = 1;
        int learningRate = initialLearningRate;
        while(t <= maxIteration){
            try {
                String input;
                while ((input = br.readLine()) != null) {
                    int index1 = input.indexOf("\t");
                    String labels = input.substring(0, index1);
                    String words = input.substring(index1+1, input.length());

                    String[] label = labels.split(",");
                    String[] word = words.split(" ");

                    /* in each example */
                    for(int i=0; i<label.length; i++){
                        k++;
                        if(k>trainingSize){
                            t++;
                            k = 0;
                            learningRate = initialLearningRate/(t*t);
                        }

                        /* in each non-zero features of Xi with index j and value Xj */
                        for(int j=0; j<word.length; j++){
                            if(!B.containsKey(word[j])){
                                HashMap<String, Integer> map = new HashMap();
                                map.put("nl", 0);
                                map.put("el", 0);
                                map.put("ru", 0);
                                map.put("sl", 0);
                                map.put("pl", 0);
                                map.put("ca", 0);
                                map.put("fr", 0);
                                map.put("tr", 0);
                                map.put("hu", 0);
                                map.put("de", 0);
                                map.put("hr", 0);
                                map.put("es", 0);
                                map.put("ga", 0);
                                map.put("pt", 0);
                                B.put(word[j], map);
                            }
                            if(!A.containsKey(word[j])) A.put(word[j], 0);
                            Iterator iter = B.get(word[i]).keySet().iterator();
                            while(iter.hasNext()){
                                String y = (String)iter.next();
                                int newValue = B.get(word[i]).get(y) * (1 - 2 * learningRate * coef)^(k - A.get(word[j]));
                                int exp = 0;
                                for(int m=0; m<word.length; m++){
                                    if(B.containsKey(word[m])) exp += B.get(word[m]).get(y);
                                }
                                double p = Math.exp((double)exp)/(1 + Math.exp((double)exp));
                                if(y.equals(label[i])){
                                    newValue += learningRate * (1 - p);
                                }else{
                                    newValue += learningRate * (0 - p);
                                }
                                B.get(word[i]).put(y, newValue);
                            }

                            A.put(word[j], k);
                        }

                    }
                }

                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Iterator iter = B.keySet().iterator();
        while(iter.hasNext()){
            String word = (String)iter.next();
            Iterator iter2 = B.get(word).keySet().iterator();
            while(iter2.hasNext()){
                String y = (String)iter.next();
                int finalValue = B.get(word).get(y) * (1 - 2 * learningRate * coef)^(k - A.get(word));
                B.get(word).put(y, finalValue);
            }
        }


        /* Testing Pharse*/

        try {
            File inputFile = new File(testFileName);
            InputStreamReader reader = null;
            reader = new InputStreamReader(new FileInputStream(inputFile));
            BufferedReader br2 = new BufferedReader(reader);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
            String input = "";

            while ((input = br2.readLine()) != null) {

            }

            bw.write(""+"\n");
            bw.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
