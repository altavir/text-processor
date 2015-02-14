/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inr.prog.text.api;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.function.Predicate;

/**
 * The fragment of the text binded to some source. Any changes in the fragment
 * are reflected on the source.
 *
 * @author Darksnake
 */
public interface TextFragment {

    public static final Comparator<TextFragment> comparator
            = (TextFragment o1, TextFragment o2) -> o1.marker().compareTo(o2.marker());

    /**
     * Find all subFragments with a given predicate.
     *
     * @param condition
     * @return
     */
    Collection<TextFragment> findFragments(Predicate<TextFragment> condition);

    /**
     * The type of this fragment
     *
     * @return
     */
    FragmentType type();

    /**
     * Create new fragment equal to this one but attached to a different marker
     *
     * @param marker
     * @return
     */
    TextFragment attach(Marker marker);

    /**
     * The sorted map of all subfragments. Could be empty. Subfragments could
     * overlap.
     *
     * @return
     */
    SortedSet<TextFragment> fragments();

    /**
     * insert marked fragment at a given position
     *
     * @param pos
     * @param fragment
     */
    void insert(int pos, TextFragment fragment);

    /**
     * insert unmarked string at a given position
     *
     * @param pos
     * @param string
     */
    void insert(int pos, String string);

    /**
     * The marker in the parent fragment to which this fragment is attached. If
     * {@code null} then this fragment is absolute.
     *
     * @return
     */
    Marker marker();

    /**
     * Remove this fragment and corresponding marker
     */
    void remove();

    /**
     * Remove the part of this fragment
     *
     * @param from
     * @param to
     */
    void remove(int from, int to);

    /**
     * Replace part of fragment with given string
     *
     * @param from
     * @param to
     * @param str
     */
    void replace(int from, int to, String str);

    void replace(TextFragment fragment);

    default void replace(String str) {
        replace(0, size(), str);
    }

    /**
     * Length of this fragment
     *
     * @return
     */
    int size();

    /**
     * The source text for this fragment
     *
     * @return
     */
    String source();

    /**
     * Mark new relative fragment, remember it in this TextFragment and return
     * it.
     *
     * @param from
     * @param to
     * @param type
     * @return
     */
    TextFragment markFragment(int from, int to, FragmentType type);

    /**
     * String representation of this fragment
     *
     * @return
     */
    @Override
    String toString();
}
