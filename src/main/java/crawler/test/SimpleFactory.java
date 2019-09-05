package crawler.test;

import java.net.URL;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import crawler.core.analyze.Analyzer;
import crawler.core.analyze.AnalyzerImp;
import crawler.core.analyze.PageAnalyzer;
import crawler.core.analyze.PageAnalyzerImp;
import crawler.core.factory.annotation.AnnotationCrawlerFactory;
import crawler.core.select.UrlExtractor;
import crawler.core.select.UrlExtractorImp;
import crawler.core.storage.UrlsRepository;
import crawler.settings.JobId;
import crawler.settings.PageSetting;
import crawler.settings.SchedulerSetting;
import crawler.settings.Settings;
import lombok.NonNull;

public final class SimpleFactory extends AnnotationCrawlerFactory {

    private final UrlsRepository urlsRepository;

    SimpleFactory(@NonNull JobId jobId,
                  @NonNull SchedulerSetting schedulerSetting,
                  @NonNull Set<URL> startUrls,
                  @NonNull Set<Object> handlers,
                  @NonNull UrlsRepository urlsRepository) {
        super(jobId, schedulerSetting, startUrls, handlers);
        this.urlsRepository = urlsRepository;
    }

    protected UrlExtractor createUrlExtractor(@NotNull Settings settings) {
        return new UrlExtractorImp(settings.getPageSettings()
                .stream()
                .collect(Collectors
                        .toMap(PageSetting::getId,
                                s -> s.getSelectSettings().isEmpty() ? Collections.emptyList() : s.getSelectSettings())
                )
        );
    }

    protected Analyzer createAnalyzeManager(@NotNull Settings settings) {
        return new AnalyzerImp(settings.getPageSettings().stream().map(this::createPageAnalyzer).collect(Collectors.toList()));
    }

    protected UrlsRepository createUrlsRepository(@NotNull Settings settings) {
        return urlsRepository;
    }

    private PageAnalyzer createPageAnalyzer(PageSetting setting) {
        return new PageAnalyzerImp(setting.getMinWeight(), setting.getId(), setting.getAnalyzeTemplates());
    }

}
