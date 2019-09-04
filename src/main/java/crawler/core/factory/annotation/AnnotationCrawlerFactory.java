package crawler.core.factory.annotation;

import java.net.URL;
import java.util.Set;

import crawler.core.analyze.Analyzer;
import crawler.core.factory.CrawlerFactory;
import crawler.core.main.Crawler;
import crawler.core.main.CrawlerContext;
import crawler.core.processor.FormatterFactory;
import crawler.core.processor.annotation.AnnotationFormatterFactoryImp;
import crawler.core.processor.annotation.processor.PageSettingsProcessor;
import crawler.core.select.UrlExtractor;
import crawler.core.storage.UrlsRepository;
import crawler.settings.JobId;
import crawler.settings.SchedulerSetting;
import crawler.settings.Settings;
import lombok.NonNull;

/**
 * {@linkplain CrawlerFactory} skeleton realization. Can be used as base for annotation based
 * crawler factories
 */
public abstract class AnnotationCrawlerFactory implements CrawlerFactory {
    private final Settings setting;
    private final Set<?> handlers;

    public AnnotationCrawlerFactory(@NonNull JobId jobId,
                                    @NonNull SchedulerSetting schedulerSetting,
                                    @NonNull Set<? extends URL> startUrls,
                                    @NonNull Set<?> handlers) {
        this.handlers = handlers;
        this.setting = new Settings(jobId, schedulerSetting, startUrls, new PageSettingsProcessor(handlers).process());
    }

    @Override
    public final Crawler create() {
        return new CrawlerContext(createAnalyzeManager(setting), setting, createUrlExtractor(setting),
                createFormatFactory(setting).create(handlers), createUrlsRepository(setting));
    }

    protected FormatterFactory createFormatFactory(@NonNull Settings settings) {
        return new AnnotationFormatterFactoryImp();
    }

    protected abstract UrlExtractor createUrlExtractor(@NonNull Settings settings);

    protected abstract Analyzer createAnalyzeManager(@NonNull Settings settings);

    protected abstract UrlsRepository createUrlsRepository(@NonNull Settings settings);
}
