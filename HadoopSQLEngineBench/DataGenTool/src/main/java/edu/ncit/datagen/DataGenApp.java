package edu.ncit.datagen;

import edu.ncit.datagen.options.DataGenOptions;
import edu.ncit.datagen.util.StructuredData;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class DataGenApp extends Configured implements Tool {

    public static final boolean DEBUG_MODE = true;

    public static void main(String[] args) throws Exception {
        int result = ToolRunner.run(new Configuration(), new DataGenApp(), args);
        System.exit(result);
    }

    @Override
    public int run(String[] args) throws Exception {

        DataGenOptions options = new DataGenOptions(args);
        switch (options.getType()) {
            case HIVE: {
                StructuredData data = new StructuredData(options);
                data.generate();
                break;
            }
            default:
                break;
        }
        return 0;
    }
}
