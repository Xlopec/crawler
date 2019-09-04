package crawler.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.zip.ZipEntry;

import crawler.core.factory.CrawlerFactory;
import crawler.core.main.CrawlingCallback;
import crawler.core.main.model.Page;
import crawler.core.main.model.PageStatus;
import crawler.core.storage.UrlsRepository;
import crawler.settings.JobId;
import crawler.settings.SchedulerSetting;
import lombok.extern.java.Log;
import lombok.val;

@Log
public class Runner {

    public static void main(String... args) throws Exception {

       /* File destDir = new File("unzipped");
        File scrDir = new File("out");
        byte[] buffer = new byte[1024];

        for (val file : scrDir.listFiles()) {

            ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
            ZipEntry zipEntry = zis.getNextEntry();

            while (zipEntry != null) {
                File newFile = newFile(destDir, zipEntry);
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
        }*/
       val startUrls = new HashSet<URL>();

        startUrls.add(new URL("https://www.rulit.me/books/uk/1/date"));
      //  startUrls.add(new URL("https://www.rulit.me/books/ariel-download-free-560582.html"));

        val handlers = new HashSet<Object>();

        handlers.add(new UrlCollector());
        handlers.add(new BookArticleHandler());

        CrawlerFactory cf = new MFactory(
                new JobId("diploma"), new SchedulerSetting(Runtime.getRuntime().availableProcessors(),
                1, 3000L, 2000L),
                startUrls,
                handlers,
                new Repo()
        );

        cf.create().start(new CrawlingCallback() {
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
        });
    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

}


class Repo implements UrlsRepository {

    private Map<URL, PageStatus> map = new HashMap<>();


    @Override
    public synchronized void add(URL url, JobId job, PageStatus status) {
        map.putIfAbsent(url, status);
    }

    @Override
    public synchronized void store(URL url, JobId job, PageStatus status) {
        map.put(url, status);
    }

    @Override
    public synchronized void store(JobId job, PageStatus status) {
        for (val e : map.entrySet()) {
            e.setValue(status);
        }
    }

    @Override
    public synchronized void store(Collection<? extends URL> urls, JobId job, PageStatus status) {
        for (val u : urls) {
            map.put(u, status);
        }
    }

    @Override
    public Iterator<URL> urlsIterator(JobId job) {
        return new Iterator<URL>() {
            @Override
            public synchronized boolean hasNext() {
                return map.containsValue(PageStatus.PENDING);
            }

            @Override
            public synchronized URL next() {
                for(val e: map.entrySet()) {
                    if (e.getValue() == PageStatus.PENDING) {
                        return e.getKey();
                    }
                }

                throw new IllegalStateException();
            }
        };
    }
}
