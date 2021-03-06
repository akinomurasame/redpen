package cc.redpen.validator.sentence;

import cc.redpen.RedPen;
import cc.redpen.RedPenException;
import cc.redpen.config.Configuration;
import cc.redpen.config.ValidatorConfiguration;
import cc.redpen.model.Document;
import cc.redpen.model.Sentence;
import cc.redpen.tokenizer.JapaneseTokenizer;
import cc.redpen.validator.ValidationError;
import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class EndOfSentenceValidatorTest {
    @Test
    public void testInvalidEndOfSentence() {
        EndOfSentenceValidator validator = new EndOfSentenceValidator();
        List<ValidationError> errors = new ArrayList<>();
        validator.validate(errors, new Sentence("He said \"that is right\".", 0));
        assertEquals(1, errors.size());
    }

    @Test
    public void testValidEndOfSentence() {
        EndOfSentenceValidator validator = new EndOfSentenceValidator();
        List<ValidationError> errors = new ArrayList<>();
        validator.validate(errors, new Sentence("He said \"that is right.\"", 0));
        assertEquals(0, errors.size());
    }

    @Test
    public void testInValidEndOfSentenceWithQuestionMark() {
        EndOfSentenceValidator validator = new EndOfSentenceValidator();
        List<ValidationError> errors = new ArrayList<>();
        validator.validate(errors, new Sentence("He said \"Is it right\"?", 0));
        assertEquals(1, errors.size());
    }

    @Test
    public void testVoid() {
        EndOfSentenceValidator validator = new EndOfSentenceValidator();
        List<ValidationError> errors = new ArrayList<>();
        validator.validate(errors, new Sentence("", 0));
        assertEquals(0, errors.size());
    }

    @Test
    public void testJapaneseInvalidEndOfSentence() throws RedPenException {
        Configuration config = new Configuration.ConfigurationBuilder()
                .addValidatorConfig(new ValidatorConfiguration("EndOfSentence"))
                .setLanguage("ja").build();

        List<Document> documents = new ArrayList<>();documents.add(
                new Document.DocumentBuilder(new JapaneseTokenizer())
                        .addSection(1)
                        .addParagraph()
                        .addSentence("彼は言った，“今日は誕生日”。", 1)
                        .build());

        RedPen redPen = new RedPen(config);
        Map<Document, List<ValidationError>> errors = redPen.validate(documents);
        Assert.assertEquals(1, errors.get(documents.get(0)).size());
    }
}
