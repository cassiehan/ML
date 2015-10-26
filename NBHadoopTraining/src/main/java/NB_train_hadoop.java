import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Vector;

/**
 * Created by hanxi on 25/09/2015.
 */
public class NB_train_hadoop {
    public static class Map extends Mapper<Object, Text, Text, IntWritable> {
        private final static IntWritable ONE = new IntWritable(1);
        private Text word = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String[] parts = value.toString().split("\t");
            String[] classes = parts[1].split(",");
            Vector<String> words = tokennizeDoc(parts[2]);

            for(int i=0; i<classes.length; i++){
                for(int j=0; j<words.size(); j++){
                    String outputKey = "Y=" + classes[i] + "," + "W=" + words.get(j);
                    context.write(new Text("Y=" + classes[i] + "," + "W=*"), ONE);
                    context.write(new Text(outputKey), ONE);
                }
                //System.out.println("Y="+classes[i]);
                context.write(new Text("Y="+classes[i]), ONE);
                context.write(new Text("Y=*"), ONE);
            }
        }
    }

    public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for(IntWritable val : values){
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    public static Vector<String> tokennizeDoc(String cur_doc){
        String[] words = cur_doc.split("\\s+"); // split id, class and words
        Vector<String> tokens = new Vector<String>();
        for(int i=0; i<words.length; i++){
            words[i] = words[i].replaceAll("\\W",""); // remove A non-word character
            if(words[i].length()>0){
                tokens.add(words[i]);
            }
        }
        return tokens;
    }
}
