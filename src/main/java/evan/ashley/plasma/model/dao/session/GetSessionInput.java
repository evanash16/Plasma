package evan.ashley.plasma.model.dao.session;

import org.immutables.value.Value;

@Value.Immutable
public interface GetSessionInput {

    String getId();
}
