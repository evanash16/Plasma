package evan.ashley.plasma.model.api.follow;

import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.util.List;

@Value.Immutable
public interface ListFollowsResponse {

    List<Follow> getFollows();

    @Nullable
    String getPaginationToken();
}
