package com.usermanager.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ActionResponseTest {

    @Test
    void testAllArgsConstructorAndGetters() {

        ActionResponse res = new ActionResponse("SUCCESS", "User deleted");

        assertThat(res.getStatus()).isEqualTo("SUCCESS");
        assertThat(res.getMessage()).isEqualTo("User deleted");
    }

    @Test
    void testSettersAndToString() {

        ActionResponse res = new ActionResponse(null, null);

        res.setStatus("OK");
        res.setMessage("Done");

        assertThat(res.getStatus()).isEqualTo("OK");
        assertThat(res.getMessage()).isEqualTo("Done");
        assertThat(res.toString()).contains("OK").contains("Done");
    }

    @Test
    void testEqualsAndHashCode() {

        ActionResponse a = new ActionResponse("SUCCESS", "Saved");
        ActionResponse b = new ActionResponse("SUCCESS", "Saved");

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
