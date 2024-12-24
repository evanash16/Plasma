package evan.ashley.plasma.model.api.post;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import evan.ashley.plasma.model.OptionalString;
import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
@JsonDeserialize(as = ImmutableUpdatePostRequest.class)
public interface UpdatePostRequest {

    @Nullable
    String getTitle();

    @Nullable
    OptionalString getBody();
}
