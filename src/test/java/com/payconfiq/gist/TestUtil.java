package com.payconfiq.gist;

import com.payconfiq.gist.scenarios.StarGistScenario;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class TestUtil {

    public static String readJson(String filename){
        try {
            InputStream is = StarGistScenario.class.getClassLoader().getResourceAsStream(filename);
            return IOUtils.toString(is, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
