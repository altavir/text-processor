/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inr.prog.text.api;

import inr.prog.text.RelativeMarker;

/**
 *
 * @author Darksnake
 */
public interface Marker extends Comparable<Marker> {

    public static Marker create(TextFragment holder, int pos) {
        if (pos < 0 || pos >= holder.size()) {
            throw new IllegalArgumentException();
        }

        return new RelativeMarker(holder, pos);
    }

    /**
     * The text fragment with which this marker is associated
     *
     * @return
     */
    TextFragment holder();

    /**
     * Shift the relative position of this marker by {@code shift}.
     *
     * @param shift
     */
    void shift(int shift);

    /**
     * The relative position of this marker
     *
     * @return
     */
    int pos();

    int absolutePos();

    /**
     * {@code true} if this marker is valid and {@code false} if it has been
     * removed
     *
     * @return
     */
    boolean isValid();

    void setInvalid();
}
