package io.github.ztkmkoo.dss.core.message.security;

import java.util.Objects;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DssAuthorizationCommandResponse implements DssAuthCommand {
	private static final long serialVersionUID = -9093863668942745924L;

	private String valid;
	
	@Builder
	private DssAuthorizationCommandResponse (String valid) {		
		this.valid = valid;
	}
	
	@Override
	public String toString() {
		 return "DssAuthorizationCommandResponse{" +
	        	"valid: '" + (Objects.nonNull(valid)? valid : "null") + "'" +
	        	"}";
	 }
}
