package edu.ncit.mapsidejoin.broadcast;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableUtils;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class BroadcastJoin {

	public static class BroadcastJoinMapper extends Mapper<Object, Text, Text, Text> {
		public static final String PROJECTS_FILENAME_CONF_KEY = "projects.filename";
		private Map<String, Text> projectsCache = new HashMap<>();
		private Text outputKey = new Text();
		private Text outputValue = new Text();

		@Override
		public void setup(Context context) throws IOException, InterruptedException {
			boolean cacheOK = false;

			URI[] cacheFiles = context.getCacheFiles();
			final String distributedCacheFilename = context.getConfiguration().get(PROJECTS_FILENAME_CONF_KEY);

			Configuration conf = new Configuration();
			for (URI cacheFile : cacheFiles) {
				Path path = new Path(cacheFile);
				if (path.getName().equals(distributedCacheFilename)) {
					System.out.println("Starting to build cache from : " + cacheFile);
					try (SequenceFile.Reader reader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(path)))
					{
						System.out.println("Compressed ? " + reader.isBlockCompressed());
						Text tempKey = new Text();
						Text tempValue = new Text();

						while (reader.next(tempKey, tempValue)) {
							// Clone the writable otherwise all map values will be the same reference to tempValue
							Text project = WritableUtils.clone(tempValue, conf);
							projectsCache.put(tempKey.toString(), project);
						}
					}
					System.out.println("Finished to build cache. Number of entries : " + projectsCache.size());

					cacheOK = true;
					break;
				}
			}

			if (!cacheOK) {
				System.out.println("Distributed cache file not found : " + distributedCacheFilename);
				throw new IOException("Distributed cache file not found : " + distributedCacheFilename);
			}
		}

		@Override
		public void map(Object key, Text donation, Context context)	throws IOException, InterruptedException {
			String[] parts = donation.toString().split("(?<!\\\\),", -1);
			String project = projectsCache.get(parts[0]).toString();
			String[] partss = project.split("(?<!\\\\),", -1);

			// Ignore if the corresponding entry doesn't exist in the projects data (INNER JOIN)
			if (project == null) {
				return;
			}

			String donationOutput = String.format("%s|%s|%s|%s|%.2f", parts[0], parts[1],parts[4], parts[8], parts[11]);

			String projectOutput = String.format("%s|%s|%d|%s",	parts[0], parts[1],parts[4], parts[8], parts[11]);

			outputKey.set(donationOutput);
			outputValue.set(projectOutput);
			context.write(outputKey, outputValue);
		}
	}
}
