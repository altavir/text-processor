/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inr.prog.text.api;

import inr.prog.text.Text;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Text structure analyzer.
 *
 * @author Darksnake
 */
public interface Parser {

    Text parse(InputStream stream) throws IOException;

    default Text parse(String str) throws IOException {
        return parse(new ByteArrayInputStream(str.getBytes()));
    }

    default Text parse(File file) throws IOException {
        return parse(new FileInputStream(file));
    }

}
