package io.github.curryful.example;

import static io.github.curryful.rest.http.HttpContentType.TEXT_PLAIN;
import static io.github.curryful.rest.http.HttpResponseCode.OK;
import static io.github.curryful.rest.http.HttpResponseCode.UNAUTHORIZED;

import io.github.curryful.rest.RestFunction;
import io.github.curryful.rest.http.HttpResponse;

public class Hello {

	public static final RestFunction sayHello = _context ->
			HttpResponse.of(OK, "Hello, world!", TEXT_PLAIN);

	public static final RestFunction sayHelloName = _context ->
			HttpResponse.of(OK, "Hello, " + _context.getPathParameters().get("name").getValue() + "!" , TEXT_PLAIN);

	public static final RestFunction secureHello = context -> {
		var username = context.getHeaders().get("X-Authenticated-User");

		if (!username.hasValue()) {
			return HttpResponse.of(UNAUTHORIZED);
		}

		return HttpResponse.of(OK, "Hello, " + username.getValue() + "!", TEXT_PLAIN);
	};
}

