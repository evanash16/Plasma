package evan.ashley.plasma.model.api.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import evan.ashley.plasma.model.api.ImmutableUpdateUserRequest;
import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
@JsonDeserialize(as = ImmutableUpdateUserRequest.class)
public interface UpdateUserRequest {

    @Nullable
    String getUsername();

    @Nullable
    String getPassword();
}
