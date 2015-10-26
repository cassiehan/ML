import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by hanxi on 14/09/2015.
 */
public class NBTest {
    public static void main(String[] args){
        try{
            HashSet<String> hashSet = new HashSet<String>(); // to save the words in test file
            Hashtable<String, Integer> hashtable = new Hashtable<String, Integer>(); // to save the word, y pair
            Hashtable<String, Integer> ytable = new Hashtable<String, Integer>(); // to save the needed y
            int ytotal = 0;

            /* read event counter from input stream */
            BufferedReader brCount = new BufferedReader(new InputStreamReader(System.in));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
            String events;

            /* read test file from args */
            String inputFileName = null;
            if (args.length > 0) {
                inputFileName = args[0];
            }
            else{
                System.out.println("fileNotFound");
            }

            File inputFile = new File(inputFileName);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(inputFile));
            BufferedReader brTest = new BufferedReader(reader);
            String inputTest;
            String inputTest2;
            String inputCount;

           /* add the needed x to hashset */
            while((inputTest = brTest.readLine())!=null){
                String[] words = inputTest.split("\t")[1].split(" ");
                for(int i=0; i<words.length; i++){
                    if(!hashSet.contains(words[i])){
                        hashSet.add(words[i]);
                    }
                }
            }

            //System.out.println("1. hashset "+ hashSet.size());
             //11000	11000,Y=de,1	11000,Y=fr,1	11000,Y=nl,1	11000,Y=pl,1	11000,Y=pt,1
            //Y=y* 360
            /* for each event, if event involves a needed term x in hashset read it into hashtable */
            while((inputCount = brCount.readLine())!=null){
                String[] parts = inputCount.split("\t");
                if(parts.length == 1){
                    String y = parts[0].split(" ")[0];
                    int count = Integer.parseInt(parts[0].split(" ")[1]);
                    if(y.equals("Y^y*")){
                        ytotal = count;
                    }
                    else{
                        ytable.put(y, count);
                    }

                }
                else{
                    String word = parts[0];
                    if(hashSet.contains(word)){
                        for(int i=1; i<parts.length; i++){
                            String[] token = parts[i].split(",");
                            String key = token[0]+","+token[1];
                            int value = Integer.parseInt(token[2]);
                            hashtable.put(key, value);
                        }
                    }
                }

            }

            //System.out.println("2. hashtable "+ hashtable.size());

            File inputFile2 = new File(inputFileName);
            InputStreamReader reader2 = new InputStreamReader(new FileInputStream(inputFile2));
            BufferedReader brTest2 = new BufferedReader(reader2);

            /* for each example in test, compute log Pr(y, x1, x2, ..., xd) */

            while((inputTest2 = brTest2.readLine())!=null){
                String[] words = inputTest2.split("\t")[1].split(" ");
                Hashtable<String, Double> probability = new Hashtable<String, Double>();

                    Iterator ite = ytable.keySet().iterator();
                    while (ite.hasNext())
                    {
                        String y = (String)ite.next();

                        double part1 = 0;
                        double part2 = 0;
                        for(int j=0; j< words.length; j++){
                            if(hashtable.containsKey(words[j]+","+y)){
//                                System.out.println("y "+ y + " "+c++);

                                double qi = 1.0000000000000000/hashSet.size();
                                double cwy = hashtable.get(words[j]+","+y) + 1;
                                double cy = ytable.get(y) + qi;
//                                double cwy = hashtable.get(words[j]+","+y);
//                                double cy = ytable.get(y);
                                part1 += cwy/cy;
//                                System.out.println("cwy " + cwy);
//                                System.out.println("cy " + cy);
//                                System.out.println("cwy/cy " + cwy/cy);
//                                System.out.println("part1 " + part1);

                            }
                        }

//                        BigDecimal bg1 = new BigDecimal(1 / ytotal);
//                        double qy = bg1.setScale(4, BigDecimal.ROUND_DOWN).doubleValue();

                        double qy = 1.000000000000000000/ytotal;
                        double cy1 = ytable.get(y) + 1;
                        double cyall = ytotal + qy;
//                        double cy1 = ytable.get(y);
//                        double cyall = ytotal;
                        part2 = Math.log(cy1/cyall);
                        double p = Math.log(part1)+part2;
                        if(part1 != 0){
                            probability.put(y, p);
                        }
                    }

                /* get the y with maximum probability */
                String maxKey=null;
                Double maxValue = -1000000.00;
                for(Map.Entry<String,Double> entry : probability.entrySet()) {
                    if(entry.getValue() > maxValue) {
                        maxValue = entry.getValue();
                        maxKey = entry.getKey();
                    }
                }
                //y^ds
                String Y=maxKey.substring(2);
                //System.out.println();
                bw.write(Y+"\t"+maxValue+"\n");
                bw.flush();
                //System.out.println(probability.size());
                probability.clear();
            }

            brCount.close();
            brTest.close();
            brTest2.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
