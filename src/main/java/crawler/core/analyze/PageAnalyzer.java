package crawler.core.analyze;

import crawler.core.main.model.Page;
import lombok.NonNull;

/**
 * <p>Single page analyzer contract</p>
 * Created by Максим on 12/1/2016.
 */
public interface PageAnalyzer {

    /**
     * Analyzes single page
     *
     * @param page page to analyze
     * @return true if web page matches criteria
     */
    @NonNull
    Result analyze(@NonNull Page page);

}
