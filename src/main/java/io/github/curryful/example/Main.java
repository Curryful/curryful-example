package io.github.curryful.example;

import static io.github.curryful.example.Hello.sayHello;
import static io.github.curryful.example.Hello.sayHelloName;
import static io.github.curryful.example.Hello.secureHello;
import static io.github.curryful.example.Numbers.getNumbers;
import static io.github.curryful.rest.Server.listen;

import java.util.Base64;

import io.github.curryful.commons.collections.ImmutableArrayList;
import io.github.curryful.commons.collections.ImmutableMaybeHashMap;
import io.github.curryful.commons.collections.MutableArrayList;
import io.github.curryful.commons.collections.MutableMaybeHashMap;
import io.github.curryful.rest.Destination;
import io.github.curryful.rest.Endpoint;
import io.github.curryful.rest.http.HttpContext;
import io.github.curryful.rest.http.HttpMethod;
import io.github.curryful.rest.middleware.PreMiddleware;

public class Main {

	private static final ImmutableMaybeHashMap<String, String> USERS;

	static {
		MutableMaybeHashMap<String, String> users = MutableMaybeHashMap.empty();
		users.put("admin", "admin");
		users.put("user", "password123");
		USERS = users;
	}

	public static final PreMiddleware basicAuth = context -> {
		var authHeader = context.getHeaders().get("Authorization");

		if (!authHeader.hasValue()) {
			return context;
		}

		var authParts = authHeader.getValue().split(" ");

		if (authParts.length != 2 || !authParts[0].equals("Basic")) {
			return context;
		}

		var credentials = new String(Base64.getDecoder().decode(authParts[1]));
		var credentialParts = credentials.split(":");

		if (credentialParts.length != 2) {
			return context;
		}

		var username = credentialParts[0];
		var password = credentialParts[1];

		var dbPassword = USERS.get(username);
		if (!dbPassword.hasValue() || !dbPassword.getValue().equals(password)) {
			return context;
		}

		var headers = MutableMaybeHashMap.of(context.getHeaders());
		headers.put("X-Authenticated-User", username);
		return HttpContext.of(context.getMethod(), context.getActualUri(), context.getFormalUri(),
				context.getPathParameters(), context.getQueryParameters(), headers,
				context.getAddress(), context.getBody());
	};

	public static void main(String[] args) {
		MutableArrayList<PreMiddleware> preMiddleware = MutableArrayList.empty();
		preMiddleware.add(basicAuth);

		MutableArrayList<Endpoint> endpoints = MutableArrayList.empty();
		endpoints.add(Endpoint.of(Destination.of(HttpMethod.GET, "/hello"), sayHello));
		endpoints.add(Endpoint.of(Destination.of(HttpMethod.GET, "/hello/:name"), sayHelloName));
		endpoints.add(Endpoint.of(Destination.of(HttpMethod.GET, "/secure/hello"), secureHello));
		endpoints.add(Endpoint.of(Destination.of(HttpMethod.GET, "/numbers"), getNumbers));

		listen.apply(preMiddleware).apply(endpoints).apply(ImmutableArrayList.empty()).apply(8080);
	}
}

