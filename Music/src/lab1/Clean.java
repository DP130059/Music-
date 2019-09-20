package lab1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;
import shapeless.newtype;

public class Clean {
	public static void main(String args[]) {
		SparkConf sc = new SparkConf();
		sc.setAppName("NBA Cluster");
		sc.setMaster("local");
		JavaSparkContext jsContext = new JavaSparkContext(sc);
		JavaRDD<String> rawArtistaliasdata = jsContext.textFile("src/lab1/artist_alias.txt");
		JavaRDD<String> parseArtistaliasdata = rawArtistaliasdata.flatMap(new FlatMapFunction<String, String>() {

			@Override
			public Iterator<String> call(String singleline) throws Exception {
				return Arrays.asList(singleline.split("\n")).iterator();
			}
		});
		JavaPairRDD<Long, Long> artistalias = parseArtistaliasdata.mapToPair(new PairFunction<String, Long, Long>() {

			@Override
			public Tuple2<Long, Long> call(String arg0) throws Exception {
				Long badid = 0L;
				Long goodid = 0L;
				if (!arg0.split("\t")[0].equals(""))
					badid = Long.parseLong(arg0.split("\t")[0]);
				if (!arg0.split("\t")[1].equals(""))
					goodid = Long.parseLong(arg0.split("\t")[1]);
				return new Tuple2<Long, Long>(badid, goodid);
			}
		});
		Map<Long, Long> aliasMap = artistalias.collectAsMap();
		JavaRDD<String> rawuserdata = jsContext.textFile("src/lab1/user_artist_data.txt");
		JavaRDD<String> parseuserdata = rawuserdata.flatMap(new FlatMapFunction<String, String>() {

			@Override
			public Iterator<String> call(String singleline) throws Exception {
				return Arrays.asList(singleline.split("\n")).iterator();
			}
		});
		JavaPairRDD<Users, Long> dirtydata = parseuserdata.mapToPair(new PairFunction<String, Users, Long>() {

			@Override
			public Tuple2<Users, Long> call(String arg0) throws Exception {
				Long userid = Long.parseLong(arg0.split(" ")[0]);
				Long artistid = Long.parseLong(arg0.split(" ")[1]);
				Long number = Long.parseLong(arg0.split(" ")[2]);
				if (aliasMap.containsKey(artistid))
					artistid = aliasMap.get(artistid);
				Users user_artist = new Users(userid, artistid);
				return new Tuple2<Users, Long>(user_artist, number);
			}
		});
		JavaPairRDD<Users, Long> cleandata = dirtydata.reduceByKey(new Function2<Long, Long, Long>() {

			@Override
			public Long call(Long arg0, Long arg1) throws Exception {

				return arg0 + arg1;
			}
		});
		cleandata.coalesce(1).saveAsTextFile("src/lab1/user_artist_clean_data");
		jsContext.close();

	}

}
