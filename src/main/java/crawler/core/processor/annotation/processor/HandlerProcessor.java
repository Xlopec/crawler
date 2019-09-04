package crawler.core.processor.annotation.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import crawler.core.processor.annotation.Context;
import crawler.core.processor.annotation.invocation.GroupedInvoker;
import crawler.core.processor.annotation.invocation.Invoker;
import crawler.core.processor.annotation.invocation.MethodInvoker;
import crawler.core.processor.annotation.invocation.PostLifecycleInvoker;
import crawler.core.processor.annotation.invocation.PreLifecycleInvoker;
import lombok.Value;
import lombok.val;

@Value//todo rename bindingInvokers
public final class HandlerProcessor {

    private final Collection<? extends Invoker> beforeInvokers;
    private final Collection<? extends Invoker> afterInvokers;
    private final Collection<? extends GroupedInvoker> bindingInvokers;

    public HandlerProcessor(Object handler, Context context) {

        val beforeInvokers = new ArrayList<Invoker>(1);
        val afterInvokers = new ArrayList<Invoker>(1);
        val bindingInvokers = new ArrayList<GroupedInvoker>(1);

        for (val method : handler.getClass().getMethods()) {

            if (method.getDeclaringClass() == Object.class) {
                continue;
            }

            if (PreLifecycleInvoker.canHandle(method)) {
                beforeInvokers.add(new PreLifecycleInvoker(method, handler));

            } else if (PostLifecycleInvoker.canHandle(method)) {
                afterInvokers.add(new PostLifecycleInvoker(method, handler));

            } else if (MethodInvoker.canHandle(method)) {
                bindingInvokers.add(new MethodInvoker(method, handler, context));
            }
        }

        this.beforeInvokers = Collections.unmodifiableCollection(beforeInvokers);
        this.afterInvokers = Collections.unmodifiableCollection(afterInvokers);
        this.bindingInvokers = Collections.unmodifiableCollection(bindingInvokers);
    }

}
