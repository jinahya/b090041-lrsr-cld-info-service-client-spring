package com.github.jinahya.datagokr.api.b090041.lrsrcldinfoservice.client.message.value;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

@Slf4j
class ItemTest {

    @Test
    void unmarshal() throws Exception {
        try (InputStream resource = getClass().getResourceAsStream("item.xml")) {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document document = builder.parse(resource);
            final JAXBContext context = JAXBContext.newInstance(Item.class);
            final Unmarshaller unmarshaller = context.createUnmarshaller();
            final Item item = unmarshaller.unmarshal(document, Item.class).getValue();
            log.debug("item: {}", item);
            log.debug("lunar date: {}", item.getLunarDate());
            log.debug("solar date: {}", item.getSolarDate());
        }
    }
}