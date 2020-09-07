package io.github.ztkmkoo.dss.core.message.security;

import java.util.Objects;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DssAuthorizationCommandRequest implements DssAuthenticationCommand {
	private static final long serialVersionUID = 378791374837689579L;
	
	private final String userID;
	private final String token;

	@Builder
	private DssAuthorizationCommandRequest(String userID, String token) {
		Objects.requireNonNull(userID);
		Objects.requireNonNull(token);
		
		this.userID = userID;
		this.token = token;
	}
	
	protected DssAuthorizationCommandRequest(DssAuthorizationCommandRequest request) {
        this(
        		request.getUserID(),
        		request.getToken()
        );
    }
	
	 @Override
	    public String toString() {
	        return "DssAuthorizationCommandRequest{" +
	        		"userID: '" + userID + "', " +
	        		"token: '" + token + "'" +
	        		"}";
	 }
}
