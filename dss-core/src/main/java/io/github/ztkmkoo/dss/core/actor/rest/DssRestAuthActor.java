package io.github.ztkmkoo.dss.core.actor.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import io.github.ztkmkoo.dss.core.message.security.DssAuthCommand;
import io.github.ztkmkoo.dss.core.message.security.DssAuthenticationCommandRequest;
import io.github.ztkmkoo.dss.core.message.security.DssAuthenticationCommandResponse;
import io.github.ztkmkoo.dss.core.message.security.DssAuthorizationCommandRequest;
import io.github.ztkmkoo.dss.core.message.security.DssAuthorizationCommandResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class DssRestAuthActor {
	
	public static Behavior<DssAuthCommand> create(Map<String, String> userList) {
        return Behaviors.setup(context -> new DssRestAuthActor(context, userList).dssRestAuthActor());
    }
	
	private final ActorContext<DssAuthCommand> context;
	private final Map<String, String> userList;
	private final Map<String, SecretKey> tokenList;
    
    private DssRestAuthActor(ActorContext<DssAuthCommand> context, Map<String, String> userList) {
        this.context = context;
        this.userList = userList;
        this.tokenList = new HashMap<>();
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
    	SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    	
    	if(userList.get(userID).equals(userPassword)) {
    		token = createToken(userID, key);
    		tokenList.put(token, key);
    	}
    	
    	request.getTokenInfo().tell(DssAuthenticationCommandResponse
    			.builder()
    			.token(token)
    			.key(key)
    			.build()
    			);
    	
    	return Behaviors.same();
    }
    
    private Behavior<DssAuthCommand> handlingDssAuthorizationCommandRequest(DssAuthorizationCommandRequest request) {
    	
    	context.getLog().info("DssAuthorizationCommandRequest: {}", request);
    	
    	String token = request.getToken();
    	SecretKey key = request.getKey();
    	String valid = "false";
    	
    	if(tokenList.get(token).equals(key) && verifyToken(token, key)) {
    		valid = "true";
    	}
    	
    	request.getValid().tell(DssAuthorizationCommandResponse
    			.builder()
    			.valid(valid)
    			.build()
    			);
    	
    	return Behaviors.same();
    }
    
    private String createToken(String userID, SecretKey key) {
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
                .signWith(key) 
                .compact(); 

        return jwt;
    }
    
    private boolean verifyToken(String token, SecretKey key) {
    	Jws<Claims> jws;
    	
        try {
            jws = Jwts.parserBuilder()
            		.setSigningKey(key)
            		.build()
            		.parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
        	return false;
        }
    }    
}
