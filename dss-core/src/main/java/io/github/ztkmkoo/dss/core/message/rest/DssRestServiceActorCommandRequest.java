package io.github.ztkmkoo.dss.core.message.rest;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 15. 오전 1:27
 * @deprecated remove before merge pr
 */
@Deprecated
public class DssRestServiceActorCommandRequest extends DssRestMasterActorCommandRequest implements DssRestServiceActorCommand {
    private static final long serialVersionUID = 7974865777802725667L;

    public DssRestServiceActorCommandRequest(DssRestMasterActorCommandRequest request) {
        super(request);
    }
}
