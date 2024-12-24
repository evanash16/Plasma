package evan.ashley.plasma.translator;

public interface TokenTranslator<T> {

    String encode(T token);

    T decode(String token);
}
