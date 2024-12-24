package evan.ashley.plasma.model.dao.user;

import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.util.List;

@Value.Immutable
public interface SearchUsersOutput {

    List<User> getUsers();

    @Nullable
    String getPaginationToken();
}
