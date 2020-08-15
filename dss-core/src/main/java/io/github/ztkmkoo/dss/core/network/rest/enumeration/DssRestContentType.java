package io.github.ztkmkoo.dss.core.network.rest.enumeration;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 25. 오전 1:20
 */
public enum DssRestContentType {
    // application
    APPLICATION_JSON("application/json"),
    APPLICATION_WWW_FORM_URL_ENCODED("application/x-www-form-urlencoded"),
    // audio
    // multipart
    MULTIPART_FORM_DATA("multipart/form-data"),
    // text
    ;

    private static final Map<String, DssRestContentType> textMap = initTextMap();
    @Getter(value = AccessLevel.PRIVATE)
    private final String text;

    DssRestContentType(String text) {
        this.text = text;
    }

    public static DssRestContentType fromText(String text) {  
    	if(Objects.nonNull(text)){
    		return textMap.get(text.split(";")[0]);
    	}
    	return null;
    }

    private static Map<String, DssRestContentType> initTextMap() {
        return Collections.unmodifiableMap(
                Stream
                        .of(values())
                        .filter(Objects::nonNull)
                        .collect(Collectors.toMap(
                                DssRestContentType::getText,
                                dssRestContentType -> dssRestContentType
                        )
                )
        );
    }
}
