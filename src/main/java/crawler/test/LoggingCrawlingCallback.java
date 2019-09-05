package crawler.test;

import java.net.URL;
import java.util.logging.Level;

import crawler.core.main.CrawlingCallback;
import crawler.core.main.model.Page;
import lombok.extern.java.Log;

@Log
final class LoggingCrawlingCallback implements CrawlingCallback {

    @Override
    public void onUrlEntered(URL url) {
        log.info("onUrlEntered " + url);
    }

    @Override
    public void onPageAccepted(Page page) {
        log.info("onPageAccepted " + page.getUrl());
    }

    @Override
    public void onPageRejected(Page page) {
        log.info("onPageRejected " + page.getUrl());
    }

    @Override
    public void onStop() {
        log.info("stopped ");
    }

    @Override
    public void onCrawlException(URL url, Throwable th) {
        log.log(Level.WARNING, "crawl error ", th);
    }

    @Override
    public void onInternalException(Throwable th) {
        log.log(Level.WARNING, "onInternalException error ", th);
    }
}
