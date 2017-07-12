package edu.ncit.mapsidejoin.broadcast;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * Created by hdhamee on 7/12/17.
 */
public class DriverProgram {

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Braodcast Join");
        job.setJarByClass(BroadcastJoin.class);

        // Input parameters
        Path donationsPath = new Path(args[0]);
        Path projectsPath = new Path(args[1]);
        Path outputPath = new Path(args[2]);

        // Create cache file and set path in configuration to be retrieved later by the mapper
        job.addCacheFile(projectsPath.toUri());
        job.getConfiguration().set(BroadcastJoin.BroadcastJoinMapper.PROJECTS_FILENAME_CONF_KEY, projectsPath.getName());

        // Mapper configuration
        job.setMapperClass(BroadcastJoin.BroadcastJoinMapper.class);

        job.setInputFormatClass(TextInputFormat.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        // No need to reduce
        job.setNumReduceTasks(0);

        FileInputFormat.setInputPaths(job, donationsPath);
        FileOutputFormat.setOutputPath(job, outputPath);

        System.exit(job.waitForCompletion(true) ? 0 : 1);

    }
}
