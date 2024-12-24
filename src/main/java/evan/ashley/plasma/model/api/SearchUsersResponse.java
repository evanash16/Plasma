package evan.ashley.plasma.model.api;

import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.util.List;

@Value.Immutable
public interface SearchUsersResponse {

    List<User> getUsers();

    @Nullable
    String getPaginationToken();
}
