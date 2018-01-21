package gen;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class PostGen {
	String quotes = "";

	public JSONArray toObj(String quotes) {
		return new JSONArray(quotes);
	}

	public void createPost(int postId, JSONObject quote) throws IOException {

		TimeZone tz = TimeZone.getTimeZone("UTC+2");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+02:00'"); // Quoted "Z" to indicate UTC, no timezone offset
		df.setTimeZone(tz);

		ArrayList<String> lines = new ArrayList<String>() {
			{
				add("---");
				add("title: \"Therapy " + postId + "\"");
				add("postId: \"therapy_" + postId + "\"");
				add("description: \"" + StringEscapeUtils.escapeJava(quote.get("q").toString()) + "\"");
				add("date: " + df.format(new Date(quote.getLong("published"))));
				add("draft: false");
				add("---");
			}
		};
		Path file = Paths.get("post", "therapy-" + postId + ".md");
		Files.write(file, lines, Charset.forName("UTF-8"));
	}

	public void create(String quotes) throws IOException {
		JSONArray quoteArr = toObj(quotes);
		int index = quoteArr.length();
		for (Object quote : quoteArr) {
			createPost(index, (JSONObject) quote);
			index--;
		}
	}

	public static void main(String[] arg) throws IOException {
		PostGen postGen = new PostGen();
		postGen.create(postGen.quotes);
	}

}
