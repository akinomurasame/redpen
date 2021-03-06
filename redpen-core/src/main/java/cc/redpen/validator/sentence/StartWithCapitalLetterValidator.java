package cc.redpen.validator.sentence;

import cc.redpen.RedPenException;
import cc.redpen.model.Sentence;
import cc.redpen.tokenizer.TokenElement;
import cc.redpen.util.WordListExtractor;
import cc.redpen.validator.ValidationError;
import cc.redpen.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Check if the input sentence start with a capital letter.
 */
final public class StartWithCapitalLetterValidator extends Validator {
    private static final String DEFAULT_RESOURCE_PATH = "default-resources/capital-letter-exception-list";
    private static final Logger LOG =
            LoggerFactory.getLogger(SpellingValidator.class);
    private Set<String> whiteList;

    public StartWithCapitalLetterValidator() {
        this.whiteList = new HashSet<>();
    }

    public boolean addWhiteList(String item) {
        return whiteList.add(item);
    }

    @Override
    public void validate(List<ValidationError> errors, Sentence sentence) {
        String content = sentence.getContent();
        List<TokenElement> tokens = sentence.getTokens();
        String headWord = "";
        for (TokenElement token : tokens) {
            if (!token.getSurface().equals("")) { // skip white space
                headWord = token.getSurface();
                break;
            }
        }

        if (tokens.size() == 0 || this.whiteList.contains(headWord)) {
            return;
        }

        char headChar = '≡';
        for (char ch: content.toCharArray()) {
            if (ch != ' ') {
                headChar = ch;
                break;
            }
        }

        if (headChar == '≡') {
            return;
        }

        if (Character.isLowerCase(headChar)) {
            errors.add(createValidationError(sentence, headChar));
        }
    }

    @Override
    protected void init() throws RedPenException {
        WordListExtractor extractor = new WordListExtractor();

        LOG.info("Loading default capital letter exception dictionary ");
        String defaultDictionaryFile = DEFAULT_RESOURCE_PATH
                + "/default-capital-case-exception-list.dat";
        try {
            extractor.loadFromResource(defaultDictionaryFile);
        } catch (IOException e) {
            throw new RedPenException("Failed to load default dictionary.", e);
        }
        LOG.info("Succeeded to load default dictionary.");

        Optional<String> confFile = getConfigAttribute("dict");
        confFile.ifPresent(f -> {
            LOG.info("user dictionary file is " + f);
            try {
                extractor.load(new FileInputStream(f));
            } catch (IOException e) {
                LOG.error("Failed to load user dictionary.");
                return;
            }
            LOG.info("Succeeded to load specified user dictionary.");
        });

        whiteList = extractor.get();
    }

    @Override
    public String toString() {
        return "StartWithCapitalLetterValidator{" +
                "whiteList=" + whiteList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StartWithCapitalLetterValidator that = (StartWithCapitalLetterValidator) o;

        return !(whiteList != null ? !whiteList.equals(that.whiteList) : that.whiteList != null);

    }

    @Override
    public int hashCode() {
        return whiteList != null ? whiteList.hashCode() : 0;
    }
}
