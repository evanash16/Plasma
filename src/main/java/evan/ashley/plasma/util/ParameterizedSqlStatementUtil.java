package evan.ashley.plasma.util;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.text.StringSubstitutor;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public class ParameterizedSqlStatementUtil {

    public static ParameterizedSqlStatement build(final String path, Object... parameters) {
        return buildFromTemplate(path, ImmutableMap.of(), parameters);
    }

    public static ParameterizedSqlStatement buildFromTemplate(
            final String path,
            final Map<String, Object> valuesByTemplateString,
            Object... parameters) {
        try (InputStream inputStream = ParameterizedSqlStatementUtil.class.getClassLoader().getResourceAsStream(path)) {
            return ImmutableParameterizedSqlStatement.builder()
                    .sql(StringSubstitutor.replace(
                            new String(Objects.requireNonNull(inputStream).readAllBytes(), StandardCharsets.UTF_8),
                            valuesByTemplateString))
                    .addParameters(parameters)
                    .build();
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
