/**
 * redpen: a text inspection tool
 * Copyright (C) 2014 Recruit Technologies Co., Ltd. and contributors
 * (see CONTRIBUTORS.md)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.redpen.validator.sentence;

import cc.redpen.RedPen;
import cc.redpen.RedPenException;
import cc.redpen.config.Configuration;
import cc.redpen.config.Symbol;
import cc.redpen.config.ValidatorConfiguration;
import cc.redpen.model.Document;
import cc.redpen.validator.ValidationError;
import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cc.redpen.config.SymbolType.*;

public class SymbolWithSpaceValidatorTest {
    @Test
    public void testNotNeedSpace() throws RedPenException {
        List<Document> documents = new ArrayList<>();
        documents.add(
                new Document.DocumentBuilder()
                        .addSection(1)
                        .addParagraph()
                        .addSentence("I like apple/orange", 1)
                        .build());

        Configuration conf = new Configuration.ConfigurationBuilder()
                .addValidatorConfig(new ValidatorConfiguration("SymbolWithSpace"))
                .setLanguage("en")
                .setSymbol(new Symbol(SLASH, '/'))
                .build();

        RedPen redPen = new RedPen(conf);
        Map<Document, List<ValidationError>> errors = redPen.validate(documents);
        Assert.assertEquals(0, errors.get(documents.get(0)).size());
    }

    @Test
    public void testNeedAfterSpace() throws RedPenException {
        List<Document> documents = new ArrayList<>();
        documents.add(
                new Document.DocumentBuilder()
                        .addSection(1)
                        .addParagraph()
                        .addSentence("I like her:yes it is", 1)
                        .build());

        Configuration conf = new Configuration.ConfigurationBuilder()
                .addValidatorConfig(new ValidatorConfiguration("SymbolWithSpace"))
                .setLanguage("en")
                .setSymbol(new Symbol(COLON, ':', "", false, true))
                .build();

        RedPen redPen = new RedPen(conf);
        Map<Document, List<ValidationError>> errors = redPen.validate(documents);
        Assert.assertEquals(1, errors.get(documents.get(0)).size());
    }

    @Test
    public void testNeedBeforeSpace() throws RedPenException {
        List<Document> documents = new ArrayList<>();
        documents.add(
                new Document.DocumentBuilder()
                        .addSection(1)
                        .addParagraph()
                        .addSentence("I like her(Nancy) very much.", 1)
                        .build());

        Configuration conf = new Configuration.ConfigurationBuilder()
                .addValidatorConfig(new ValidatorConfiguration("SymbolWithSpace"))
                .setLanguage("en")
                .setSymbol(new Symbol(LEFT_PARENTHESIS, '(', "", true, false))
                .build();

        RedPen redPen = new RedPen(conf);
        Map<Document, List<ValidationError>> errors = redPen.validate(documents);
        Assert.assertEquals(1, errors.get(documents.get(0)).size());
    }

    @Test
    public void testNeedSpaceInMultiplePosition() throws RedPenException {
        List<Document> documents = new ArrayList<>();
        documents.add(
                new Document.DocumentBuilder()
                        .addSection(1)
                        .addParagraph()
                        .addSentence("I like her(Nancy)very much.", 1)
                        .build());

        Configuration conf = new Configuration.ConfigurationBuilder()
                .addValidatorConfig(new ValidatorConfiguration("SymbolWithSpace"))
                .setLanguage("en")
                .setSymbol(new Symbol(LEFT_PARENTHESIS, '(', "", true, false))
                .setSymbol(new Symbol(RIGHT_PARENTHESIS, ')', "", false, true))
                .build();

        RedPen redPen = new RedPen(conf);
        Map<Document, List<ValidationError>> errors = redPen.validate(documents);
        Assert.assertEquals(2, errors.get(documents.get(0)).size());
    }

    @Test
    public void testReturnOnlyOneForHitBothBeforeAndAfter() throws RedPenException {
        List<Document> documents = new ArrayList<>();
        documents.add(
                new Document.DocumentBuilder()
                        .addSection(1)
                        .addParagraph()
                        .addSentence("I like 1*10.", 1)
                        .build());

        Configuration conf = new Configuration.ConfigurationBuilder()
                .addValidatorConfig(new ValidatorConfiguration("SymbolWithSpace"))
                .setLanguage("en")
                .setSymbol(new Symbol(ASTERISK, '*', "", true, true))
                .build();

        RedPen redPen = new RedPen(conf);
        Map<Document, List<ValidationError>> errors = redPen.validate(documents);
        Assert.assertEquals(1, errors.get(documents.get(0)).size());
    }
}
