package util;

import odata.api.uri.PathInfo;
import odata.core.uri.PathInfoImpl;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RestUtil {
    private static final String REG_EX_OPTIONAL_WHITESPACE = "\\s?";

    // RFC 2616, 3.9: qvalue = ("0"["." 0*3DIGIT]) | ("1"["." 0*3("0")])
    private static final String REG_EX_QVALUE = "q=((?:1(?:\\.0{0,3})?)|(?:0(?:\\.[0-9]{0,3})?))";

    // RFC 2616, 14.1: the media-range parameters
    private static final String REG_EX_PARAMETER = "(?:;\\s*(?:(?:[^qQ].*)|(?:[qQ]\\s*=\\s*(?:[^01].*))))*";
    private static final Pattern REG_EX_ACCEPT =
            Pattern.compile("([a-z\\*\\s]+/[a-zA-Z\\+\\*\\-=\\s]+" + REG_EX_PARAMETER + ")");
    private static final Pattern REG_EX_ACCEPT_WITH_Q_FACTOR =
            Pattern.compile(REG_EX_ACCEPT + "(?:" + REG_EX_OPTIONAL_WHITESPACE + REG_EX_QVALUE + ")?");

    // RFC 2616, 14.4: language-range = ((1*8ALPHA *("-" 1*8ALPHA)) | "*")
    private static final Pattern REG_EX_ACCEPT_LANGUAGES =
            Pattern.compile("((?:\\*)|(?:[a-z]{1,8}(?:\\-[a-zA-Z]{1,8})?))");
    private static final Pattern REG_EX_ACCEPT_LANGUAGES_WITH_Q_FACTOR =
            Pattern.compile(REG_EX_ACCEPT_LANGUAGES + "(?:;" + REG_EX_OPTIONAL_WHITESPACE + REG_EX_QVALUE + ")?");

    private static final Pattern REG_EX_MATRIX_PARAMETER = Pattern.compile("([^=]*)(?:=(.*))?");


    /*
     * Parses query parameters.
     */
    public static Map<String, String> extractQueryParameters(final String queryString) {
        Map<String, String> queryParametersMap = new HashMap<String, String>();
        if (queryString != null) {
            // At first the queryString will be decoded.
            List<String> queryParameters = null;
            try {
                queryParameters = Arrays.asList(URLDecoder.decode(queryString, "UTF-8").split("\\&"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            for (String param : queryParameters) {
                int indexOfEqualSign = param.indexOf("=");
                if (indexOfEqualSign < 0) {
                    queryParametersMap.put(param, "");
                } else {
                    queryParametersMap.put(param.substring(0, indexOfEqualSign), param.substring(indexOfEqualSign + 1));
                }
            }
        }
        return queryParametersMap;
    }

    /*
     * Parses Accept-Language header. Returns a list sorted by quality parameter
     */
    public static List<Locale> extractAcceptableLanguage(final String acceptableLanguageHeader) {
        List<Locale> acceptLanguages = new ArrayList<Locale>();
        TreeSet<Accept> acceptTree = getAcceptTree();
        if (acceptableLanguageHeader != null && !acceptableLanguageHeader.isEmpty()) {
            List<String> list = Arrays.asList(acceptableLanguageHeader.split(",\\s?"));
            for (String acceptLanguage : list) {
                Matcher matcher = REG_EX_ACCEPT_LANGUAGES_WITH_Q_FACTOR.matcher(acceptLanguage);
                if (matcher.find()) {
                    String language = matcher.group(1);
                    double qualityFactor = matcher.group(2) != null ? Double.parseDouble(matcher.group(2)) : 1d;
                    acceptTree.add(new Accept(language, qualityFactor));
                }
            }
        }
        for (Accept accept : acceptTree) {
            String languageRange = accept.getValue();
            // The languageRange has to be splitted in language tag and country tag
            int indexOfMinus = languageRange.indexOf("-");
            Locale locale;
            if (indexOfMinus < 0) {
                // no country tag
                locale = new Locale(languageRange);
            } else {
                String language = languageRange.substring(0, indexOfMinus);
                String country = languageRange.substring(indexOfMinus + 1);
                locale = new Locale(language, country);
            }
            acceptLanguages.add(locale);
        }
        return acceptLanguages;
    }

    /*
     * Parses Accept header. Returns a list of media ranges sorted by quality parameter
     */
    public static List<String> extractAcceptHeaders(final String acceptHeader) {
        TreeSet<Accept> acceptTree = getAcceptTree();
        List<String> acceptHeaders = new ArrayList<String>();
        if (acceptHeader != null && !acceptHeader.isEmpty()) {
            List<String> list = Arrays.asList(acceptHeader.split(",\\s?"));
            for (String accept : list) {
                Matcher matcher = REG_EX_ACCEPT_WITH_Q_FACTOR.matcher(accept);
                if (matcher.find()) {
                    String headerValue = matcher.group(1);
                    double qualityFactor = matcher.group(2) != null ? Double.parseDouble(matcher.group(2)) : 1d;
                    acceptTree.add(new Accept(headerValue, qualityFactor));
                }
            }
        }
        for (Accept accept : acceptTree) {
            acceptHeaders.add(accept.getValue());
        }
        return acceptHeaders;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, List<String>> extractHeaders(final HttpServletRequest req) {
        Map<String, List<String>> requestHeaders = new HashMap<String, List<String>>();
        for (Enumeration<String> headerNames = req.getHeaderNames(); headerNames.hasMoreElements(); ) {
            String headerName = headerNames.nextElement();
            List<String> headerValues = new ArrayList<String>();
            for (Enumeration<String> headers = req.getHeaders(headerName); headers.hasMoreElements(); ) {
                String value = headers.nextElement();
                headerValues.add(value);
            }
            if (requestHeaders.containsKey(headerName)) {
                requestHeaders.get(headerName).addAll(headerValues);
            } else {
                requestHeaders.put(headerName, headerValues);
            }
        }
        return requestHeaders;
    }

    public static PathInfo buildODataPathInfo(final HttpServletRequest req, final int pathSplit) {
        PathInfoImpl pathInfo = splitPath(req, pathSplit);

        pathInfo.setServiceRoot(buildBaseUri(req, pathInfo.getPrecedingSegments()));
        pathInfo.setRequestUri(buildRequestUri(req));
        return pathInfo;
    }

    private static URI buildBaseUri(final HttpServletRequest req, final List<String> precedingPathSegments) {
        try {
            URI baseUri;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(req.getContextPath()).append(req.getServletPath());
            for (final String ps : precedingPathSegments) {
                stringBuilder.append("/").append(ps);
            }

            String path = stringBuilder.toString();
            if (!path.endsWith("/")) {
                path = path + "/";
            }
            baseUri = new URI(req.getScheme(), null, req.getServerName(), req.getServerPort(), path, null, null);
            return baseUri;
        } catch (final URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static URI buildRequestUri(final HttpServletRequest servletRequest) {
        URI requestUri;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(servletRequest.getRequestURL());
        String queryString = servletRequest.getQueryString();

        if (queryString != null) {
            stringBuilder.append("?").append(queryString);
        }

        String requestUriString = stringBuilder.toString();
        requestUri = URI.create(requestUriString);
        return requestUri;
    }


    private static PathInfoImpl splitPath(final HttpServletRequest servletRequest, final int pathSplit) {
        PathInfoImpl pathInfo = new PathInfoImpl();
        List<String> precedingPathSegments;
        List<String> pathSegments;

        String pathInfoString = extractPathInfo(servletRequest);
        while (pathInfoString.startsWith("/")) {
            pathInfoString = pathInfoString.substring(1);
        }
        List<String> segments = Arrays.asList(pathInfoString.split("/", -1));

        if (pathSplit == 0) {
            precedingPathSegments = Collections.emptyList();
            pathSegments = segments;
        } else {
//            if (segments.size() < pathSplit) {
//                throw new ODataBadRequestException(ODataBadRequestException.URLTOOSHORT);
//            }

            precedingPathSegments = segments.subList(0, pathSplit);
            final int pathSegmentCount = segments.size();
            pathSegments = segments.subList(pathSplit, pathSegmentCount);
        }

        // Percent-decode only the preceding path segments.
        // The OData path segments are decoded during URI parsing.
        pathInfo.setPrecedingPathSegment(convertPathSegmentList(precedingPathSegments));

        List<String> odataSegments = new ArrayList<String>();
        for (final String segment : pathSegments) {

            int index = segment.indexOf(";");
            if (index < 0) {
                odataSegments.add(segment);
            } else {
//                String path = segment.substring(0, index);
//                Map<String, List<String>> parameterMap = extractMatrixParameter(segment, index);
//                throw new ODataNotFoundException(ODataNotFoundException.MATRIX.addContent(parameterMap.keySet(), path));
            }
        }
        pathInfo.setODataPathSegment(odataSegments);
        return pathInfo;
    }

    private static List<String> convertPathSegmentList(final List<String> pathSegments) {
        ArrayList<String> converted = new ArrayList<String>();
        for (final String segment : pathSegments) {
            int index = segment.indexOf(";");
            if (index == -1) {
                converted.add(URLDecoder.decode(segment));
            } else {
                String path = segment.substring(0, index);
                Map<String, List<String>> parameterMap = extractMatrixParameter(segment, index);
                converted.add(URLDecoder.decode(path));
            }
        }
        return converted;
    }

    private static Map<String, List<String>> extractMatrixParameter(final String segment, final int index) {
        List<String> matrixParameters = Arrays.asList(segment.substring(index + 1).split(";"));
        String matrixParameterName = "";
        String matrixParamaterValues = "";
        Map<String, List<String>> parameterMap = new HashMap<String, List<String>>();

        for (String matrixParameter : matrixParameters) {
            List<String> values = Arrays.asList("");
            Matcher matcher = REG_EX_MATRIX_PARAMETER.matcher(matrixParameter);
            if (matcher.find()) {
                matrixParameterName = matcher.group(1);
                matrixParamaterValues = matcher.group(2);
            }
            if (matrixParamaterValues != null) {
                values = Arrays.asList(matrixParamaterValues.split(","));
            }
            parameterMap.put(matrixParameterName, values);
        }
        return parameterMap;
    }

    private static String extractPathInfo(final HttpServletRequest servletRequest) {
        String pathInfoString;
        final String requestUri = servletRequest.getRequestURI();
        pathInfoString = requestUri;
        int index = requestUri.indexOf(servletRequest.getContextPath());

        if (index >= 0) {
            pathInfoString = pathInfoString.substring(servletRequest.getContextPath().length());
        }

        int indexServletPath = requestUri.indexOf(servletRequest.getServletPath());
        if (indexServletPath >= 0) {
            pathInfoString = pathInfoString.substring(servletRequest.getServletPath().length());
        }
        return pathInfoString;
    }

    private static TreeSet<Accept> getAcceptTree() {
        TreeSet<Accept> treeSet = new TreeSet<Accept>(new Comparator<Accept>() {
            @Override
            public int compare(final Accept header1, final Accept header2) {
                if (header1.getQuality() <= header2.getQuality()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        return treeSet;
    }

    /*
     * The class is used in order to sort headers by "q" parameter.
     * The object of this class contains a value of the Accept header or Accept-Language header and value of the
     * quality parameter.
     */
    private static class Accept {
        private double quality;
        private String value;

        public Accept(final String headerValue, final double qualityFactor) {
            value = headerValue;
            quality = qualityFactor;
        }

        public String getValue() {
            return value;
        }

        public double getQuality() {
            return quality;
        }

    }

}
