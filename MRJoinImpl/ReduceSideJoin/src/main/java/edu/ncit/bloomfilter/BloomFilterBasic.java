package edu.ncit.bloomfilter;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.util.bloom.BloomFilter;
import org.apache.hadoop.util.bloom.Key;
import org.apache.hadoop.util.hash.Hash;

import java.io.IOException;

/**
 * Created by hdhamee on 7/13/17.
 */
public class BloomFilterBasic {

    // Each instance of this mapper outputs a BloomFilter with the data of its own split.
    public static class FilterCreationMapper extends Mapper<Object, Text, NullWritable, BloomFilter> {
        private BloomFilter filter = new BloomFilter(2_000_000, 7, Hash.MURMUR_HASH);
        private int counter = 0;

        @Override
        public void map(Object key, Text project, Context context) throws IOException, InterruptedException {
            // Add joining key ("project_id" in this example) to bloom filter if the condition is met
            String[] parts = project.toString().split(",");
            String subject = (parts[3] != null) ? parts[3].toLowerCase() : "";// primary focus subject
            if (subject.contains("science")) {
                Key filterKey = new Key(parts[1].getBytes());// project id
                filter.add(filterKey);
                counter++;
            }

        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            // Print number of entries in the filter
            System.out.println("Number of entries in BloomFilter : " + counter);

            // Write the filter to HDFS once all maps are finished
            context.write(NullWritable.get(), filter);
        }

    }

    //This reducer will merge all BloomFilters from each split into a single BloomFilter.
    public static class FilterMergingReducer extends Reducer<NullWritable, BloomFilter, NullWritable, NullWritable> {
        public static String FILTER_OUTPUT_FILE_CONF = "bloomfilter.output.file";
        private BloomFilter filter = new BloomFilter(2_000_000, 7, Hash.MURMUR_HASH);

        @Override
        protected void reduce(NullWritable key, Iterable<BloomFilter> values, Context context) throws IOException, InterruptedException {
            // Merge all filters by logical OR
            for (BloomFilter value : values) {
                filter.or(value);
            }

        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            Path outputFilePath = new Path(context.getConfiguration().get(FILTER_OUTPUT_FILE_CONF));
            FileSystem fs = FileSystem.get(context.getConfiguration());

            try (FSDataOutputStream fsdos = fs.create(outputFilePath)) {
                filter.write(fsdos);
            } catch (Exception e) {
                throw new IOException("Error while writing bloom filter to file system.", e);
            }
        }
    }
}
