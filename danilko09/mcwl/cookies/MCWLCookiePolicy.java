package danilko09.mcwl.cookies;

import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;

/**
 * Определяет политику приема cookies. По умолчанию разрешено сохранять cookies
 * только для своего домена
 *
 * @author danilko09
 */
public class MCWLCookiePolicy implements CookiePolicy {

    @Override
    public boolean shouldAccept(URI uri, HttpCookie cookie) {
        return CookiePolicy.ACCEPT_ORIGINAL_SERVER.shouldAccept(uri, cookie);
    }

}
