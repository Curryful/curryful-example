package io.github.curryful.example;

import static io.github.curryful.rest.Router.listen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.github.curryful.rest.Destination;
import io.github.curryful.rest.Endpoint;
import io.github.curryful.rest.HttpMethod;
import io.github.curryful.rest.HttpResponse;
import io.github.curryful.rest.HttpResponseCode;
import io.github.curryful.rest.RestFunction;

public class Main {

	private static final List<Integer> NUMBERS = Arrays.asList(5, 2, 10, 13, 1, 4);

	private static final RestFunction sayHello = _context ->
			new HttpResponse<String>(HttpResponseCode.OK, "Hello, world!");

	private static final RestFunction sayHelloName = _context ->
			new HttpResponse<String>(HttpResponseCode.OK, "Hello, " + _context.getPathParameters().get("name").getValue() + "!");

	private static final RestFunction getNumbers = context -> {
		var numbers = new ArrayList<>(NUMBERS);
		var order = context.getQueryParameters().get("order");

		if (order.hasValue()) {
			Collections.sort(numbers);

			if (order.getValue().equals("descending")) {
				Collections.reverse(numbers);
			}
		}

		var body = numbers.stream().map(i -> i.toString()).collect(Collectors.joining(", "));
		return new HttpResponse<String>(HttpResponseCode.OK, body);
	};

	public static void main(String[] args) {
		var endpoints = new ArrayList<Endpoint>();
		endpoints.add(Endpoint.of(new Destination(HttpMethod.GET, "/hello"), sayHello));
		endpoints.add(Endpoint.of(new Destination(HttpMethod.GET, "/hello/:name"), sayHelloName));
		endpoints.add(Endpoint.of(new Destination(HttpMethod.GET, "/numbers"), getNumbers));

		listen.apply(endpoints).apply(8080);
	}
}
