package io.github.ztkmkoo.dss.core.actor.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import io.github.ztkmkoo.dss.core.message.security.DssAuthCommand;
import io.github.ztkmkoo.dss.core.message.security.DssAuthenticationCommandRequest;
import io.github.ztkmkoo.dss.core.message.security.DssAuthenticationCommandResponse;
import io.github.ztkmkoo.dss.core.message.security.DssAuthorizationCommandRequest;
import io.github.ztkmkoo.dss.core.message.security.DssAuthorizationCommandResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class DssRestAuthActor {
	
	public static Behavior<DssAuthCommand> create(Map<String, String> userList) {
        return Behaviors.setup(context -> new DssRestAuthActor(context, userList).dssRestAuthActor());
    }
	
	private final ActorContext<DssAuthCommand> context;
	private final Map<String, String> userList;
	private final List<String> tokenList;
    
    private DssRestAuthActor(ActorContext<DssAuthCommand> context, Map<String, String> userList) {
        this.context = context;
        this.userList = userList;
        this.tokenList = new ArrayList<>();
    }
    
    private Behavior<DssAuthCommand> dssRestAuthActor() {
        return Behaviors
                .receive(DssAuthCommand.class)
                .onMessage(DssAuthenticationCommandRequest.class, this::handlingDssAuthenticationCommandRequest)
                .onMessage(DssAuthorizationCommandRequest.class, this::handlingDssAuthorizationCommandRequest)
                .build();
    }
    
    private Behavior<DssAuthCommand> handlingDssAuthenticationCommandRequest(DssAuthenticationCommandRequest request) {
    	
    	context.getLog().info("DssAuthenticationCommandRequest: {}", request);
    	
    	String userID = request.getUserID();
    	String userPassword = request.getUserPassword();
    	String token = null;
    	
    	if(userList.get(userID).equals(userPassword)) {
    		token = createToken(userID);
    		tokenList.add(token);
    	}
    	
    	request.getToken().tell(DssAuthenticationCommandResponse
    			.builder()
    			.token(token)
    			.build()
    			);
    	
    	return Behaviors.same();
    }
    
    private Behavior<DssAuthCommand> handlingDssAuthorizationCommandRequest(DssAuthorizationCommandRequest request) {
    	
    	context.getLog().info("DssAuthorizationCommandRequest: {}", request);
    	
    	String userID = request.getUserID();
    	String token = request.getToken();
    	String valid = null;
    	
    	if(tokenList.contains(token) && verifyToken(userID, token)) {
    		valid = "true";
    	}
    	
    	request.getValid().tell(DssAuthorizationCommandResponse
    			.builder()
    			.valid(valid)
    			.build()
    			);
    	
    	return Behaviors.same();
    }
    
    private String createToken(String userID) {
    	Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        Map<String, Object> payloads = new HashMap<>();
        payloads.put("data", userID);

        Long expiredTime = 1000 * 60L * 60L * 2L;

        Date ext = new Date();
        ext.setTime(ext.getTime() + expiredTime);
     
        String jwt = Jwts.builder()
                .setHeader(headers) 
                .setClaims(payloads) 
                .setSubject("user")
                .setExpiration(ext)
                .signWith(SignatureAlgorithm.HS256, userID.getBytes()) 
                .compact(); 

        return jwt;
    }
    
    private boolean verifyToken(String userID, String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(userID.getBytes("UTF-8"))
                    .parseClaimsJws(token)
                    .getBody();
            
            if(claims.get("data", String.class).equals(userID)) {
            	return true;
            } else {
            	return false;
            }
            
        } catch (ExpiredJwtException e) {
        	return false;
        } catch (Exception e) {
        	return false;
        }
    }    
}
