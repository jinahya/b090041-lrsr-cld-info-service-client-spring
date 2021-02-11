package com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.adapter;

import com.github.jinahya.datagokr.api.b090041_.lrsrcldinfoservice.client.message.Item;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LeapBooleanAdapter extends XmlAdapter<String, Boolean> {

    @Override
    public String marshal(final Boolean v) throws Exception {
        if (v == null) {
            return null;
        }
        return v.equals(Boolean.TRUE) ? Item.LEAP : Item.NORMAL;
    }

    @Override
    public Boolean unmarshal(final String v) throws Exception {
        if (v == null) {
            return null;
        }
        return v.trim().equals(Item.LEAP) ? Boolean.TRUE : Boolean.FALSE;
    }
}
