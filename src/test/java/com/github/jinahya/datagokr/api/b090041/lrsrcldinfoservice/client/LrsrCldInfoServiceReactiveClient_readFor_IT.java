package com.github.jinahya.datagokr.api.b090041.lrsrcldinfoservice.client;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class LrsrCldInfoServiceReactiveClient_readFor_IT
        extends LrsrCldInfoServiceReactiveClientIT {

//    @EnabledIf("#{"
//               + "systemProperties['" + SYSTEM_PROPERTY_URL + "'] != null"
//               + " && "
//               + "systemProperties['" + SYSTEM_PROPERTY_TOKEN + "'] != null"
//               + " && "
//               + "systemProperties['" + SYSTEM_PROPERTY_KEYS + "'] != null"
//               + "}")
//    @Test
//    void test() {
//        final String token = System.getProperty(SYSTEM_PROPERTY_TOKEN);
//        final String[] keys = System.getProperty(SYSTEM_PROPERTY_KEYS).split(",");
//        clientInstance().readFor(token, keys)
//                .doOnNext(m -> {
//                    final Map<String, Entry> entries = m.getEntries();
//                    assertThat(entries)
//                            .isNotNull()
//                            .hasSize(1);
//                    for (final String key : keys) {
//                        assertThat(entries.get(key))
//                                .isNotNull()
//                                .satisfies(e -> {
//                                    assertThat(e.getKey())
//                                            .isNotNull()
//                                            .isEqualTo(key);
//                                    assertThat(e.getValue())
//                                            .isNotNull()
//                                            .satisfies(v -> {
//                                                final String string;
//                                                try {
//                                                    string = objectMapper().writer().withDefaultPrettyPrinter()
//                                                            .writeValueAsString(v);
//                                                } catch (final JsonProcessingException jpe) {
//                                                    throw new RuntimeException(jpe);
//                                                }
//                                                log.debug("string: {}", string);
//                                            });
//                                    assertThat(e.getDescription())
//                                            .isNotNull();
//                                });
//                    }
//                })
//                .block();
//    }
}
