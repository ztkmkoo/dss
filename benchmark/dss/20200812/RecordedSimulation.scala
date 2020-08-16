package org.example

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RecordedSimulation extends Simulation {

	val httpProtocol = http
		.baseUrl("http://211.43.12.186:8181")
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
		.doNotTrackHeader("1")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36")

	val scn = scenario("RecordedSimulation")
		.exec(http("request_1")
			.get("/hello")
			.header(HttpHeaderNames.ContentType, HttpHeaderValues.ApplicationJson)
			.body(StringBody("""Test""")))



	setUp(
  		scn.inject(
			nothingFor(4 seconds),
			atOnceUsers(10),
			rampUsers(10) during (5 seconds),
			constantUsersPerSec(20) during (15 seconds),
			constantUsersPerSec(20) during (15 seconds) randomized,
			rampUsersPerSec(10) to 20 during (10 minutes),
			rampUsersPerSec(10) to 20 during (10 minutes) randomized,
			heavisideUsers(1000) during (20 seconds)
		).protocols(httpProtocol)
	)
}
