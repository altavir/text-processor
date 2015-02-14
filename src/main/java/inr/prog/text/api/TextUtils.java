/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inr.prog.text.api;

import inr.prog.text.Text;
import java.util.Collection;

/**
 *
 * @author Darksnake
 */
public class TextUtils {

    public static int numberOfSentences(Text text) {
        return text.findFragments((fr) -> fr.type() == FragmentType.SENTENCE).size();
    }

    public static int numberOfWords(Text text) {
        return text.findFragments((fr) -> fr.type() == FragmentType.WORD).size();
    }

    public static int numberOfSymbold(Text text) {
        // Functional style!
        return text.findFragments((fr) -> fr.type() == FragmentType.WORD)
                .stream()
                .mapToInt(TextFragment::size)
                .sum();

    }

    public static int replaceWord(Text text, String word, String replace) {
        Collection<TextFragment> words = text.findFragments((fr) -> (fr.type() == FragmentType.WORD) && (fr.toString().equals(word)));
        for (TextFragment fragment : words) {
            fragment.replace(new Text(replace));
        }
        return words.size();
    }

}
