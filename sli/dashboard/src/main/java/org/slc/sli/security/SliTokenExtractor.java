package org.slc.sli.security;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.model.Token;
import org.scribe.utils.Preconditions;

/**
 * Class to get the token from the authorize response.
 * @author jnanney
 *
 */
public class SliTokenExtractor  implements AccessTokenExtractor {
    private static final String TOKEN_REGEX = "\"access_token\":\"([^&\"]+)\"";

    @Override
    public Token extract(String response) {
        Preconditions.checkEmptyString(response, "Response body is incorrect. Can't extract a token from an empty string");
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(response).getAsJsonObject();
        return new Token(json.get("access_token").getAsString(), "", response);

//        Matcher matcher = Pattern.compile(TOKEN_REGEX).matcher(response);
//        if (matcher.find()) {
//          String token = OAuthEncoder.decode(matcher.group(1));
//          return new Token(token, "", response);
//        } else {
//          throw new OAuthException("Response body is incorrect. Can't extract a token from this: '" + response + "'", null);
//        }
    }
    
}
