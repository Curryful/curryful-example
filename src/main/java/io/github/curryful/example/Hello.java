package io.github.curryful.example;

import io.github.curryful.rest.HttpResponse;
import io.github.curryful.rest.HttpResponseCode;
import io.github.curryful.rest.RestFunction;

public class Hello {

	public static final RestFunction sayHello = _context ->
			new HttpResponse<String>(HttpResponseCode.OK, "Hello, world!");

	public static final RestFunction sayHelloName = _context ->
			new HttpResponse<String>(HttpResponseCode.OK,
					"Hello, " + _context.getPathParameters().get("name").getValue() + "!");

	public static final RestFunction secureHello = context -> {
		var username = context.getHeaders().get("X-Authenticated-User");

		if (!username.hasValue()) {
			return new HttpResponse<String>(HttpResponseCode.UNAUTHORIZED);
		}

		return new HttpResponse<String>(HttpResponseCode.OK, "Hello, " + username.getValue() + "!");
	};
}

