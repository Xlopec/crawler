package crawler.core.analyze;

import java.util.Set;

import crawler.core.main.model.PageID;
import crawler.settings.AnalyzeTemplate;
import crawler.settings.AnalyzeWeight;
import lombok.Value;

/**
 * <p>
 * Page analyze result. Contains page identifier, calculated match weight and matching rules
 * </p>
 * Created by Максим on 12/18/2016.
 */
@Value
public class Result {
    PageID id;
    Weight calculatedWeight;
    AnalyzeWeight minAcceptableWeight;
    Set<? extends AnalyzeTemplate> matchedRules;
    Set<? extends AnalyzeTemplate> rules;

    public boolean isMatching() {
        return calculatedWeight.getWeight() >= minAcceptableWeight.getWeight();
    }
}
