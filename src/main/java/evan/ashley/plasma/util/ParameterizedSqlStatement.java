package evan.ashley.plasma.util;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public interface ParameterizedSqlStatement {

    String getSql();

    List<Object> getParameters();
}
