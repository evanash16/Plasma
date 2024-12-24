package evan.ashley.plasma.util;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;

public class OptionalUtil {

    public static <T, U> U resolveOptionalValue(
            @Nullable final T optional,
            final Function<T, Boolean> extractRemove,
            final Function<T, U> extractValue) {
        if (optional == null) {
            return null;
        }

        if (Optional.ofNullable(extractRemove.apply(optional)).orElse(false)) {
            return null;
        }

        return extractValue.apply(optional);
    }
}
