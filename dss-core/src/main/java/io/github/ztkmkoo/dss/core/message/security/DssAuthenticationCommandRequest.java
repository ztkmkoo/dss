package io.github.ztkmkoo.dss.core.message.security;

import java.util.Objects;

import io.github.ztkmkoo.dss.core.message.rest.DssRestMasterActorCommandRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DssAuthenticationCommandRequest implements DssAuthenticationCommand {

	private final String userID;
	private final String userPassword;
	
	@Builder
	private DssAuthenticationCommandRequest(String userID, String userPassword) {
		Objects.requireNonNull(userID);
		Objects.requireNonNull(userPassword);
		
		this.userID = userID;
		this.userPassword = userPassword;
	}
	
	protected DssAuthenticationCommandRequest(DssAuthenticationCommandRequest request) {
        this(
                request.getUserID(),
                request.getUserPassword()
        );
    }
	
	 @Override
	    public String toString() {
	        return "DssAuthenticationCommandRequest{" +
	        		"userID: '" + userID + "', " +
	        		"userPassword: '" + userPassword + "', ";
	 }
	
}
