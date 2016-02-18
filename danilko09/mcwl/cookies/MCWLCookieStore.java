package danilko09.mcwl.cookies;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import danilko09.mcwl.util.Files;
import danilko09.mcwl.util.JSON;
import java.io.File;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Осуществляет хранение Cookies на диске
 *
 * @author danilko09
 */
public class MCWLCookieStore implements CookieStore, Runnable {

    CookieStore store;

    public MCWLCookieStore() {
        // get the default in memory cookie store
        store = new CookieManager().getCookieStore();

        JsonArray arr = new JsonArray();
        if((new File("cookies.json")).exists()){
            arr = (JsonArray) JSON.loadJSON(Files.getFile("cookies.json"));
        }
        
        for (int i = 0; i < arr.size(); i++) {
            JsonObject o = (JsonObject) arr.get(i);
 
            UriAndCookie uriAndCookie = jsonToCookie(o);
            add(uriAndCookie.getUri(), uriAndCookie.getCookie());
 
        }
        
        // add a shutdown hook to write out the in memory cookies
        Runtime.getRuntime().addShutdownHook(new Thread(this));
    }

    @Override
    public void run() {
        List<HttpCookie> cookies = getCookies();
        List<URI> urIs = getURIs();

        JsonArray arr = new JsonArray();

        for (int i = 0; i < cookies.size(); i++) {
            URI uri = null;

            for (int j = 0; j < urIs.size(); j++) {
                if (urIs.get(j).toString().contains(cookies.get(i).getDomain().substring(1))) {
                    uri = urIs.get(j);
                }
            }

            JsonObject o = cookieToJson(new UriAndCookie(uri, cookies.get(i)));

            arr.add(o);
        }

        JSON.saveJSON(Files.getFile("cookies.json"), arr);
        
    }

    @Override
    public final void add(URI uri, HttpCookie cookie) {
        store.add(uri, cookie);
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        return store.get(uri);
    }

    @Override
    public List<HttpCookie> getCookies() {
        return store.getCookies();
    }

    @Override
    public List<URI> getURIs() {
        return store.getURIs();
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        return store.remove(uri, cookie);
    }

    @Override
    public boolean removeAll() {
        return store.removeAll();
    }

    private JsonObject cookieToJson(UriAndCookie object) {
 
        HttpCookie cookie = object.getCookie();
        JsonObject obj = new JsonObject();
        
        obj.add("name", new JsonPrimitive(cookie.getName()));
        obj.add("value", new JsonPrimitive(cookie.getValue()));
        
        if(cookie.getComment() != null){obj.add("comment", new JsonPrimitive(cookie.getComment()));}
        if(cookie.getCommentURL() != null){obj.add("comment_url", new JsonPrimitive(cookie.getCommentURL()));}
        
        obj.add("discard", new JsonPrimitive(cookie.getDiscard()));
        obj.add("max_age", new JsonPrimitive(cookie.getMaxAge()));
        
        if(cookie.getDomain() != null){obj.add("domain", new JsonPrimitive(cookie.getDomain()));}
        if(cookie.getPortlist() != null){obj.add("port_list", new JsonPrimitive(cookie.getPortlist()));}
        if(cookie.getPath() != null){obj.add("path", new JsonPrimitive(cookie.getPath()));}
        if(object.getUri() != null){obj.add("uri", new JsonPrimitive(object.getUri().toString()));}
        obj.add("secure", new JsonPrimitive(cookie.getSecure()));
        
        obj.add("version", new JsonPrimitive(cookie.getVersion()));
 
        return obj;
 
    }
 
    private UriAndCookie jsonToCookie(JsonObject o) {
        HttpCookie cookie = new HttpCookie((String) o.get("name").getAsString(), (String) o.get("value").getAsString());
 
        JsonElement comment = o.get("comment");
        if( comment != null) cookie.setComment(comment.getAsString());
 
        JsonElement commentUrl = o.get("comment_url");
        if( commentUrl != null) cookie.setCommentURL(commentUrl.getAsString());
 
        JsonElement discard = o.get("discard");
        if( discard != null) cookie.setDiscard(discard.getAsBoolean());
 
 
        JsonElement domain = o.get("domain");
        if( domain != null) cookie.setDomain(domain.getAsString());
 
        JsonElement maxAge = o.get("max_age");
        if( maxAge != null) cookie.setMaxAge(maxAge.getAsLong());
 
 
        JsonElement path = o.get("path");
        if( path != null) cookie.setPath(path.getAsString());
 
        JsonElement portList = o.get("port_list");
        if( portList != null) cookie.setPortlist(portList.getAsString());
 
 
        JsonElement secure = o.get("secure");
        if( secure != null) cookie.setSecure(secure.getAsBoolean());
 
        JsonElement version = o.get("version");
        if( version != null) cookie.setVersion(version.getAsInt());
 
 
 
        try {
            URI uri = new URI((String) o.get("uri").getAsString());
            return new UriAndCookie(uri, cookie);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
 
        return null;
 
    }

    
    private static class UriAndCookie {

        private final URI uri;
        private final HttpCookie cookie;

        public UriAndCookie(URI uri, HttpCookie cookie) {
            this.uri = uri;
            this.cookie = cookie;
        }

        public URI getUri() {
            return uri;
        }

        public HttpCookie getCookie() {
            return cookie;
        }
    }

}
