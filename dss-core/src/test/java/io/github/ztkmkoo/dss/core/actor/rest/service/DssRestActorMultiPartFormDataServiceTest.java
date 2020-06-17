package io.github.ztkmkoo.dss.core.actor.rest.service;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 6. 17. 오후 11:53
 */
public class DssRestActorMultiPartFormDataServiceTest {

    @Test
    public void testGetBoundaryFromRawContentType() {
        final String rawContentType = "multipart/form-data; boundary=ABCDEF;";
        final String boundary = DssRestActorMultiPartFormDataService.getBoundaryFromRawContentType(rawContentType);

        assertEquals("ABCDEF", boundary);
    }
}