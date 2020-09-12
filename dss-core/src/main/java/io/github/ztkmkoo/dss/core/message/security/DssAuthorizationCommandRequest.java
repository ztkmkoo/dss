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
	private final SecretKey key;
	private final ActorRef<DssAuthCommand> valid;

	@Builder
	private DssAuthorizationCommandRequest(String token, SecretKey key, ActorRef<DssAuthCommand> valid) {
		Objects.requireNonNull(token);
		Objects.requireNonNull(key);
		
		this.token = token;
		this.key = key;
		this.valid = valid;
	}
	
	protected DssAuthorizationCommandRequest(DssAuthorizationCommandRequest request) {
        this(
        		request.getToken(),
        		request.getKey(),
        		request.getValid()
        );
    }
	
	 @Override
	 public String toString() {
		 return "DssAuthorizationCommandRequest{" +
	        	"token: '" + token + "', " +
				"key: '" + Encoders.BASE64.encode(key.getEncoded()) + "', " +
	        	"valid: '" + (Objects.nonNull(valid)? valid.path().name() : "null") + "'" +
	        	"}";
	 }
}
