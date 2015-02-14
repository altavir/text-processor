/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inr.prog.text;

import inr.prog.text.api.TextFragment;
import inr.prog.text.api.Marker;
import inr.prog.text.api.FragmentType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Predicate;

public class Text implements TextFragment {

    private final StringBuilder builder;
    private final SortedSet<TextFragment> children;

    public Text(String text) {
        this.builder = new StringBuilder(text);
        children = new TreeSet<>(comparator);
    }

    public Text(String text, SortedSet<TextFragment> children) {
        this.builder = new StringBuilder(text);
        this.children = children;
    }

    public void append(CharSequence sequence) {
        this.builder.append(sequence);
    }

    @Override
    public TextFragment attach(Marker marker) {
        TreeSet<TextFragment> ts = new TreeSet<>(children);
        return new RelativeFragment(marker, size(), ts, type());
    }

    private void checkBounds(int from, int to) {
        if (from < 0 || to > size() || from >= to) {
            throw new IllegalArgumentException("Error in fragment boundaries.");
        }
    }

    private void checkBounds(int pos) {
        if (pos < 0 || pos >= size()) {
            throw new IllegalArgumentException("'pos' is ou of fragment boundaries.");
        }
    }

    @Override
    public Collection<TextFragment> findFragments(Predicate<TextFragment> condition) {
        List<TextFragment> res = new ArrayList<>();
        this.fragments().forEach((TextFragment fr) -> {
            if (condition.test(fr)) {
                res.add(fr);
            }
            res.addAll(fr.findFragments(condition));
        });
        return res;
    }

    @Override
    public SortedSet<TextFragment> fragments() {
        return children;
    }

    @Override
    public void insert(int pos, TextFragment fragment) {
        children.add(fragment.attach(Marker.create(this, pos)));
        insert(pos, fragment.toString());
    }

    @Override
    public void insert(int pos, String string) {
        checkBounds(pos);
        for (TextFragment fragment : fragments()) {
            if (fragment.marker().pos() >= pos) {
                fragment.marker().shift(string.length());
            }
        }
        builder.insert(pos, string);
    }

    @Override
    public TextFragment markFragment(int from, int to, FragmentType type) {
        checkBounds(from, to);
        Marker newMarker = Marker.create(this, from);
        TextFragment fr = new RelativeFragment(newMarker, to - from, null, type);
        children.add(fr);
        return fr;
    }

    @Override
    public Marker marker() {
        return null;
    }

    @Override
    public void remove() {
        throw new IllegalStateException("Can't remove root text");
    }

    @Override
    public void remove(int from, int to) {
        checkBounds(from, to);
        for (Iterator<TextFragment> iterator = children.iterator(); iterator.hasNext();) {
            TextFragment fragment = iterator.next();
            int fragmentStart = fragment.marker().pos();
            int fragmentEnd = fragment.marker().pos() + fragment.size();
            if (fragmentStart > to) {
                fragment.marker().shift(from - to);
            } else if (fragmentStart >= from && fragmentEnd <= to) {
                fragment.marker().setInvalid();
                iterator.remove();
            }
        }
        builder.replace(from, to, "");
    }

    @Override
    public void replace(int from, int to, String str) {
        remove(from, to);
        insert(from, str);
    }

    @Override
    public void replace(TextFragment fragment) {
        throw new IllegalStateException("Can't remove root text");
    }

    @Override
    public int size() {
        return builder.length();
    }

    @Override
    public String source() {
        return toString();
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    @Override
    public FragmentType type() {
        return FragmentType.TEXT;
    }
}
