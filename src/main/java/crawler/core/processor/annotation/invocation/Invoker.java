package crawler.core.processor.annotation.invocation;

import crawler.core.main.model.Page;
import crawler.settings.PageSetting;
import lombok.NonNull;

/**
 * Subclasses which implement this interface should invoke corresponding underlying handler's
 * method and pass given page as argument
 */
public interface Invoker {
    /**
     * Implement to pass given page to an underlying handler
     *  @param page page to pass
     * @param settings
     */
    void invoke(@NonNull Page page, PageSetting settings);
}
