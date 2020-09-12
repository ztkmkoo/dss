package io.github.ztkmkoo.dss.core.message.security;

import java.util.Objects;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DssAuthenticationCommandResponse implements DssAuthCommand {
	private static final long serialVersionUID = -3012014937865358233L;
	
	private final String token;
	
	@Builder
	private DssAuthenticationCommandResponse (String token) {		
		this.token = token;
	}
	
	@Override
	public String toString() {
		 return "DssAuthenticationCommandResponse{" +
	        	"token: '" + (Objects.nonNull(token)? token : "null") + "'" +
	        	"}";
	 }
}
