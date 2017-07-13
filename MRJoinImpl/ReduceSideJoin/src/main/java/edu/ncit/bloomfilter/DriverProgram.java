package edu.ncit.bloomfilter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.util.bloom.BloomFilter;

/**
 * Created by hdhamee on 7/13/17.
 */
public class DriverProgram {

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "BloomFilter Creation from Project Table");
        job.setJarByClass(BloomFilterBasic.class);

        // Input parameters
        Path projectsPath = new Path(args[0]);
        Path filtersFolder = new Path(args[1]);
        String filterOutput = args[1] + Path.SEPARATOR + "filter";

        // Output parameter (sent to Reducer who will write the bloom filter to file system)
        job.getConfiguration().set(BloomFilterBasic.FilterMergingReducer.FILTER_OUTPUT_FILE_CONF, filterOutput);

        // Mapper configuration
        job.setMapperClass(BloomFilterBasic.FilterCreationMapper.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(BloomFilter.class);

        // Reducer configuration
        job.setReducerClass(BloomFilterBasic.FilterMergingReducer.class);
        job.setOutputFormatClass(NullOutputFormat.class);
        job.setNumReduceTasks(1);

        FileInputFormat.setInputPaths(job, projectsPath);
        FileOutputFormat.setOutputPath(job, filtersFolder);

        System.exit(job.waitForCompletion(true) ? 0 : 1);

    }
}
