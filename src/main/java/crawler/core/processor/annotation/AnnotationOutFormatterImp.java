package crawler.core.processor.annotation;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import crawler.core.main.model.Page;
import crawler.core.main.model.PageID;
import crawler.core.processor.OutFormatter;
import crawler.core.processor.annotation.invocation.HandlerInvoker;
import crawler.core.processor.annotation.util.InvokerUtil;
import crawler.core.processor.convert.CollectionAdapter;
import crawler.core.processor.convert.Converter;
import crawler.core.processor.convert.general.ListAdapter;
import crawler.core.processor.convert.general.PageAdapter;
import crawler.core.processor.convert.general.SetAdapter;
import crawler.core.processor.convert.general.StringAdapter;
import crawler.core.processor.convert.general.StubAdapter;
import crawler.core.processor.convert.general.UrlAdapter;
import crawler.core.processor.exception.ProcessException;
import crawler.settings.PageSetting;
import crawler.settings.v2.PageHandler;
import crawler.util.Preconditions;
import lombok.NonNull;
import lombok.extern.java.Log;
import lombok.val;

@Log
public final class AnnotationOutFormatterImp implements OutFormatter {

    private final Context context;
    private final Map<PageID, ? extends Collection<HandlerInvoker>> idToHandlers;

    @SuppressWarnings("unchecked")
    public AnnotationOutFormatterImp(@NonNull Collection<?> handlers) {
        Preconditions.checkArgument(!handlers.isEmpty());
        // register default converters
        this.context = new Context(Arrays.asList(
                StubAdapter.getInstance(),
                StringAdapter.getInstance(),
                UrlAdapter.getInstance(),
                PageAdapter.getInstance()
        ));

        context.registerAdapterProvider(Collection.class, c -> new CollectionAdapter(c));
        context.registerAdapterProvider(List.class, c -> new ListAdapter(c));
        context.registerAdapterProvider(Set.class, c -> new SetAdapter(c));

        this.idToHandlers = mapToHandlers(handlers);
    }

    @Override
    public void registerAdapter(Converter<?, ?> adapter) {
        context.registerAdapter(adapter);
    }

    @Override
    public void unregisterAdapter(Class<? extends Converter<?, ?>> cl) {
        context.unregisterAdapter(cl);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> Optional<? extends Converter<?, R>> getAdapter(Class<? extends R> cl) {
        return context.getRegisteredAdapters().stream().filter(a -> cl.isAssignableFrom(a.converts()))
                .map(a -> (Converter<?, R>) a).findFirst();
    }

    @Override
    public Set<? extends Converter<?, ?>> getRegisteredAdapters() {
        return context.getRegisteredAdapters();
    }

    @Override
    public void formatPage(PageID pageID, Page page, PageSetting settings) throws ProcessException {
        synchronized (idToHandlers) {
            Optional.ofNullable(idToHandlers.get(pageID)).ifPresent(handlers -> handlers.forEach(h -> h.invoke(page, settings)));
        }
    }

    private Map<PageID, ? extends Collection<HandlerInvoker>> mapToHandlers(Collection<?> handlers) {
        return handlers.stream().collect(
                Collectors.groupingBy(
                        AnnotationOutFormatterImp::extractPageId,
                        Collectors.mapping(o -> new HandlerInvoker(o, context), Collectors.toList()))
        );
    }

    private static PageID extractPageId(Object o) {
        val handler = Preconditions.checkNotNull(o.getClass().getAnnotation(PageHandler.class),
                String.format("No %s annotation was found for class %s", PageHandler.class.getName(), o.getClass().getName()));

        return InvokerUtil.newPageId(handler, o);
    }

}
