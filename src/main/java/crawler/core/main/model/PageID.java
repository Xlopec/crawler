package crawler.core.main.model;

import lombok.NonNull;
import lombok.Value;
import crawler.util.Preconditions;
import crawler.util.TextUtils;

/**
 * <p>
 * Page id; can be specified, for example, via code-based config
 * </p>
 * Created by Максим on 12/17/2016.
 */
@Value
public class PageID {
    String id;

    public PageID(@NonNull String id) {
        Preconditions.checkArgument(TextUtils.isNonEmpty(id));
        this.id = id;
    }
}
