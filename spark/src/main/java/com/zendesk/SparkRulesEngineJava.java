package com.zendesk;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SparkRulesEngineJava {
    private static final Pattern SPACE = Pattern.compile(" ");

    public static void main(String[] args) {

        final SparkSession spark = SparkSession
                .builder()
                .master("local[*]")
                .appName("SparkRulesEngineJava")
                .getOrCreate();

        spark.sparkContext().setLogLevel("ERROR");
        List<String> words = Arrays.asList(
                "hello world",
                "hello zendesk",
                "zendesk is a great place to work"
        );
        JavaRDD<String> lines = spark.createDataset(words, Encoders.STRING()).javaRDD();
        JavaPairRDD<String, Integer> counts = lines
                .flatMap(s -> Arrays.asList(SPACE.split(s)).iterator())
                .mapToPair(s -> new Tuple2<>(s, 1))
                .reduceByKey((i1, i2) -> i1 + i2);

        List<Tuple2<String, Integer>> output = counts.collect();
        for (Tuple2<?, ?> tuple : output) {
            System.out.println(tuple._1() + ": " + tuple._2());
        }
        spark.stop();
    }
}