package io.github.ztkmkoo.dss.core.message.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class DssAuthenticationCommandResponseTest {
	
	@Test
    void testToString() {
        final DssAuthenticationCommandResponse response = DssAuthenticationCommandResponse
        		.builder().token("testToken").build();
        assertFalse(response.toString().isEmpty());
    }
	
	@Test
	void getToken() {
		final DssAuthenticationCommandResponse response = DssAuthenticationCommandResponse
        		.builder().token("testToken").build();
        assertEquals("testToken", response.getToken());
	}
}
