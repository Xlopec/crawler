package crawler.core.processor.annotation.invocation;

import crawler.core.main.model.Page;
import crawler.settings.PageSetting;
import crawler.settings.v2.process.AfterPage;
import crawler.util.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;
import lombok.val;
import crawler.core.processor.annotation.util.InvokerUtil;

import java.lang.reflect.Method;

@EqualsAndHashCode
@ToString
public final class PostLifecycleInvoker implements Invoker {
    private final Method method;
    private final Object target;
    private boolean isPageRequired;

    public static boolean canHandle(Method method) {
        return method.isAnnotationPresent(AfterPage.class);
    }

    public PostLifecycleInvoker(@NonNull Method method, @NonNull Object target) {
        InvokerUtil.checkMethodOrThrow(method, target);
        Preconditions.checkArgument(PostLifecycleInvoker.canHandle(method), "Missing lifecycle annotation");

        val params = method.getParameterTypes();

        Preconditions.checkArgument(params.length <= 1,
                "Invalid number of parameters, one or none should be used");

        if (params.length == 1) {
            Preconditions.checkArgument(params[0].isAssignableFrom(Page.class),
                    String.format("Method argument type should be assignable from %s", Page.class));
        }

        this.method = method;
        this.target = target;
        this.isPageRequired = params.length == 1;
    }

    @Override
    public void invoke(Page page, PageSetting settings) {
        if (isPageRequired) {
            InvokerUtil.invokeWrappingError(method, target, page);
        } else {
            InvokerUtil.invokeWrappingError(method, target);
        }
    }
}
