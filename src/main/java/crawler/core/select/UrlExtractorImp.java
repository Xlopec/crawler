package crawler.core.select;

import com.google.common.base.Preconditions;

import org.elasticsearch.common.collect.Tuple;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import crawler.core.main.model.Page;
import crawler.core.main.model.PageID;
import crawler.settings.UrlSelectSetting;
import crawler.util.PageUtils;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.java.Log;
import lombok.val;

/**
 * Created by Максим on 12/18/2016.
 */
@Value
@Log
public class UrlExtractorImp implements UrlExtractor {

    Map<PageID, Collection<? extends UrlSelectSetting>> idToSetting;

    public UrlExtractorImp(@NotNull Map<PageID, Collection<? extends UrlSelectSetting>> idToSetting) {
        this.idToSetting = Collections.unmodifiableMap(Preconditions.checkNotNull(idToSetting, "id to settings == null"));
    }

    @Override
    public Set<URL> extract(@NotNull(message = "Page id == null") PageID id,
                            @NotNull(message = "Cannot extract urls from null page") Page page) {

        val selectSettings = idToSetting.get(id);

        return selectSettings == null || !PageUtils.canParse(page.getContentType()) ? Collections.emptySet() : selectSettings
                .stream()
                // transform select setting into tuple which contains both selected elements and setting
                .map(setting -> new Tuple<>(page.toDocument().select(setting.getCssSelector()), setting))
                // get selected elements and transform them into urls
                .map(tuple -> tuple.v1()
                        .stream()
                        .map(elem -> {
                            val baseUrl = tuple.v2().getBaseUrl();

                            baseUrl.ifPresent(url -> elem.setBaseUri(url.toExternalForm()));

                            return elem.absUrl(tuple.v2().getAttrName());
                        })
                        // since method #elem.absUrl returns non-empty strings
                        // for valid urls, we don't need to wrap url instance
                        // creation in try/catch block
                        .filter(absUrl -> absUrl.length() != 0)
                        .map(this::toUrl)
                        .collect(Collectors.toSet())
                )
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @SneakyThrows(MalformedURLException.class)
    private URL toUrl(String s) {
        return new URL(s);
    }

}
