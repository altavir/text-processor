/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inr.prog.text;

/**
 *
 * @author Darksnake
 */
@FunctionalInterface
public interface StopCondition {

    boolean stop(CharSequence fragment, CharSequence nextChar);
}
