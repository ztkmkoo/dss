package io.github.ztkmkoo.dss.core.message.security;

import javax.crypto.SecretKey;

import io.jsonwebtoken.io.Encoders;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DssAuthenticationCommandResponse implements DssAuthCommand {
	private static final long serialVersionUID = -3012014937865358233L;
	
	private final String token;
	private final SecretKey key;
	
	@Builder
	private DssAuthenticationCommandResponse (String token, SecretKey key) {		
		this.token = token;
		this.key = key;
	}
	
	@Override
	public String toString() {
		 return "DssAuthCommandResponse{" +
	        	"token: '" + token + "', " +
				"key: '" + Encoders.BASE64.encode(key.getEncoded()) + "'" +
	        	"}";
	 }
}
