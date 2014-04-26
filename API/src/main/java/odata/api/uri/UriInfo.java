package odata.api.uri;

import odata.api.common.InlineCount;

import java.util.List;

public interface UriInfo {

    public UriType getUriType();

    public PathInfo getPathInfo();

    /**
     * Determines whether $count has been used in the request URI.
     *
     * @return whether $count has been used
     */
    public boolean isCount();

    /**
     * Determines whether $value has been used in the request URI.
     *
     * @return whether $value has been used
     */
    public boolean isValue();

    /**
     * Determines whether $links has been used in the request URI.
     *
     * @return whether $links has been used
     */
    public boolean isLinks();

    /**
     * Gets the value of the $format system query option.
     *
     * @return the format (as set as <code>$format</code> query parameter) or null
     */
    public String getFormat();

    public String getFilter();

    /**
     * Gets the value of the $skip system query option.
     *
     * @return skip or null
     */
    public Integer getSkip();

    /**
     * Gets the value of the $top system query option.
     *
     * @return top or null
     */
    public Integer getTop();


    public List<String> getSelect();

    public InlineCount getInlineCount();


}
