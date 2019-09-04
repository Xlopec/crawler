package crawler.core.analyze;

import java.util.Set;

import crawler.core.main.model.Page;
import lombok.NonNull;

/**
 * <p>
 * Class which allows to analyze web-pages
 * </p>
 * Created by Максим on 12/18/2016.
 */
public interface Analyzer {

    /**
     * Analyzes page and returns set of analyze results
     *
     * @param page page to analyze
     * @return set of {@linkplain Result}
     */
    @NonNull
    Set<Result> analyze(@NonNull Page page);

    /**
     * Analyzes page and returns set of matching results
     *
     * @param page page to analyze
     * @return set of {@linkplain Result}
     */
    @NonNull
    Set<Result> matchingResults(@NonNull Page page);

}
