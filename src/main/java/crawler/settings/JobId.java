package crawler.settings;

import crawler.util.Preconditions;
import crawler.util.TextUtils;
import lombok.NonNull;
import lombok.Value;

/**
 * Represents crawling job. Can be used to separate url processing queue for different
 * web sites or other resources.
 */
@Value
public class JobId {
    String id;

    public JobId(@NonNull String jobId) {
        Preconditions.checkArgument(TextUtils.isNonEmpty(jobId));
        this.id = jobId;
    }
}
