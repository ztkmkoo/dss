package io.github.ztkmkoo.dss.core.message.security;

import java.util.Objects;

import akka.actor.typed.ActorRef;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DssAuthenticationCommandRequest implements DssAuthCommand {
	private static final long serialVersionUID = 1163425179253222602L;
	
	private final String userID;
	private final String userPassword;
	private final ActorRef<DssAuthCommand> tokenInfo;
	
	@Builder
	private DssAuthenticationCommandRequest(String userID, String userPassword, ActorRef<DssAuthCommand> tokenInfo) {
		Objects.requireNonNull(userID);
		Objects.requireNonNull(userPassword);
		
		this.userID = userID;
		this.userPassword = userPassword;
		this.tokenInfo = tokenInfo;
	}
	
	protected DssAuthenticationCommandRequest(DssAuthenticationCommandRequest request) {
        this(
                request.getUserID(),
                request.getUserPassword(),
                request.getTokenInfo()
        );
    }
	
	 @Override
	 public String toString() {
		 return "DssAuthenticationCommandRequest{" +
	        	"userID: '" + userID + "', " +
	        	"userPassword: '" + userPassword + "', " +
	        	"token: '" + (Objects.nonNull(tokenInfo)? tokenInfo.path().name() : "null") + "'" +
	        	"}";
	 }
	
}
