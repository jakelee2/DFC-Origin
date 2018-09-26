package org.dbadmin.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.dbadmin.model.DataModel;
import org.dbadmin.model.NonNumericalDataModel;
import org.dbadmin.model.NumericalDataModel;

/**
 * This Util Class compute the statistical model
 * of numerical field and non-numerical field of data table
 */
public class StatisticsUtils {

    private static final double OUTLIERS_THRESHOLD = .50;

    /**
     * Compute Data Model for Numerical Field.
     *
     * @param list Collection of all values for a column
     * @return a Data Model either Numerical Or Non-numerical depends on the table type
     */
    public static DataModel getNumericalDataModel(List<String> list) {
        NumericalDataModel dataModel = new NumericalDataModel();
        List<Double> nonnull_values =
            list.stream().filter(p -> p != null).map(p -> Double.parseDouble(p))
                .collect(Collectors.toList());

        DescriptiveStatistics stats = new DescriptiveStatistics();
        nonnull_values.forEach(p -> stats.addValue(p));
        Set<Double> uniqueValues = new HashSet<Double>(nonnull_values);

        dataModel.max = Collections.max(nonnull_values);
        dataModel.min = Collections.min(nonnull_values);
        dataModel.p99 = stats.getPercentile(99);
        dataModel.p95 = stats.getPercentile(95);
        dataModel.p90 = stats.getPercentile(90);
        dataModel.p75 = stats.getPercentile(75);
        dataModel.p50 = stats.getPercentile(50);
        dataModel.p25 = stats.getPercentile(25);
        dataModel.p10 = stats.getPercentile(10);
        dataModel.p5 = stats.getPercentile(5);
        dataModel.p1 = stats.getPercentile(1);
        dataModel.std = stats.getStandardDeviation();
        dataModel.mean = stats.getMean();
        dataModel.unique = uniqueValues.size();
        dataModel.missing = list.size() - nonnull_values.size();
        dataModel.non_missing = nonnull_values.size();
        dataModel.missing_percent = dataModel.missing / list.size();
        return dataModel;
    }

    /**
     * compute the non numerical Data Model field
     *
     * @param list list Collection of all values for a column
     * @return a Data Model either Numerical Or Non-numerical depends on the table type
     */
    public static DataModel getNonNumericalDataModel(List<String> list) {
        NonNumericalDataModel dataModel = new NonNumericalDataModel();
        List<String> nonnull_values =
            list.stream().filter(p -> p != null).map(p -> p).collect(Collectors.toList());
        Set<String> uniqueValues = new HashSet<>(nonnull_values);

        dataModel.unique = uniqueValues.size();
        dataModel.missing = list.size() - nonnull_values.size();
        dataModel.non_missing = nonnull_values.size();
        dataModel.missing_percent = dataModel.missing / list.size();

        Map<String, Integer> countMap = nonnull_values.parallelStream()
            .collect(Collectors.toConcurrentMap(w -> w, w -> 1, Integer::sum));

        List<Map.Entry<String, Integer>> topKMap = countMap.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).limit(10)
            .collect(Collectors.toList());

        if (topKMap.size() > 0) {
            dataModel.cat_1 = topKMap.get(0).getKey();
            dataModel.freq_1 = topKMap.get(0).getValue();
        }

        if (topKMap.size() > 1) {
            dataModel.cat_2 = topKMap.get(1).getKey();
            dataModel.freq_2 = topKMap.get(1).getValue();
        }

        if (topKMap.size() > 2) {
            dataModel.cat_3 = topKMap.get(2).getKey();
            dataModel.freq_3 = topKMap.get(2).getValue();
        }

        if (topKMap.size() > 3) {
            dataModel.cat_4 = topKMap.get(3).getKey();
            dataModel.freq_4 = topKMap.get(3).getValue();
        }

        if (topKMap.size() > 4) {
            dataModel.cat_5 = topKMap.get(4).getKey();
            dataModel.freq_5 = topKMap.get(4).getValue();
        }

        if (topKMap.size() > 5) {
            dataModel.cat_6 = topKMap.get(5).getKey();
            dataModel.freq_6 = topKMap.get(5).getValue();
        }

        if (topKMap.size() > 6) {
            dataModel.cat_7 = topKMap.get(6).getKey();
            dataModel.freq_7 = topKMap.get(6).getValue();
        }

        if (topKMap.size() > 7) {
            dataModel.cat_8 = topKMap.get(7).getKey();
            dataModel.freq_8 = topKMap.get(7).getValue();
        }

        if (topKMap.size() > 8) {
            dataModel.cat_9 = topKMap.get(8).getKey();
            dataModel.freq_9 = topKMap.get(8).getValue();
        }

        if (topKMap.size() > 9) {
            dataModel.cat_10 = topKMap.get(9).getKey();
            dataModel.freq_10 = topKMap.get(9).getValue();
        }

        return dataModel;
    }

    public static Map<String, String> getChauvenetOutliers(Map<String, String> rowValues) {
        Map<String, String> outLiersMap = new HashMap<>();

        List<String> list = rowValues.values().stream().collect(Collectors.toList());
        List<Double> nonnull_values =
            list.stream().filter(p -> p != null).map(p -> Double.parseDouble(p))
                .collect(Collectors.toList());

        DescriptiveStatistics dStats = new DescriptiveStatistics();
        nonnull_values.forEach(p -> dStats.addValue(p));

        if (list.size() > 5 && dStats.getStandardDeviation() > 0) //Only remove outliers if relatively normal
        {
            double mean = dStats.getMean();
            double stDev = dStats.getStandardDeviation();
            NormalDistribution normalDistribution = new NormalDistribution(mean, stDev);

            double significanceLevel = OUTLIERS_THRESHOLD / list.size();
            for (String rowId : rowValues.keySet()) {
                String valueStr = rowValues.get(rowId);
                if (valueStr == null)
                    continue;
                double num = Double.parseDouble(valueStr);
                double pValue = normalDistribution.cumulativeProbability(num);
                if (pValue < significanceLevel) //Chauvenet's Criterion for Outliers
                {
                    outLiersMap.put(rowId, valueStr);
                }
            }

        }
        return outLiersMap;
    }

    public static Map<String, String> getOutliers(Map<String, String> rowValues) {
        Map<String, String> outLiersMap = new HashMap<>();

        List<String> list = rowValues.values().stream().collect(Collectors.toList());
        List<Double> nonnull_values =
            list.stream().filter(p -> p != null).map(p -> Double.parseDouble(p)).sorted()
                .collect(Collectors.toList());

        DescriptiveStatistics dStats = new DescriptiveStatistics();
        nonnull_values.forEach(p -> dStats.addValue(p));

        double q1 = dStats.getPercentile(25);
        double q3 = dStats.getPercentile(75);
        double iqr = q3 - q1;
        double thresholdMax = q3 + 1.5 * iqr;
        double thresholdMin = q1 - 1.5 * iqr;

        for (String rowId : rowValues.keySet()) {
            String valueStr = rowValues.get(rowId);
            if (valueStr == null)
                continue;
            double num = Double.parseDouble(valueStr);
            if (num < thresholdMin || num > thresholdMax) // Standard outliers
            {
                outLiersMap.put(rowId, valueStr);
            }
        }


        return outLiersMap;
    }

    public static Map<String, String> getDuplicatedValues(Map<String, String> rowValues) {

        Map<String, Integer> count = new HashMap<>();
        for (String value : rowValues.values()) {
            int c = 0;
            if (count.containsKey(value)) {
                c = count.get(value);
            }
            c++;
            count.put(value, c);
        }

        Map<String, String> results = rowValues.entrySet().stream().filter(e -> count.get(e.getValue()) > 1)
            .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

        return results;
    }

    public static Map<String, String> getDuplicatedWithAbnormalValues(Map<String, String> rowValues) {

        int abnormal = rowValues.size() / 2;
        Map<String, Integer> count = new HashMap<>();
        for (String value : rowValues.values()) {
            int c = 0;
            if (count.containsKey(value)) {
                c = count.get(value);
            }
            c++;
            count.put(value, c);
        }

        Map<String, String> results = rowValues.entrySet().stream().filter(e -> count.get(e.getValue()) > abnormal)
            .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

        return results;
    }

    // wrapper class
    public static class LocationWrapper implements Clusterable {
        private double[] points;
        private String id;

        public LocationWrapper(String id, String value) {
            this.id = id;
            int v = Integer.parseInt(value);
            this.points = new double[] {0, v};
        }

        public String getId() {
            return id;
        }

        public double[] getPoint() {
            return points;
        }
    }

    public static Map<String, String> getClusterDetection(Map<String, String> rowValues) {
        List<LocationWrapper> clusterInput = rowValues.entrySet().stream().map(e -> new LocationWrapper(e.getKey(), e.getValue()))
            .collect(Collectors.toList());
        KMeansPlusPlusClusterer<LocationWrapper> clusterer = new KMeansPlusPlusClusterer<>(5, 10000); //5 clusters and 10000 iterations maximum.
        List<CentroidCluster<LocationWrapper>> clusterResults = clusterer.cluster(clusterInput);

        Map<String, String> results = new HashMap<>();
        // output the clusters
        for (int i = 0; i < clusterResults.size(); i++) {
            for (LocationWrapper locationWrapper : clusterResults.get(i).getPoints())
                results.put(locationWrapper.getId(), String.valueOf(i));
        }
        return results;
    }

    public static double[] getFittingDistribution(Map<String, String> rowValues, int degree) {
        final PolynomialCurveFitter fitter = PolynomialCurveFitter.create(degree);
        final WeightedObservedPoints obs = new WeightedObservedPoints();
        rowValues.entrySet().forEach(e -> obs.add(Double.parseDouble(e.getKey()), Double.parseDouble(e.getValue())));
        final double[] coeff = fitter.fit(obs.toList());
        return coeff;
    }
}
