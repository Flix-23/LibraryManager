package gatling;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

public class StressTest extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8086")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    ScenarioBuilder reviewScenario = scenario("Get review")
            .exec(http("Get review ")
                    .get("/api/v1/review")
                    .check(status().is(200))
            );


    ScenarioBuilder loanScenario = scenario("Get loan")
            .exec(http("Get loan")
                    .get("/api/v1/loan")
                    .check(status().is(200)));

    ScenarioBuilder authorScenario = scenario("Get author")
            .exec(http("Get author")
                    .get("/api/v1/author")
                    .check(status().is(200)));

    ScenarioBuilder userScenario = scenario("Get user")
            .exec(http("Get user")
                    .get("/api/v1/user")
                    .check(status().is(200)));

    ScenarioBuilder getBookScenario = scenario("Get book")
            .exec(http("Get book")
                    .get("/api/v1/book")
                    .check(status().is(200)));

    {
        setUp(
                reviewScenario.injectOpen(rampUsers(5).during(30)),
                authorScenario.injectOpen(rampUsers(4).during(30)),
                loanScenario.injectOpen(rampUsers(5).during(30)),
                getBookScenario.injectOpen(rampUsers(3).during(30)),
                userScenario.injectOpen(rampUsers(5).during(30))

        ).protocols(httpProtocol);
    }
}