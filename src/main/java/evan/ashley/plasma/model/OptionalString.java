package evan.ashley.plasma.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
@JsonDeserialize(as = ImmutableOptionalString.class)
public interface OptionalString {

    @Nullable
    String getValue();

    @Nullable
    Boolean isRemove();
}
