package crawler.core.processor.annotation.invocation;

import crawler.core.main.model.Page;
import lombok.NonNull;
import lombok.val;
import crawler.core.processor.annotation.Context;
import crawler.core.processor.annotation.processor.HandlerProcessor;
import crawler.settings.PageSetting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public final class HandlerInvoker implements Invoker {

    private final Collection<Invoker> invokers;

    public HandlerInvoker(@NonNull Object handler, @NonNull Context context) {

        val handlerProcessor = new HandlerProcessor(handler, context);

        this.invokers = new ArrayList<>(handlerProcessor.getBeforeInvokers());

        val groupInvokers = handlerProcessor.getBindingInvokers().stream()
                .collect(Collectors.groupingBy(GroupedInvoker::group, Collectors.toList()))
                .entrySet().stream()
                .map(e -> new SingleGroupInvoker(e.getValue()))
                .collect(Collectors.toList());

        invokers.addAll(groupInvokers);
        invokers.addAll(handlerProcessor.getAfterInvokers());
    }

    @Override
    public void invoke(Page page, PageSetting settings) {
        invokers.forEach(i -> i.invoke(page, settings));
    }

}


