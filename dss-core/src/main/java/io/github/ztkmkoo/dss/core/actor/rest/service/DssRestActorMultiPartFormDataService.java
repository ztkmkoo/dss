package io.github.ztkmkoo.dss.core.actor.rest.service;

import io.github.ztkmkoo.dss.core.actor.exception.DssMultiPartFormDataConvertException;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestContentInfo;
import io.github.ztkmkoo.dss.core.message.rest.DssRestServiceActorCommandRequest;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestContentType;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import io.github.ztkmkoo.dss.core.util.StringUtils;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.MultipartStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 6. 17. 오후 11:18
 */
@Slf4j
public abstract class DssRestActorMultiPartFormDataService extends AbstractDssRestActorService<HashMap<String, Serializable>> {

    private static final DssRestContentInfo formDataContentInfo = DssRestContentInfo
            .builder()
            .contentType(DssRestContentType.MULTIPART_FORM_DATA)
            .charset(CharsetUtil.UTF_8)
            .build();
    private static final Pattern BOUNDARY_PATTERN = Pattern.compile("boundary=\\S+;?");
    private static final Pattern BOUNDARY_NAME_PATTERN = Pattern.compile("name=\"\\S+\"");

    public DssRestActorMultiPartFormDataService(String name, String path, DssRestMethodType methodType) {
        super(name, path, methodType, formDataContentInfo, DssRestContentInfo.APPLICATION_JSON_UTF8);
    }

    @Override
    protected HashMap<String, Serializable> getBody(DssRestServiceActorCommandRequest commandRequest) {
        return getBody(commandRequest.getContent(), commandRequest.getRawContentType());
    }

    @SuppressWarnings("unchecked")
    protected final HashMap<String, Serializable> getBody(String content, String rawContentType) {
        if (StringUtils.isEmpty(content)) {
            return new HashMap<>();
        }

        final String boundary = getBoundaryFromRawContentType(rawContentType);
        if (!StringUtils.isEmpty(boundary)) {
            try (final ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes())){
                @SuppressWarnings({"deprecation", "NullPointerException"})
                final MultipartStream ms = new MultipartStream(bais, boundary.getBytes());

                final HashMap<String, Serializable> map = new HashMap<>();

                while (ms.skipPreamble()) {
                    final String header = ms.readHeaders();
                    final Matcher m = BOUNDARY_NAME_PATTERN.matcher(header);
                    if (m.find()) {
                        final String result = m.group();
                        if (StringUtils.isEmpty(result) || result.length() < 8) {
                            continue;
                        }

                        final String name = result.substring(6, result.length() - 1);

                        final ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
                        ms.readBodyData(baos);
                        final String value = baos.toString();
                        baos.close();

                        map.put(name, value);
                    }
                }

                return map;
            } catch (IOException e) {
                log.error("IOException occurred:", e);
                throw new DssMultiPartFormDataConvertException(e);
            }
        }

        return new HashMap<>();
    }

    static String getBoundaryFromRawContentType(String rawContentType) {
        final Matcher m = BOUNDARY_PATTERN.matcher(rawContentType);
        if (m.find()) {
            final String result = m.group();

            final int lastIndex = result.length() - 1;
            final char lastChar = result.charAt(lastIndex);
            final int fixedLastIndex = lastChar == ';' ? lastIndex - 1 : lastIndex;

            if (fixedLastIndex <= 9) {
                throw new DssMultiPartFormDataConvertException("boundary text is too small " + fixedLastIndex + 1);
            }

            final String boundaryPrefix = "boundary=";
            final int eIndex = result.indexOf(boundaryPrefix) + boundaryPrefix.length();
            return result.substring(eIndex, fixedLastIndex + 1);
        }

        throw new DssMultiPartFormDataConvertException("Cannot find boundary info from content-type header: " + rawContentType);
    }
}
