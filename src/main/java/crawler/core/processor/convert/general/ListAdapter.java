package crawler.core.processor.convert.general;

import crawler.core.main.model.Page;
import crawler.core.processor.convert.ElementConverter;
import crawler.settings.PageSetting;
import lombok.NonNull;
import org.jsoup.select.Elements;

import crawler.core.processor.convert.CollectionConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Transforms element into {@linkplain URL}
 * Created by Максим on 1/8/2017.
 */
public final class ListAdapter<I> implements CollectionConverter<I, List<? extends I>> {

    private final ElementConverter<I> delegate;

    public ListAdapter(@NonNull ElementConverter<I> delegate) {
        this.delegate = delegate;
    }

    @Override
    public Class<? extends List<? extends I>> converts() {
        return (Class<? extends List<? extends I>>) new ArrayList<I>().getClass();//Class<? extends List<? extends I>>) List.class;
    }

    @Override
    public List<? extends I> convert(Elements elements, Page page, PageSetting settings) {
        return elements.stream().map(e -> delegate.convert(e, page, settings)).collect(Collectors.toList());
    }

}
