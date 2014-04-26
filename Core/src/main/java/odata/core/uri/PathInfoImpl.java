package odata.core.uri;


import odata.api.uri.PathInfo;

import java.net.URI;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class PathInfoImpl implements PathInfo {

    private List<String> precedingPathSegment = Collections.emptyList();
    private List<String> odataPathSegment = Collections.emptyList();
    private URI serviceRoot;
    private URI requestUri;

    public void setODataPathSegment(final List<String> odataPathSegement) {
        odataPathSegment = odataPathSegement;
    }

    public void setPrecedingPathSegment(final List<String> precedingPathSegement) {
        precedingPathSegment = precedingPathSegement;
    }

    public void setServiceRoot(final URI uri) {
        serviceRoot = uri;
    }

    @Override
    public List<String> getPrecedingSegments() {
        return Collections.unmodifiableList(precedingPathSegment);
    }

    @Override
    public List<String> getODataSegments() {
        return Collections.unmodifiableList(odataPathSegment);
    }

    @Override
    public URI getServiceRoot() {
        return serviceRoot;
    }

    @Override
    public URI getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(final URI requestUri) {
        this.requestUri = requestUri;
    }
}
