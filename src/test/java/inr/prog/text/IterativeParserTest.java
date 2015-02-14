/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inr.prog.text;

import inr.prog.text.api.TextUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Darksnake
 */
public class IterativeParserTest {

    public IterativeParserTest() {
    }

    @Before
    public void setUp() {
    }

    /**
     * Test of parse method, of class IterativeParser.
     */
    @org.junit.Test
    public void testParse() throws Exception {
        String testString = "Мама мыла рамы. А папа сапоги.";

        IterativeParser parser = new IterativeParser();
        Text text = parser.parse(testString);
        assertEquals(2, TextUtils.numberOfSentences(text));
        assertEquals(6, TextUtils.numberOfWords(text));
        TextUtils.replaceWord(text, "папа", "крокодил");
        System.out.println(text.toString());
    }

}
