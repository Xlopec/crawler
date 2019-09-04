package crawler.core.processor.annotation;

import org.jsoup.nodes.Element;

import crawler.core.main.model.Page;
import crawler.settings.PageSetting;
import crawler.core.processor.convert.ElementConverter;

public final class StubCallAdapter implements ElementConverter<Page> {

    private static final class Holder {
        private static final StubCallAdapter INSTANCE = new StubCallAdapter();
    }

    private StubCallAdapter() {
    }

    public static StubCallAdapter getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public Class<? extends Page> converts() {
        return Page.class;
    }

    @Override
    public Page convert(Element element, Page page, PageSetting settings) {
        return page;
    }
}
