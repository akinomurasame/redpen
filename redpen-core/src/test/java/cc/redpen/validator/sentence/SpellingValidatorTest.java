package cc.redpen.validator.sentence;

import cc.redpen.RedPen;
import cc.redpen.RedPenException;
import cc.redpen.config.Configuration;
import cc.redpen.config.ValidatorConfiguration;
import cc.redpen.model.Document;
import cc.redpen.validator.ValidationError;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class SpellingValidatorTest {

    @Test
    public void testValidate() throws Exception {
        List<Document> documents = new ArrayList<>();
                documents.add(new Document.DocumentBuilder()
                        .addSection(1)
                        .addParagraph()
                        .addSentence("this iz a pen", 1)
                        .build());

        SpellingValidator validator = new SpellingValidator();
        validator.addWord("this");
        validator.addWord("a");
        validator.addWord("pen");
        List<ValidationError> errors = new ArrayList<>();
        validator.validate(errors, documents.get(0).getLastSection().getParagraph(0).getSentence(0));
        assertEquals(1, errors.size());
    }

    @Test
    public void testLoadDefaultDictionary() throws RedPenException {
        Configuration config = new Configuration.ConfigurationBuilder()
                .addValidatorConfig(new ValidatorConfiguration("Spelling"))
                .setLanguage("en").build();

        List<Document> documents = new ArrayList<>();
                documents.add(new Document.DocumentBuilder()
                        .addSection(1)
                        .addParagraph()
                        .addSentence("this iz goody", 1)
                        .build());

        RedPen redPen = new RedPen(config);
        Map<Document, List<ValidationError>> errors = redPen.validate(documents);
        assertEquals(1, errors.get(documents.get(0)).size());
    }

    @Test
    public void testUpperCase() throws RedPenException {
        Configuration config = new Configuration.ConfigurationBuilder()
                .addValidatorConfig(new ValidatorConfiguration("Spelling"))
                .setLanguage("en").build();

        List<Document> documents = new ArrayList<>();
                documents.add(new Document.DocumentBuilder()
                        .addSection(1)
                        .addParagraph()
                        .addSentence("This iz goody", 1)
                        .build());

        RedPen redPen = new RedPen(config);
        Map<Document, List<ValidationError>> errors = redPen.validate(documents);
        assertEquals(1, errors.get(documents.get(0)).size());
    }


    @Test
    public void testSkipCharacterCase() throws RedPenException {
        Configuration config = new Configuration.ConfigurationBuilder()
                .addValidatorConfig(new ValidatorConfiguration("Spelling"))
                .setLanguage("en").build();

        List<Document> documents = new ArrayList<>();
                documents.add(new Document.DocumentBuilder()
                        .addSection(1)
                        .addParagraph()
                        .addSentence("That is true, but there is a condition", 1)
                        .build());

        RedPen redPen = new RedPen(config);
        Map<Document, List<ValidationError>> errors = redPen.validate(documents);
        assertEquals(0, errors.get(documents.get(0)).size());
    }

    @Test
    public void testUserSkipList() throws RedPenException {
        Configuration config = new Configuration.ConfigurationBuilder()
                .addValidatorConfig(new ValidatorConfiguration("Spelling").addAttribute("list", "abeshi,baz"))
                .setLanguage("en").build();

        List<Document> documents = new ArrayList<>();
                documents.add(new Document.DocumentBuilder()
                        .addSection(1)
                        .addParagraph()
                        .addSentence("Abeshi is a word used in a comic.", 1)
                        .build());

        RedPen redPen = new RedPen(config);
        Map<Document, List<ValidationError>> errors = redPen.validate(documents);
        assertEquals(0, errors.get(documents.get(0)).size());
    }

    @Test
    public void testEndPeriod() throws RedPenException {
        Configuration config = new Configuration.ConfigurationBuilder()
                .addValidatorConfig(new ValidatorConfiguration("Spelling"))
                .setLanguage("en").build();

        List<Document> documents = new ArrayList<>();
                documents.add(new Document.DocumentBuilder()
                        .addSection(1)
                        .addParagraph()
                        .addSentence("That is true.", 1)
                        .build());

        RedPen redPen = new RedPen(config);
        Map<Document, List<ValidationError>> errors = redPen.validate(documents);
        assertEquals(0, errors.get(documents.get(0)).size());
    }

    @Test
    public void testVoid() throws RedPenException {
        Configuration config = new Configuration.ConfigurationBuilder()
                .addValidatorConfig(new ValidatorConfiguration("Spelling"))
                .setLanguage("en").build();

        List<Document> documents = new ArrayList<>();
                documents.add(new Document.DocumentBuilder()
                        .addSection(1)
                        .addParagraph()
                        .addSentence("", 1)
                        .build());

        RedPen redPen = new RedPen(config);
        Map<Document, List<ValidationError>> errors = redPen.validate(documents);
        assertEquals(0, errors.get(documents.get(0)).size());
    }
}
