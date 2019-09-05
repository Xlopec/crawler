## Crawler - simple web crawler library
This library is a configurable Java based implementation of web [crawler](https://en.wikipedia.org/wiki/Web_crawler). 
It can be used to test web sites for security vulnerabilities, parse content, grab files from sites, etc.

Let's consider traditional crawler architecture:

![Crawler](WebCrawlerArchitecture.svg)

According to this scheme crawler consist from the following main components/packages with some modifications:
- `core`: a set of page analyzing rules and weights. They are used to compute a page relevance
- `factory`: factories that are responsible for a crawler instance creation. For now, only annotation 
based factories are supported, XML based factories were removed.
- `main`: contains crawler implementation itself together with its main components such as url queue, scheduler, looper, etc.
- `processor`: annotation based class analyzer. It is used to bind user's page handlers to the crawler instance.
- `select`: extracts urls from a page to add to crawler's queue later
- `storage`: storage where crawler stores its urls for later processing. Can be either a simple Map, database or whatever
needed
- `settings`: main configuration parameters of the crawler
- `test`: a sample of usage

Crawler can be used as following:

```java
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
```
Page handler that downloads and stores files from a web page located on https://www.rulit.me/books/ might be defined as following:

```java
package crawler.test;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.logging.Level;

import javax.validation.constraints.NotNull;

import crawler.core.main.model.Page;
import crawler.settings.v2.PageHandler;
import crawler.settings.v2.analyze.ContentAnalyzer;
import crawler.settings.v2.analyze.UrlAnalyzer;
import crawler.settings.v2.process.Binding;
import lombok.extern.java.Log;
import lombok.val;

/**
 * Collector that simply downloads and stores files from web page
 *
 * Created by Максим on 12/10/2017.
 */
@Log
@Component
@PageHandler(
        analyzers = @ContentAnalyzer(selector = "*"),
        baseUrl = "https://www.rulit.me/books/",
        urlSelectors = {
                @UrlAnalyzer(selector = "#ruler > a:nth-child(3)")
        }
)
public final class UrlCollector {

    public void onHandleUri(@NotNull @Binding(selectors = {"#program_name > a"}) URL url, Page page) throws Exception {
        log.log(Level.INFO, String.format("On handle url %s of page %s", url.toExternalForm(), page.getUrl()));

        val strUrl = url.toExternalForm();
        val id = strUrl.replaceAll("\\D", "");
        val outFile = new File("out", String.format("%s.zip", id));

        if (!outFile.exists()) {
            outFile.createNewFile();
        }

        //https://www.rulit.me/books/ariel-download-free-560582.html
        //https://www.rulit.me/download-books-560582.html?t=fb2
       try(val input = new URL(String.format("https://www.rulit.me/download-books-%s.html?t=fb2", id)).openStream();
           val output = new FileOutputStream(outFile)) {

           IOUtils.copy(input, output);
       }

    }

}
```
