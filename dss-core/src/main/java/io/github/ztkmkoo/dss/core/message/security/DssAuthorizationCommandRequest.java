package io.github.ztkmkoo.dss.core.message.security;

import java.util.Objects;

import akka.actor.typed.ActorRef;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DssAuthorizationCommandRequest implements DssAuthCommand {
	private static final long serialVersionUID = 378791374837689579L;
	
	private final String userID;
	private final String token;
	private final ActorRef<DssAuthCommand> valid;

	@Builder
	private DssAuthorizationCommandRequest(String userID, String token, ActorRef<DssAuthCommand> valid) {
		Objects.requireNonNull(userID);
		Objects.requireNonNull(token);
		
		this.userID = userID;
		this.token = token;
		this.valid = valid;
	}
	
	protected DssAuthorizationCommandRequest(DssAuthorizationCommandRequest request) {
        this(
        		request.getUserID(),
        		request.getToken(),
        		request.getValid()
        );
    }
	
	 @Override
	 public String toString() {
		 return "DssAuthorizationCommandRequest{" +
	        	"userID: '" + userID + "', " +
	        	"token: '" + token + "'" +
	        	"valid: '" + (Objects.nonNull(valid)? valid.path().name() : "null") + "'" +
	        	"}";
	 }
}
