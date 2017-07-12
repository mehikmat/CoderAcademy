package edu.ncit.mapsidejoin.basic;

import com.google.common.collect.Lists;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.join.CompositeInputFormat;

import java.util.List;

/**
 * Created by hdhamee on 5/29/17.
 */
public class DriverProgram {

    public static void main(String[] args) throws Exception {
        String fieldSeparator = ",";
        String joinKeyIndex = "0";

        int numReducers = 2;

        // input paths
        String jobOneInputPath = args[0];
        String jobTwoInputPath = args[1];

        // sorted and equally partitioned output paths
        String jobOneSortedPath = jobOneInputPath + "_sorted"; // left
        String jobTwoSortedPath = jobTwoInputPath + "_sorted"; // right

        String joinJobOutPath = args[2];

        Job firstSort = Job.getInstance(getConf(joinKeyIndex, fieldSeparator));
        configureJob(firstSort, "firstSort", numReducers, jobOneInputPath, jobOneSortedPath, MapSideJoin.SortByKeyMapper.class, MapSideJoin.SortByKeyReducer.class);

        Job secondSort = Job.getInstance(getConf(joinKeyIndex, fieldSeparator));
        configureJob(secondSort, "secondSort", numReducers, jobTwoInputPath, jobTwoSortedPath, MapSideJoin.SortByKeyMapper.class, MapSideJoin.SortByKeyReducer.class);

        Job mapJoin = Job.getInstance(getMapSideJoinConf(fieldSeparator, jobOneSortedPath, jobTwoSortedPath));
        configureJob(mapJoin, "mapJoin", 0, jobOneSortedPath + "," + jobTwoSortedPath, joinJobOutPath, MapSideJoin.CombineValuesMapper.class, Reducer.class);
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

    private static Configuration getMapSideJoinConf(String separator, String... paths) {
        Configuration config = new Configuration();

        config.set("mapreduce.input.keyvaluelinerecordreader.key.value.separator", separator);

        String joinExpression = CompositeInputFormat.compose("inner", KeyValueTextInputFormat.class, paths);

        config.set("mapreduce.join.expr", joinExpression);
        config.set("separator", separator);

        return config;
    }

    private static Configuration getConf(String separator, String... paths) {
        Configuration config = new Configuration();

        config.set("mapreduce.input.keyvaluelinerecordreader.key.value.separator", separator);
        config.set("separator", separator);

        return config;
    }
}
