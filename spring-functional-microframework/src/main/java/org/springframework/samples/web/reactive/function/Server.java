package org.springframework.samples.web.reactive.function;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.http.server.reactive.ServletHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.ipc.netty.http.server.HttpServer;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.*;

/**
 * https://spring.io/blog/2016/09/22/new-in-spring-5-functional-web-framework
 * <p>
 * https://github.com/poutsma/web-function-sample
 */
public class Server {

    public static final String HOST = "localhost";

    public static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.startReactorServer();
//		server.startTomcatServer();

        System.out.println("Press ENTER to exit.");
        System.in.read();
    }

    public RouterFunction<ServerResponse> routingFunction() {
        PersonRepository repository = new DummyPersonRepository();
        PersonHandler handler = new PersonHandler(repository);

        return nest(path("/person"),
                nest(accept(APPLICATION_JSON),
                        route(GET("/{id}"), handler::getPerson)
                                .andRoute(method(HttpMethod.GET), handler::listPeople)
                ).andRoute(POST("/").and(contentType(APPLICATION_JSON)), handler::createPerson));
    }

    public void startReactorServer() throws InterruptedException {
        RouterFunction<ServerResponse> route = routingFunction();
        HttpHandler httpHandler = toHttpHandler(route);

        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);
        HttpServer server = HttpServer.create(HOST, PORT);
        server.newHandler(adapter).block();
    }

    public void startTomcatServer() throws LifecycleException {
        RouterFunction<?> route = routingFunction();
        HttpHandler httpHandler = toHttpHandler(route);

        Tomcat tomcatServer = new Tomcat();
        tomcatServer.setHostname(HOST);
        tomcatServer.setPort(PORT);
        Context rootContext = tomcatServer.addContext("", System.getProperty("java.io.tmpdir"));
        ServletHttpHandlerAdapter servlet = new ServletHttpHandlerAdapter(httpHandler);
        Tomcat.addServlet(rootContext, "httpHandlerServlet", servlet);
        rootContext.addServletMapping("/", "httpHandlerServlet");
        tomcatServer.start();
    }
}