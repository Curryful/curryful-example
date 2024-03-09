package io.github.curryful.example;

import static java.util.Collections.unmodifiableList;
import static java.util.List.of;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.github.curryful.rest.HttpResponse;
import io.github.curryful.rest.HttpResponseCode;
import io.github.curryful.rest.RestFunction;

public class Numbers {

	private static final List<Integer> NUMBERS = unmodifiableList(of(5, 2, 10, 13, 1, 4));

	public static final RestFunction getNumbers = context -> {
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
}

