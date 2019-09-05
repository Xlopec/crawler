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
