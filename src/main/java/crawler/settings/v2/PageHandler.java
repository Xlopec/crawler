package crawler.settings.v2;

import crawler.settings.v2.analyze.ContentAnalyzer;
import crawler.settings.v2.analyze.UrlAnalyzer;
import crawler.settings.AnalyzeWeight;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Can be used to mark a class which should handle crawler's output for a single page. This annotation
 * contain analyzing and processing directives to be used by crawler
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PageHandler {

    /**
     * Should return unique id, so that
     * given page handler can be distinguished among
     * other. If supplied value is empty, then default one will
     * be generated
     */
    String id() default "";

    /**
     * <p>
     * Species a minimal weight which indicates whether current web-resource
     * should be accepted to be processed later by crawler
     * </p>
     * <p>
     * Suppose minimal weight to accept a page is 70, then sum of weights of
     * all matched rules of {@linkplain ContentAnalyzer} should be equal or greater
     * than 70 in order to be accepted for later processing by crawler
     * </p>
     */
    int minWeight() default AnalyzeWeight.DEFAULT_WEIGHT;

    /**
     * Analyze rules to use
     */
    @NotNull ContentAnalyzer[] analyzers();

    /**
     * Urls selectors to use
     */
    @NotNull UrlAnalyzer[] urlSelectors() default {};

    /**
     * Base url for links in case their full
     * url cannot be extracted from context
     */
    @Nullable String baseUrl() default "";

}
