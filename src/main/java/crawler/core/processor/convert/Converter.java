package crawler.core.processor.convert;

import crawler.core.main.model.Page;
import crawler.settings.PageSetting;
import lombok.NonNull;
import crawler.core.processor.OutFormatter;

/**
 * <p>
 * Class which transforms input {@linkplain I} into desired data type {@linkplain R}
 * </p>
 * <p>
 * In order to be created via reflection a no-args constructor should be supplied, in another case this adapter should
 * be registered via {@linkplain OutFormatter#registerAdapter(Converter)}
 * </p>
 */
public interface Converter<I, R> {

    /**
     * @return data class that will be returned as a result of
     * mapping
     */
    @NonNull
    Class<? extends R> converts();

    /**
     * Converts {@linkplain I} into {@linkplain R}
     *
     * @param i        element to convert
     * @param page     page to format
     * @param settings page settings
     * @return transformed instance of {@linkplain I}
     */
    @NonNull
    R convert(@NonNull I i, @NonNull Page page, @NonNull PageSetting settings);

}
