package crawler.core.processor.annotation.processor;

import crawler.settings.AnalyzeTemplate;
import crawler.settings.AnalyzeWeight;
import crawler.settings.PageSetting;
import crawler.settings.UrlSelectSetting;
import crawler.settings.v2.PageHandler;
import crawler.settings.v2.analyze.UrlAnalyzer;
import crawler.util.Preconditions;
import crawler.util.TextUtils;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import crawler.core.processor.annotation.util.InvokerUtil;

import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class PageSettingsProcessor {
    private final Collection<?> source;

    public PageSettingsProcessor(@NonNull Collection<?> source) {
        this.source = new ArrayList<>(source);
    }

    public List<PageSetting> process() {
        return source.stream()
                .map(PageSettingsProcessor::toPageSettings)
                .collect(Collectors.toList());
    }

    @SneakyThrows(MalformedURLException.class)
    private static PageSetting toPageSettings(Object o) {
        val handler = Preconditions.checkNotNull(o.getClass().getAnnotation(PageHandler.class),
                "Missing %s annotation for %s", PageHandler.class, o);

        val pageBaseUrl = TextUtils.isEmpty(handler.baseUrl()) ? null : new URL(handler.baseUrl());

        return PageSetting.builder()
                .id(InvokerUtil.newPageId(handler, o))
                .minWeight(AnalyzeWeight.ofValue(handler.minWeight()))
                .analyzeTemplates(toAnalyzeTemplates(handler))
                .selectSettings(toUrlAnalyzeTemplates(handler, pageBaseUrl))
                .baseUrl(pageBaseUrl)
                .build();
    }

    private static Collection<? extends AnalyzeTemplate> toAnalyzeTemplates(PageHandler page) {
        return Arrays.stream(page.analyzers()).map(a -> new AnalyzeTemplate(a.selector(), a.weight()))
                .collect(Collectors.toList());
    }

    private static Collection<? extends UrlSelectSetting> toUrlAnalyzeTemplates(PageHandler page, @Nullable URL pageBaseUrl) {
        return Arrays.stream(page.urlSelectors())
                .map(analyzer -> PageSettingsProcessor.toUrlAnalyzeTemplates(analyzer, pageBaseUrl))
                .collect(Collectors.toList());
    }

    @SneakyThrows(MalformedURLException.class)
    private static UrlSelectSetting toUrlAnalyzeTemplates(UrlAnalyzer urlAnalyzer, @Nullable URL pageBaseUrl) {
        final URL url;

        if (TextUtils.isEmpty(urlAnalyzer.baseUrl())) {
            url = pageBaseUrl;
        } else {
            url = new URL(urlAnalyzer.baseUrl());
        }

        return new UrlSelectSetting(urlAnalyzer.selector(), urlAnalyzer.attribute(), url);
    }

}
