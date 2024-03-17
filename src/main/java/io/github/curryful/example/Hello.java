package io.github.curryful.example;

import static io.github.curryful.rest.HttpResponseCode.OK;
import static io.github.curryful.rest.HttpResponseCode.UNAUTHORIZED;

import io.github.curryful.rest.HttpResponse;
import io.github.curryful.rest.RestFunction;

public class Hello {

	public static final RestFunction sayHello = _context ->
			HttpResponse.of(OK, "Hello, world!");

	public static final RestFunction sayHelloName = _context ->
			HttpResponse.of(OK, "Hello, " + _context.getPathParameters().get("name").getValue() + "!");

	public static final RestFunction secureHello = context -> {
		var username = context.getHeaders().get("X-Authenticated-User");

		if (!username.hasValue()) {
			return HttpResponse.of(UNAUTHORIZED);
		}

		return HttpResponse.of(OK, "Hello, " + username.getValue() + "!");
	};
}

