package evan.ashley.plasma.model.dao;

import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.util.List;

@Value.Immutable
public interface ListFollowsOutput {

    List<Follow> getFollows();

    @Nullable
    String getPaginationToken();
}
