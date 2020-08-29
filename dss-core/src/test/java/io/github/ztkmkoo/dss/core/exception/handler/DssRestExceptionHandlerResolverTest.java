package io.github.ztkmkoo.dss.core.exception.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequest;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorJsonService;
import io.github.ztkmkoo.dss.core.exception.annotation.ExceptionHandler;
import io.github.ztkmkoo.dss.core.exception.annotation.ServiceExceptionHandler;
import io.github.ztkmkoo.dss.core.message.rest.DssRestServiceActorCommandRequest;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertFalse;

class DssRestExceptionHandlerResolverTest {

    @Test
    void setExceptionHandlerMapTest() {
        DssRestExceptionHandlerResolver dssRestExceptionHandlerResolver = DssRestExceptionHandlerResolver.getInstance();

        dssRestExceptionHandlerResolver.setExceptionHandlerMap(new DssExceptionHandler() {

            @ExceptionHandler(exception = NullPointerException.class)
            public DssRestServiceResponse globalExceptionHandleMethod(DssRestServiceActorCommandRequest request) {
                return null;
            }

            @ServiceExceptionHandler(service = TestService.class, exception = Exception.class)
            public DssRestServiceResponse serviceExceptionHandleMethod(DssRestServiceActorCommandRequest request) {
                return null;
            }
        });

        assertFalse(dssRestExceptionHandlerResolver.getExceptionHandlerMap().isEmpty());
    }

    static class TestService extends DssRestActorJsonService<TestRequest> {
        public TestService() {
            super(new TypeReference<TestRequest>() {
            }, "test", "/test", DssRestMethodType.GET);
        }

        @Override
        protected DssRestServiceResponse handlingRequest(DssRestServiceRequest<TestRequest> request) {
            return null;
        }
    }

    private static class TestRequest implements Serializable {
        private static final long serialVersionUID = -1108169427984946141L;
    }
}
