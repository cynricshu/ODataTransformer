package odata.api.uri;

import java.util.List;

/**
 * Wrapper for UriParser functionality.
 */
public interface UriParser {
    /**
     * Parses path segments and query parameters.
     *
     * @return {@link UriInfo} information about the parsed URI
     */
    public UriInfo parse(List<String> pathSegments, String queryString);


}
