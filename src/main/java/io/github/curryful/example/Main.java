package io.github.curryful.example;

import static io.github.curryful.rest.Router.listen;

import java.util.Arrays;
import java.util.List;

import io.github.curryful.rest.Destination;
import io.github.curryful.rest.Endpoint;
import io.github.curryful.rest.HttpMethod;
import io.github.curryful.rest.HttpResponse;
import io.github.curryful.rest.HttpResponseCode;

public class Main {

	public static void main(String[] args) {
		List<Endpoint> endpoints = Arrays.asList(
			Endpoint.of(new Destination(HttpMethod.GET, "/hello"), _context -> new HttpResponse<String>(HttpResponseCode.OK, "Hello, world!")),
			Endpoint.of(new Destination(HttpMethod.GET, "/hello/:name"), _context -> new HttpResponse<String>(HttpResponseCode.OK, "Hello, " + _context.getPathParameters().get("name") + "!"))
		);

		listen.apply(endpoints).apply(8080);
	}
}

