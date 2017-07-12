package edu.ncit.mapsidejoin.basic;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.join.TupleWritable;

import java.io.IOException;
import java.util.List;

/**
 * Created by hdhamee on 7/11/17.
 */
public class MapSideJoin {

    public static class SortByKeyMapper extends Mapper<LongWritable, Text, Text, Text> {
        private int keyIndex;
        private Splitter splitter;
        private Joiner joiner;
        private Text joinKey = new Text();

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            String separator =  context.getConfiguration().get("separator");
            keyIndex = Integer.parseInt(context.getConfiguration().get("keyIndex"));
            splitter = Splitter.on(separator);
            joiner = Joiner.on(separator);
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            Iterable<String> values = splitter.split(value.toString());
            joinKey.set(Iterables.get(values,keyIndex));
            if(keyIndex != 0){
                value.set(reorderValue(values,keyIndex));
            }
            context.write(joinKey,value);
        }

        private String reorderValue(Iterable<String> value, int index){
            List<String> temp = Lists.newArrayList(value);
            String originalFirst = temp.get(0);
            String newFirst = temp.get(index);
            temp.set(0,newFirst);
            temp.set(index,originalFirst);
            return joiner.join(temp);
        }
    }

    public static class SortByKeyReducer extends Reducer<Text,Text,NullWritable,Text> {
        private static final NullWritable nullKey = NullWritable.get();

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            for (Text value : values) {
                context.write(nullKey, value);
            }
        }
    }

    public static class CombineValuesMapper extends Mapper<Text, TupleWritable, NullWritable, Text> {
        private static final NullWritable nullKey = NullWritable.get();
        private Text outValue = new Text();
        private StringBuilder valueBuilder = new StringBuilder();
        private String separator;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            separator = context.getConfiguration().get("separator");
        }

        @Override
        protected void map(Text key, TupleWritable value, Context context) throws IOException, InterruptedException {
            valueBuilder.append(key).append(separator);
            for (Writable writable : value) {
                valueBuilder.append(writable.toString()).append(separator);
            }
            valueBuilder.setLength(valueBuilder.length() - 1);
            outValue.set(valueBuilder.toString());
            context.write(nullKey, outValue);
            valueBuilder.setLength(0);
        }
    }
}
