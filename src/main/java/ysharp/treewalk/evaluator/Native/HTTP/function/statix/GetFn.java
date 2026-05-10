package ysharp.treewalk.evaluator.Native.HTTP.function.statix;

import ysharp.treewalk.YsharpException;
import ysharp.treewalk.evaluator.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class GetFn extends Function.NativeFunction implements Callable {

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public Variable.Variant call(Interpreter interpreter,
                                 List<Variable.Variant> arguments)
            throws YsharpException {

        requireArity(arguments, arity(), getFnName());

        Variable.Variant urlValue = arguments.getFirst();

        if (!(urlValue.value instanceof String url)) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "HTTP.get(url) expects url to be string."
            );
        }

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            return new Variable.Variant(new yString.yStringInstance(response.body()));

        } catch (IllegalArgumentException e) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "HTTP.get failed: invalid URL '" + url + "'."
            );

        } catch (Exception e) {
            throw new YsharpException(
                    YsharpException.YsharpErrorType.PROCESS,
                    -1,
                    "HTTP.get failed: " + e.getMessage()
            );
        }
    }

    @Override
    public String getFnName() {
        return "get";
    }
}