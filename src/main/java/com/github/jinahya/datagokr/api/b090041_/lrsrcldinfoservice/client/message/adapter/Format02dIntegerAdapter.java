package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Format02dIntegerAdapter extends XmlAdapter<String, Integer> {

    @Override
    public String marshal(final Integer v) throws Exception {
        if (v == null) {
            return null;
        }
        return String.format("%1$02d", v);
    }

    @Override
    public Integer unmarshal(final String v) throws Exception {
        if (v == null) {
            return null;
        }
        return Integer.parseInt(v);
    }
}
