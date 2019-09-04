package crawler.core.processor.annotation.invocation;

import crawler.core.processor.annotation.util.InvokerUtil;
import crawler.settings.v2.process.Handles;
import crawler.util.Preconditions;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

import javax.annotation.Nullable;
import java.util.Optional;

@Value
final class MethodInfo {
    @Nullable
    @Getter(value = AccessLevel.NONE)
    Handles handles;

    MethodInfo(@Nullable Handles handles) {
        if (handles != null) {
            Preconditions.checkNotNull(handles.selectors(), "Selectors were null for %s", handles);
            InvokerUtil.checkCssSelectorsThrowing(handles.selectors());
        }
        this.handles = handles;
    }

    public Optional<Handles> getHandles() {
        return Optional.ofNullable(handles);
    }

}
