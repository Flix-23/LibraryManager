package gatling;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
import static io.gatling.recorder.internal.bouncycastle.oer.its.ieee1609dot2.basetypes.Duration.seconds;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;


public class RateLimitTest extends Simulation{

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8086")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");


    ScenarioBuilder reviewRateLScenario = scenario("Get review rate limit")
            .exec(http("Get review - rate limit")
                    .get("/api/v1/review")
                    .check(status().in(200, 429))
            );

    ScenarioBuilder loanRateLScenario = scenario("Get loan rate limit")
            .exec(http("Get loan - rate limit")
                    .get("/api/v1/loan")
                    .check(status().in(200, 429))
            );

    ScenarioBuilder authorRateLScenario = scenario("Get author rate limit")
            .exec(http("Get author - rate limit")
                    .get("/api/v1/author")
                    .check(status().in(200, 429)) // Verifica si el estado es 200 o 429
            );


    ScenarioBuilder userRateLScenario = scenario("Get user rate limit")
            .exec(http("Get user - rate limit")
                    .get("/api/v1/user")
                    .check(status().in(200, 429))
            );

    ScenarioBuilder bookRateLScenario = scenario("Get book rate limit")
            .exec(http("Get book - rate limit")
                    .get("/api/v1/book")
                    .check(status().in(200, 429))
            );

    {
        setUp(
                reviewRateLScenario.injectOpen(constantUsersPerSec(30).during(30)),
                loanRateLScenario.injectOpen(constantUsersPerSec(40).during(30)),
                authorRateLScenario.injectOpen(constantUsersPerSec(45).during(30)),
                userRateLScenario.injectOpen(constantUsersPerSec(30).during(30)),
                bookRateLScenario.injectOpen(constantUsersPerSec(10).during(30))
        ).protocols(httpProtocol);

    }
}
