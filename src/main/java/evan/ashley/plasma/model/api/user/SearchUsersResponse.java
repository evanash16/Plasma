package evan.ashley.plasma.model.api.user;

import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.util.List;

@Value.Immutable
public interface SearchUsersResponse {

    List<User> getUsers();

    @Nullable
    String getPaginationToken();
}
