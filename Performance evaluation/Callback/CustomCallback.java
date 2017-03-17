import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.apache.commons.math3.util.Precision;
import org.dacapo.harness.Callback;
import org.dacapo.harness.CommandLineArgs;
import org.dacapo.harness.TestHarness;

/**
 *
 * @author Philipp Holzinger <philipp.holzinger@sit.fraunhofer.de>
 */
public class CustomCallback extends Callback {

    private ArrayList<Long> customTimes = new ArrayList();
    private static final int NUMWARMUP = 0;
    private static final int NUMTIMEDRUNS = 1000;
    private String benchmarkName = "";

    public CustomCallback(CommandLineArgs args) {
        super(args);
    }

    public void start(String benchmark) {
        super.start(benchmark);
    }

    public void stop() {
        super.stop();
    }

    @Override
    public void complete(String benchmark, boolean valid) {
        this.benchmarkName = benchmark;
        System.out.print("===== DaCapo " + TestHarness.getBuildVersion() + " " + benchmark);
        if (iterations < NUMWARMUP) {
            System.out.print(" WARMUP");
        }
        if (valid) {
            System.out.print(" PASSED ");
            System.out.print("in " + elapsed + " ms (" + (iterations + 1) + " iterations)");
        } else {
            System.out.println(" FAILED ");
            System.out.println("Error 2");
            System.exit(-1);
        }
        System.out.println("=====");
    }

    public double getDeviation(ArrayList<Long> currentTimes) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (Long l : currentTimes) {
            stats.addValue(l);
        }
        return stats.getStandardDeviation();
    }

    public DescriptiveStatistics getStatistics(ArrayList<Long> currentTimes) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (long time : currentTimes) {
            stats.addValue(time);
        }
        return stats;
    }

    public double getMean(ArrayList<Long> currentTimes) {
        return getStatistics(currentTimes).getMean();
    }

    public double getMin(ArrayList<Long> currentTimes) {
        return getStatistics(currentTimes).getMin();
    }

    public double getMax(ArrayList<Long> currentTimes) {
        return getStatistics(currentTimes).getMax();
    }

    public double getMedian(ArrayList<Long> currentTimes) {
        Median median = new Median();
        double[] values = new double[currentTimes.size()];
        for (int i = 0; i < currentTimes.size(); i++) {
            values[i] = currentTimes.get(i);
        }
        return median.evaluate(values);
    }

    @Override
    public boolean runAgain() {
        iterations++;

        if (iterations > NUMWARMUP) {
            customTimes.add(elapsed);
        }

        if (customTimes.size() < NUMTIMEDRUNS) {
            return true;
        }

        double mean = getMean(customTimes);
        double max = getMax(customTimes);
        double min = getMin(customTimes);
        double median = getMedian(customTimes);
        double deviation = getDeviation(customTimes);
        double deviationPercentage = Precision.round((deviation / mean) * 100, 2);

        try {
            printResults(mean, median, max, min, deviation, deviationPercentage);
        } catch (Exception e) {
            System.out.println("Error 1");
            System.exit(-1);
        }
        return false;
    }

    private void printResults(double mean, double median, double max, double min, double deviation, double deviationPercentage) throws IOException {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("results.txt", true)))) {
            out.println("---");
            out.println("Current time: " + getTime());
            out.println("java.home: " + System.getProperty("java.home"));
            out.println("Security manager: " + System.getSecurityManager());
            out.println("Benchmark: " + this.benchmarkName);
            out.println("Warmup rounds: " + NUMWARMUP);
            out.println("Timed runs: " + NUMTIMEDRUNS);
            for (int i = 0; i < customTimes.size(); i++) {
                out.println("Time (" + (i + 1) + "): " + customTimes.get(i) + " ms");
            }

            out.println("Mean: " + mean);
            out.println("Median: " + median);
            out.println("Max: " + max);
            out.println("Min: " + min);
            out.println("Standard deviation: " + deviation + " (" + deviationPercentage + "%)");
        }
    }

    private static String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = Calendar.getInstance().getTime();
        return dateFormat.format(date);
    }
}
