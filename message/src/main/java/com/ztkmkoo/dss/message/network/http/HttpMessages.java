package com.ztkmkoo.dss.message.network.http;

import java.io.Serializable;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 3:31
 */
public class HttpMessages {

    private HttpMessages() {}

    public static class Request implements Serializable {

        public final String path;
        public final String content;

        public Request(
                String path,
                String content) {
            this.path = path;
            this.content = content;
        }
    }
}
