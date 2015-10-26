import java.io.*;

/**
 * Created by hanxi on 14/09/2015.
 */
public class MergeCounts {
    public static void main(String[] args){
        String w = null;
        String y = null;
        int count = 0;
        String cW = null;
        String cY = null;
        int cCount = 0;
        String cValue = null;

        String y2 = null;
        int count2 = 0;
        String cY2 = null;
        int cCount2 = 0;
        String cValue2 = null;

        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
            String input;
            while ((input = br.readLine()) != null) {
                String parts[] = input.split("\t");
                if(parts.length<=2){
                    y2 = parts[0];
                    count2 = Integer.parseInt(parts[1]);
                    if(cY2 == null){
                        cY2 = y2;
                        cCount2 = count2;
                        cValue2 = cY2 + " " + cCount2;
                    }
                    else if(cY2 != null && cY2.equals(y2)){
                        cCount2 += count2;
                        cValue2 = cY2 + " " + cCount2;
                    }
                    else{
                        //System.out.println(cValue2);
                        bw.write(cValue2+"\n");
                        bw.flush();
                        cCount2 = count2;
                        cY2 = y2;
                        cValue2 = null;
                    }
                }
                else{
                    w = parts[0];
                    y = parts[1];
                    count = Integer.parseInt(parts[2]);

                    if(cW == null){
                        cW = w;
                        cY = y;
                        cCount = count;
                        cValue = cW;
                    }
                    else if(cW != null && cW.equals(w)){
                        if(cY.equals(y)){
                            cCount += count;
                            cW = w;
                            cY = y;
                        }
                        else{
                            cValue = cValue + "\t" + cW + "," + cY + "," +cCount;
                            cCount = count;
                            cW = w;
                            cY = y;
                        }
                    }
                    else{
                        cValue = cValue+ "\t" + cW + "," + cY + "," +cCount;
                        //System.out.println(cValue);
                        bw.write(cValue+"\n");
                        bw.flush();
                        cCount = count;
                        cW = w;
                        cY = y;
                        cValue = cW;
                    }
                }
            }

            if(cValue!=null){
                if(cW.equals(w)){
                    if(cY.equals(y)){
                        cCount += count;
                        cW = w;
                        cY = y;
                        cValue = cValue+ "\t" + cW + "," + cY + "," +cCount;
                        //System.out.println(cValue);
                        bw.write(cValue+"\n");
                        bw.flush();
                    }
                    else{
                        cValue = cValue+ "\t" + cW + "," + cY + "," +cCount;
                        //System.out.println(cValue);
                        bw.write(cValue + "\n");
                        cCount = count;
                        cW = w;
                        cY = y;
                        bw.write(cW + "," + cY + "," +cCount+"\n");
                        bw.flush();
                    }
                }

            }

            if(cValue2!=null){
                if(cY2.equals(y2)){
                    cCount2 += count2;
                    cValue2 = cY2 + " " + cCount2;
                    //System.out.println(cValue2);
                    bw.write(cValue2+"\n");
                    bw.flush();
                }
                else{
                    bw.write(cValue2 + "\n");
                    cCount2 = count2;
                    cY2 = y2;
                    bw.write(cY2 + " " + cCount2 + "\n");
                    bw.flush();
                }
            }

            br.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
