package crawler.core.processor.annotation.invocation;

import crawler.settings.v2.process.Handles;
import lombok.NonNull;

/**
 * Subclasses which implement this interface should invoke corresponding underlying handler's
 * method and pass given page as argument
 */
public interface GroupedInvoker extends Invoker {

    int group();

    @NonNull
    Handles.CallPolicy executionPolicy();

}
