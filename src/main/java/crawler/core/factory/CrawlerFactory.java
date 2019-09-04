package crawler.core.factory;

import crawler.core.main.Crawler;
import lombok.NonNull;

/**
 * Abstract factory contract to create instances of {@linkplain Crawler}
 */
public interface CrawlerFactory {

    @NonNull
    Crawler create();

}
