package evan.ashley.plasma.model.api.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
@JsonDeserialize(as = ImmutableSearchUsersRequest.class)
public interface SearchUsersRequest {

    String getUsernameSearchString();

    @Nullable
    Integer getMaxPageSize();

    @Nullable
    String getPaginationToken();
}
