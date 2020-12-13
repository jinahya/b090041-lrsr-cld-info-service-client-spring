package com.github.jinahya.datagokr.api.b090041.lrsrcldinfoservice.client;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class LrsrCldInfoServiceReactiveClient_readAll_IT
        extends LrsrCldInfoServiceReactiveClientIT {

//    @EnabledIf("#{"
//               + "systemProperties['" + SYSTEM_PROPERTY_URL + "'] != null"
//               + " && "
//               + "systemProperties['" + SYSTEM_PROPERTY_TOKEN + "'] != null"
//               + "}")
//    @Test
//    void test() {
//        final String token = System.getProperty(SYSTEM_PROPERTY_TOKEN);
//        assertThat(clientInstance().readAll(token).block())
//                .isNotNull()
//                .satisfies(m -> {
//                    log.debug("message: {}", m);
//                    try {
//                        final String string = objectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(m);
//                        log.debug("string: {}", string);
//                    } catch (final JsonProcessingException jpe) {
//                        jpe.printStackTrace();
//                    }
//                });
//    }
}
