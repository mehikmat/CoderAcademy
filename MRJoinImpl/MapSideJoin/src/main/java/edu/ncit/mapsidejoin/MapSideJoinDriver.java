package edu.ncit.mapsidejoin;

import com.google.common.collect.Lists;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.join.CompositeInputFormat;

import java.util.List;

/**
 * Created by hdhamee on 5/29/17.
 */
public class MapSideJoinDriver {

    public static void main(String[] args) throws Exception {
        String separator = ",";
        String keyIndex = "0";
        int numReducers = 10;
        String jobOneInputPath = args[0];
        String jobTwoInputPath = args[1];
        String joinJobOutPath = args[2];

        String jobOneSortedPath = jobOneInputPath + "_sorted";
        String jobTwoSortedPath = jobTwoInputPath + "_sorted";

        Job firstSort = Job.getInstance(getConfiguration(keyIndex, separator));
        //configureJob(firstSort, "firstSort", numReducers, jobOneInputPath, jobOneSortedPath, SortByKeyMapper.class, SortByKeyReducer.class);

        Job secondSort = Job.getInstance(getConfiguration(keyIndex, separator));
        //configureJob(secondSort, "secondSort", numReducers, jobTwoInputPath, jobTwoSortedPath, SortByKeyMapper.class, SortByKeyReducer.class);

        Job mapJoin = Job.getInstance(getMapJoinConfiguration(separator, jobOneSortedPath, jobTwoSortedPath));
        //configureJob(mapJoin, "mapJoin", 0, jobOneSortedPath + "," + jobTwoSortedPath, joinJobOutPath, CombineValuesMapper.class, Reducer.class);
        mapJoin.setInputFormatClass(CompositeInputFormat.class);

        List<Job> jobs = Lists.newArrayList(firstSort, secondSort, mapJoin);
        int exitStatus = 0;
        for (Job job : jobs) {
            boolean jobSuccessful = job.waitForCompletion(true);
            if (!jobSuccessful) {
                System.out.println("Error with job " + job.getJobName() + "  " + job.getStatus().getFailureInfo());
                exitStatus = 1;
                break;
            }
        }
        System.exit(exitStatus);
    }

    private static Configuration getMapJoinConfiguration(String separator, String... paths) {
        Configuration config = new Configuration();
        config.set("mapreduce.input.keyvaluelinerecordreader.key.value.separator", separator);
        String joinExpression = CompositeInputFormat.compose("inner", KeyValueTextInputFormat.class, paths);
        config.set("mapred.join.expr", joinExpression);
        config.set("separator", separator);
        return config;
    }

    private static Configuration getConfiguration(String separator, String... paths) {
        Configuration config = new Configuration();
        config.set("mapreduce.input.keyvaluelinerecordreader.key.value.separator", separator);
        String joinExpression = CompositeInputFormat.compose("inner", KeyValueTextInputFormat.class, paths);
        config.set("mapred.join.expr", joinExpression);
        config.set("separator", separator);
        return config;
    }
}
