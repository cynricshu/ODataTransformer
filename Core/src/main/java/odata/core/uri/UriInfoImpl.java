package odata.core.uri;


import odata.api.common.InlineCount;
import odata.api.uri.PathInfo;
import odata.api.uri.UriInfo;
import odata.api.uri.UriType;

import java.util.Collections;
import java.util.List;

/**
 * User: Cynric
 * Date: 14-4-15
 * Time: 19:40
 */
public class UriInfoImpl implements UriInfo {

    private UriType uriType;
    private boolean count;
    private boolean value;
    private boolean links;
    private PathInfo pathInfo;

    public UriType getUriType() {
        return uriType;
    }

    public void setUriType(UriType uriType) {
        this.uriType = uriType;
    }

    public void setPathInfo(PathInfo pathInfo) {
        this.pathInfo = pathInfo;
    }

    private InlineCount inlineCount;

    public InlineCount getInlineCount() {
        return inlineCount;
    }

    public void setInlineCount(InlineCount inlineCount) {
        this.inlineCount = inlineCount;
    }

    private String format;
    private String filter;
    private String orderBy;
    private Integer skip;
    private Integer top;
    private List<String> select = Collections.emptyList();

    public void setCount(boolean count) {
        this.count = count;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public void setLinks(boolean links) {
        this.links = links;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }


    public void setSkip(Integer skip) {
        this.skip = skip;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public void setSelect(List<String> select) {
        this.select = select;
    }

    @Override
    public PathInfo getPathInfo() {
        return pathInfo;
    }

    @Override
    public boolean isCount() {
        return count;
    }

    @Override
    public boolean isValue() {
        return value;
    }

    @Override
    public boolean isLinks() {
        return links;
    }

    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public String getFilter() {
        return filter;
    }

    @Override
    public Integer getSkip() {
        return skip;
    }

    @Override
    public Integer getTop() {
        return top;
    }

    @Override
    public List<String> getSelect() {
        return select;
    }
}
