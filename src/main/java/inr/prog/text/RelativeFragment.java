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

public class RelativeFragment implements TextFragment {

    final Marker marker;
    int size;
    final FragmentType type;

    final SortedSet<TextFragment> children;

//    public RelativeFragment(TextFragment text, int pos, int size, FragmentType type) {
//        this.marker = Marker.create(text, pos);
//        this.size = size;
//        this.children = new TreeSet<>(comparator);
//        this.type = type;
//        text.fragments().add(this);
//    }
    public RelativeFragment(Marker marker, int size, SortedSet<TextFragment> children, FragmentType type) {
        this.marker = marker;
        this.size = size;
        if (children != null) {
            this.children = children;
        } else {
            this.children = new TreeSet<>(comparator);
        }
        this.type = type;
    }

    @Override
    public TextFragment attach(Marker marker) {
        TreeSet<TextFragment> ts = new TreeSet<>(children);
        return new RelativeFragment(marker, size, ts, type);
    }

    private int bias() {
        return marker.pos();
    }

    @Override
    public Collection<TextFragment> findFragments(Predicate<TextFragment> condition) {
        // Functional style!
        // return this.fragments().stream().filter(condition).collect(Collectors.toList());
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
    public TextFragment markFragment(int from, int to, FragmentType type) {
        checkBounds(from, to);
        Marker newMarker = Marker.create(this, from);
        TextFragment fr = new RelativeFragment(newMarker, to - from, null, type);
        children.add(fr);
        return fr;
    }

    private void checkBounds(int from, int to) {
        if (from < 0 || to > size() || from >= to) {
            throw new IllegalArgumentException("Error in fragment boundaries.");
        }
    }

    private void checkBounds(int pos) {
        if (pos < 0 || pos >= size()) {
            throw new IllegalArgumentException("'pos' is out of fragment boundaries.");
        }
    }

    private TextFragment parent() {
        return marker.holder();
    }

    @Override
    public SortedSet<TextFragment> fragments() {
        return children;
    }

    @Override
    public void insert(int pos, TextFragment fragment) {
        insert(pos, fragment.toString());
        children.add(fragment.attach(Marker.create(this, pos)));
    }

    @Override
    public void insert(int pos, String string) {
        checkBounds(pos);
        for (TextFragment fragment : fragments()) {
            if (fragment.marker().pos() >= pos) {
                fragment.marker().shift(string.length());
            }
        }
        parent().insert(pos + bias(), string);
    }

    @Override
    public Marker marker() {
        return marker;
    }

    @Override
    public void remove() {
        for (TextFragment fragment : fragments()) {
            fragment.marker().setInvalid();
        }
        parent().remove(marker().pos(), marker().pos() + size);
//        parent().fragments().remove(this);
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
            //TODO there could be problems with overlaping fragments
        }

        parent().remove(from + bias(), to + bias());
        size -= to - from;
    }

    @Override
    public void replace(int from, int to, String str) {
        remove(from, to);
        insert(from, str);
    }

    @Override
    public void replace(TextFragment fragment) {
        int pos = this.marker().pos();
        remove();
        parent().insert(pos, fragment);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String source() {
        return parent().source();
    }

    @Override
    public String toString() {
        int ap = marker().absolutePos();
        return source().substring(ap, ap + size);
    }

    @Override
    public FragmentType type() {
        return type;
    }

}
