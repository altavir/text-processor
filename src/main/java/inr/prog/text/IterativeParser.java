/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inr.prog.text;

import inr.prog.text.api.FragmentType;
import inr.prog.text.api.Parser;
import inr.prog.text.api.TextFragment;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Darksnake
 */
public class IterativeParser implements Parser {

    @Override
    public Text parse(InputStream stream) throws IOException {
        Text text = new Text("");
        Reader reader = new InputStreamReader(stream, Charset.defaultCharset());

        StringBuilder sentence = null;
        StringBuilder word = null;
        int sentenceStart = -1;
        int wordStart = -1;
        Map<Integer, Integer> words = new LinkedHashMap<>();

        for (int i = 0; reader.ready(); i++) {
            char c = (char) reader.read();
            //Adding character to text
            String str = new String(new char[]{c});
            text.append(str);

            //Evaluating words
            if (word == null) {
                if (Character.isLetter(c)) {
                    wordStart = i;
                    word = new StringBuilder(str);
                }
            } else {
                if (wordEndCondition().stop(word, str)) {
                    if (sentence != null) {
                        words.put(wordStart - sentenceStart, i - wordStart);
                    } else {
                        // Word without sentence??
                        text.markFragment(wordStart, i, FragmentType.WORD);
                        Logger.getLogger(getClass().getName()).warning("Word without sentence?");
                    }
                    word = null;
                    wordStart = -1;
                } else {
                    word.append(str);
                }
            }

            //Evaluating sentence
            if (sentence == null) {
                if (Character.isLetter(c)) {
                    sentenceStart = i;
                    sentence = new StringBuilder(str);
                }
            } else {
                if (sentenceEndCondition().stop(sentence, str)) {
                    TextFragment s = text.markFragment(sentenceStart, i, FragmentType.SENTENCE);
                    // adding all word to sentence
                    for (Map.Entry<Integer, Integer> w : words.entrySet()) {
                        s.markFragment(w.getKey(), w.getKey() + w.getValue(), FragmentType.WORD);
//                        TextFragment fr = s.markFragment(w.getKey(), w.getKey() + w.getValue(), FragmentType.WORD);
//                        System.out.println(fr.toString());
                    }
                    words.clear();
                    sentenceStart = -1;
                    sentence = null;
                } else {
                    sentence.append(str);
                }
            }
        }
        return text;
    }

    StopCondition wordEndCondition() {
        return (CharSequence fragment, CharSequence next) -> !Character.isLetter(next.charAt(0));
    }

    StopCondition sentenceEndCondition() {
        return (CharSequence fragment, CharSequence next) -> ".!?".contains(next);
    }

}
