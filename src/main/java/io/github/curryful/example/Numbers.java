package io.github.curryful.example;

import static io.github.curryful.rest.http.HttpContentType.TEXT_PLAIN;
import static io.github.curryful.rest.http.HttpResponseCode.OK;

import java.util.Comparator;
import java.util.stream.Collectors;

import io.github.curryful.commons.collections.ImmutableArrayList;
import io.github.curryful.commons.collections.MutableArrayList;
import io.github.curryful.rest.RestFunction;
import io.github.curryful.rest.http.HttpResponse;

public class Numbers {

	private static final ImmutableArrayList<Integer> NUMBERS;

	static {
		MutableArrayList<Integer> numbers = MutableArrayList.empty();
		numbers.add(3);
		numbers.add(1);
		numbers.add(4);
		numbers.add(1);
		numbers.add(5);
		numbers.add(9);
		numbers.add(2);
		NUMBERS = numbers;
	}

	public static final RestFunction getNumbers = context -> {
		var numbers = MutableArrayList.of(NUMBERS);
		var order = context.getQueryParameters().get("order");

		Comparator<Integer> comparator = order.hasValue() && order.getValue().equals("ascending") ?
				Integer::compare : (a, b) -> Integer.compare(b, a);
		numbers = numbers.stream().sorted(comparator)
				.collect(MutableArrayList::empty, MutableArrayList::add, MutableArrayList::addAll);

		var body = numbers.stream().map(i -> i.toString()).collect(Collectors.joining(", "));
		return HttpResponse.of(OK, body, TEXT_PLAIN);
	};
}

