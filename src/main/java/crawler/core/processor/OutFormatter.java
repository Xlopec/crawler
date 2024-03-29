package crawler.core.processor;

import crawler.core.main.model.Page;
import crawler.core.main.model.PageID;
import crawler.core.processor.convert.Converter;
import crawler.core.processor.exception.ProcessException;
import crawler.settings.PageSetting;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Максим on 12/19/2016.
 */
public interface OutFormatter {

    void registerAdapter(@NotNull Converter<?, ?> adapter);

    void unregisterAdapter(@NotNull Class<? extends Converter<?, ?>> cl);

    @NotNull
    <R> Optional<? extends Converter<?, R>> getAdapter(@NotNull Class<? extends R> cl);

    @NotNull
    Set<? extends Converter<?, ?>> getRegisteredAdapters();

    void formatPage(@NotNull PageID pageID, @NotNull Page page, @NotNull PageSetting settings) throws ProcessException;

}
