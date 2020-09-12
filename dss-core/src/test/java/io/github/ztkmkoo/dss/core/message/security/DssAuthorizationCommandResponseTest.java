package io.github.ztkmkoo.dss.core.message.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class DssAuthorizationCommandResponseTest {
	
	@Test
    void testToString() {
        final DssAuthorizationCommandResponse response = DssAuthorizationCommandResponse
        		.builder().valid("true").build();
        assertFalse(response.toString().isEmpty());
    }
	
	@Test
	void getValid() {
		final DssAuthorizationCommandResponse response = DssAuthorizationCommandResponse
        		.builder().valid("true").build();
        assertEquals("true", response.getValid());
	}
}
