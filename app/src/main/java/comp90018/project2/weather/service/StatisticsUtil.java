package comp90018.project2.weather.service;
import java.util.List;

/**
 * The StatisticsUtil provides static method for statistics calculations.
 */
public class StatisticsUtil {

    /**
     * Calculate the average steps
     * @param list Stores all the steps in a list
     * @return Average steps that user takes
     */
    public double findMean(List<Double> list) {
        double total = 0;
        for (int i = 0; i < list.size(); i++) {
            total += list.get(i);
        }
        return total / list.size();
    }

    /**
     * Calculate the standard deviation of steps
     * @param list Stores all the steps in a list
     * @param mean Average steps parsed from findMean functions
     * @return Standard deviation steps
     */
    public double standardDeviation(List<Double> list, double mean) {
        double sum = 0;
        for (int i = 0; i < list.size(); i++) {
            list.set(i, Math.pow(list.get(i) - mean, 2));
            sum += list.get(i);
        }
        return Math.sqrt(sum / list.size());
    }

    /**
     * Find the peak steps that user takes
     * @param list Stores all the steps in a list
     * @param minPeak Standard steps parsed from standardDeviation functions
     * @return Number of steps
     */
    public int finAllPeaks(List<Double> list, double minPeak) {
        int counter = 0;
        for (int i = 0; i < list.size(); i++) {
            if (i + 2 < list.size()) {
                double one = list.get(i), two = list.get(i + 1), three = list.get(i + 2);
                if (one < two && two > three && two > minPeak) {
                    counter++;
                }
            }
        }
        return counter;
    }

}
