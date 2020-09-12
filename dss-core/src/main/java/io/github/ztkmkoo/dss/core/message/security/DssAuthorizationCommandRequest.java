package io.github.ztkmkoo.dss.core.message.security;

import java.util.Objects;

import javax.crypto.SecretKey;

import akka.actor.typed.ActorRef;
import io.jsonwebtoken.io.Encoders;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DssAuthorizationCommandRequest implements DssAuthCommand {
	private static final long serialVersionUID = 378791374837689579L;
	
	private final String token;
	private final ActorRef<DssAuthCommand> valid;

	@Builder
	private DssAuthorizationCommandRequest(String token, ActorRef<DssAuthCommand> valid) {
		Objects.requireNonNull(token);
		
		this.token = token;
		this.valid = valid;
	}
	
	protected DssAuthorizationCommandRequest(DssAuthorizationCommandRequest request) {
        this(
        		request.getToken(),
        		request.getValid()
        );
    }
	
	 @Override
	 public String toString() {
		 return "DssAuthorizationCommandRequest{" +
	        	"token: '" + token + "', " +
	        	"valid: '" + (Objects.nonNull(valid)? valid.path().name() : "null") + "'" +
	        	"}";
	 }
}
