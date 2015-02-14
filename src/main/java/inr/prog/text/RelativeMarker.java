/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inr.prog.text;

import inr.prog.text.api.TextFragment;
import inr.prog.text.api.Marker;

/**
 * {@inheritDoc}
 *
 */
public class RelativeMarker implements Marker {

    private final TextFragment holder;
    private int pos;

    public RelativeMarker(TextFragment holder, int pos) {
        this.holder = holder;
        this.pos = pos;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public int absolutePos() {
        if (holder.marker() == null) {
            return pos();
        } else {
            return pos() + holder.marker().absolutePos();
        }
    }

    @Override
    public int compareTo(Marker o) {
        return Integer.valueOf(pos).compareTo(Integer.valueOf(o.pos()));
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public TextFragment holder() {
        return holder;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public boolean isValid() {
        return pos >= 0;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public int pos() {
        return pos;
    }

    @Override
    public void setInvalid() {
        pos = -1;
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void shift(int shift) {
        pos += shift;
    }

}
