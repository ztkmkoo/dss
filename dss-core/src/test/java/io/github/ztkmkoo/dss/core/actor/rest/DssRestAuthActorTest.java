package io.github.ztkmkoo.dss.core.actor.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.actor.AbstractDssActorTest;
import io.github.ztkmkoo.dss.core.message.security.DssAuthCommand;
import io.github.ztkmkoo.dss.core.message.security.DssAuthenticationCommandRequest;
import io.github.ztkmkoo.dss.core.message.security.DssAuthenticationCommandResponse;

public class DssRestAuthActorTest extends AbstractDssActorTest {

	 private static final TestProbe<DssAuthCommand> probe = testKit.createTestProbe();
	 private static final ActorRef<DssAuthCommand> restAuthActorRef = testKit.spawn(DssRestAuthActor.create(testUserList()), "rest-auth");
	 
	 private static Map<String, String> testUserList() {
		 final Map<String, String> userList = new HashMap<>();
		 userList.put("testID", "testPassword");
		 
		 return userList;
	 }
	 
	 @Test
	 void handlingDssAuthenticationCommandRequest() {
		 restAuthActorRef.tell(DssAuthenticationCommandRequest
				 .builder()
				 .userID("testID")
				 .userPassword("testPassword")
				 .token(probe.ref())
				 .build());
		 
		 probe.expectMessageClass(DssAuthenticationCommandResponse.class);
		 assertTrue(true);
	 }
	 
	 @Test
	 void handlingDssAuthenticationCommandRequestErrorMethodType() {
		 restAuthActorRef.tell(DssAuthenticationCommandRequest
				 .builder()
				 .userID("testID")
				 .userPassword("testPassword")
				 .token(probe.ref())
				 .build());
		 
		 final DssAuthCommand command = probe.receiveMessage(Duration.ofSeconds(1));
		 assertEquals(DssAuthenticationCommandResponse.class, command.getClass());
		 
		 final DssAuthenticationCommandResponse response = (DssAuthenticationCommandResponse) command;
	     assertNotNull(response);
	     assertEquals("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." + 
	                  "eyJzdWIiOiJ1c2VyIiwiZGF0YSI6InRlc3RJRCIsImV4cCI6MTU5OTc0NTc0OX0." + 
	    		      "pCJxMBr9HGiWW9Eq7U8L3nw_-sP-1U3ZwCxqWwMv9-o",
	        		  response.getToken());
	 }
}
