package crawler.core.analyze;

import java.util.Map;
import java.util.WeakHashMap;

import crawler.util.Preconditions;
import lombok.Value;

/**
 * <p>
 * Page analyze result
 * </p>
 * Created by Максим on 12/18/2016.
 */
@Value
public class Weight implements Comparable<Weight> {

    int weight;

    private static final Map<Integer, Weight> CACHE = new WeakHashMap<>();

    public static Weight ofValue(int weight) {
        return CACHE.computeIfAbsent(weight, Weight::new);
    }

    private Weight(int weight) {
        Preconditions.checkArgument(weight >= 0);
        this.weight = weight;
    }

    @Override
    public int compareTo(Weight o) {
        return Integer.compare(weight, o.weight);
    }
}
