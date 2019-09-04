package crawler.core.analyze;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import crawler.core.main.model.Page;
import crawler.core.main.model.PageID;
import crawler.settings.AnalyzeTemplate;
import crawler.settings.AnalyzeWeight;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.var;
import lombok.val;

/**
 * <p>
 * Default implementation of {@linkplain PageAnalyzer}
 * </p>
 * Created by Максим on 12/1/2016.
 */
@Value
public class PageAnalyzerImp implements PageAnalyzer {

    PageID pageID;
    Set<? extends AnalyzeTemplate> templates;
    AnalyzeWeight minWeight;

    public PageAnalyzerImp(@NonNull AnalyzeWeight minSum, @NonNull PageID pageID, @NonNull Collection<? extends AnalyzeTemplate> templates) {
        this.minWeight = minSum;
        this.pageID = pageID;
        this.templates = Collections.unmodifiableSet(new HashSet<>(templates));
    }

    @Override
    public Result analyze(@NonNull Page page) {
        val doc = page.toDocument();
        var weightSum = 0;
        val matchedRules = new HashSet<AnalyzeTemplate>();

        for (val template : templates) {
            val elements = doc.select(template.getCssSelector());

            if (elements.size() > 0) {
                matchedRules.add(template);
                weightSum += elements.size() * template.getWeight();
            }
        }
        return new Result(pageID, Weight.ofValue(weightSum), minWeight, matchedRules, templates);
    }
}
