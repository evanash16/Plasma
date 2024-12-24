package evan.ashley.plasma.translator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import evan.ashley.plasma.model.api.InternalErrorException;
import lombok.AllArgsConstructor;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@AllArgsConstructor
public class TokenTranslatorImpl<T> implements TokenTranslator<T> {

    private final ObjectMapper objectMapper;
    private final TypeReference<T> typeReference;
    private final Base64.Encoder encoder;
    private final Base64.Decoder decoder;

    @Override
    public String encode(final T token) {
        if (token == null) {
            return null;
        }

        try {
            final String serializedToken = objectMapper.writeValueAsString(token);
            return new String(encoder.encode(serializedToken.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        } catch (final JsonProcessingException e) {
            throw new InternalErrorException(String.format("Failed to serialize token: %s", token), e);
        }
    }

    @Override
    @Nullable
    public T decode(@Nullable final String token) {
        if (token == null) {
            return null;
        }

        try {
            final String decodedString = new String(decoder.decode(token.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
            return objectMapper.readValue(decodedString, typeReference);
        } catch (JsonProcessingException e) {
            throw new InternalErrorException(String.format("Failed to deserialize token: %s", token), e);
        }
    }
}
