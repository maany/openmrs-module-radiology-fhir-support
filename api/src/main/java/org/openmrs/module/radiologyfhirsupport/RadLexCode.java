package org.openmrs.module.radiologyfhirsupport;

import org.w3c.dom.Document;

/**
 * Created by devmaany on 14/6/16.
 */
public class RadLexCode {
    private String scheme;
    private String code;
    private String meaning;

    public RadLexCode(Document document) {

    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}
