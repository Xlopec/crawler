package crawler.test;

import org.springframework.stereotype.Component;

import crawler.settings.v2.PageHandler;
import crawler.settings.v2.analyze.ContentAnalyzer;
import crawler.settings.v2.analyze.UrlAnalyzer;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;

/**
 * Created by Максим on 12/10/2017.
 */
@Log
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@PageHandler(
        minWeight = 90,
        urlSelectors = {
                @UrlAnalyzer(selector = "#info > noindex > a > img"),
                @UrlAnalyzer(selector = "#info > noindex:nth-child(19) > a > img")
        },
        analyzers = {
                // Has a file link
                @ContentAnalyzer(weight = 30, selector = "#info > noindex:nth-child(19) > a > img"),
                @ContentAnalyzer(weight = 30, selector = "#info > a > img"),
                // Has title
                @ContentAnalyzer(weight = 30, selector = "#program_info_title")
        }
)
public final class BookArticleHandler {

   /* @Handles(selectors = {
            "#info > noindex:nth-child(19) > a > img",
            "#info > a > img"
    })
    public void handleLinks(@NotNull @Binding() URL url, Page page) {
        "https://www.rulit.me/download-books-560582.html?t=doc"
    }*/

}
