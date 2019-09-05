package crawler.test;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import crawler.core.main.model.PageStatus;
import crawler.core.storage.UrlsRepository;
import crawler.settings.JobId;
import lombok.val;

final class SimpleUrlRepository implements UrlsRepository {

    private final Map<URL, PageStatus> map = new HashMap<>();

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
