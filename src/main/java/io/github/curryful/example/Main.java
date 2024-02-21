package io.github.curryful.example;

import static io.github.curryful.rest.Router.listen;

import java.util.HashMap;

import io.github.curryful.rest.HttpMethod;
import io.github.curryful.rest.Destination;
import io.github.curryful.rest.Endpoint;
import io.github.curryful.rest.HttpResponse;
import io.github.curryful.rest.HttpResponseCode;

// void main() {
//     var routes = new HashMap<Destination, Endpoint>();
//     routes.put(new Destination(HttpMethod.GET, "/hello"), _context -> new HttpResponse<String>(HttpResponseCode.OK, "Hello, world!"));

//     listen.apply(routes).apply(8080);
// }

public class Main {

	public static void main(String[] args) {
		var routes = new HashMap<Destination, Endpoint>();
		routes.put(new Destination(HttpMethod.GET, "/hello"), _context -> new HttpResponse<String>(HttpResponseCode.OK, "Hello, world!"));
		routes.put(new Destination(HttpMethod.GET, "/hello/:name"), _context -> new HttpResponse<String>(HttpResponseCode.OK, "Hello, " + _context.get("name") + "!"));
		listen.apply(routes).apply(8080);
	}
}

