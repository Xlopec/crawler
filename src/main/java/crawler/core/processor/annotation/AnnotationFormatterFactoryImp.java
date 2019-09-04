package crawler.core.processor.annotation;

import crawler.core.processor.OutFormatter;
import crawler.core.processor.FormatterFactory;

import java.util.Collection;

public final class AnnotationFormatterFactoryImp implements FormatterFactory {
    @Override
    public OutFormatter create(Collection<?> handlers) {
        return new AnnotationOutFormatterImp(handlers);
    }
}
