package crawler.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;

import crawler.core.factory.CrawlerFactory;
import crawler.settings.JobId;
import crawler.settings.SchedulerSetting;
import lombok.extern.java.Log;
import lombok.val;

@Log
public final class Runner {

    public static void main(String... args) throws MalformedURLException {
        // setup urls to start from
        val startUrls = new HashSet<URL>() {
            { add(new URL("https://www.rulit.me/books/uk/1/date")); }
        };
        // add own handlers that will handle relevant pages
        val handlers = new HashSet<Object>() {
            {
                add(new UrlCollector());
                add(new BookArticleHandler());
            }
        };
        // configure crawler and start crawling process
        CrawlerFactory cf = new SimpleFactory(
                new JobId("job"),
                new SchedulerSetting(Runtime.getRuntime().availableProcessors(),
                1, 3000L, 2000L),
                startUrls,
                handlers,
                new SimpleUrlRepository()
        );
        // let's log our progress
        cf.create().start(new LoggingCrawlingCallback());
    }

}


