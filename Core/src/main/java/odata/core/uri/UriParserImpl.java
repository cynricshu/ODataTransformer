package odata.core.uri;

import odata.api.common.InlineCount;
import odata.api.uri.UriInfo;
import odata.api.uri.UriParser;
import odata.api.uri.UriType;
import util.RestUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * User: Cynric
 * Date: 14-4-15
 * Time: 20:05
 */
public class UriParserImpl implements UriParser {

    private static final Pattern INITIAL_SEGMENT_PATTERN = Pattern
            .compile("(?:([^.()]+)\\.)?([^.()]+)(?:\\((.+)\\)|(\\(\\)))?");
    private static final Pattern NAVIGATION_SEGMENT_PATTERN = Pattern.compile("([^()]+)(?:\\((.+)\\)|(\\(\\)))?");
    private static final Pattern NAMED_VALUE_PATTERN = Pattern.compile("(?:([^=]+)=)?([^=]+)");

    private List<String> pathSegments;
    private Map<SystemQueryOption, String> systemQueryOptions;
    private Map<String, String> otherQueryParameters;
    private UriInfoImpl uriInfo;
    private String currentPathSegment;

    @Override
    public UriInfo parse(List<String> pathSegments, String queryString) {
        uriInfo = new UriInfoImpl();

        systemQueryOptions = new HashMap<>();

        handleResourcePath();

        distributeQueryParameters(queryString);
        handleSystemQueryOptions();


        return uriInfo;
    }

    private void handleResourcePath() {
        if (pathSegments.isEmpty()) {
            uriInfo.setUriType(UriType.serviceDocument);
        } else {

            currentPathSegment = pathSegments.remove(0);

            if ("$metadata".equals(currentPathSegment)) {
                ensureLastSegment();
                uriInfo.setUriType(UriType.metadata);

            } else if ("$batch".equals(currentPathSegment)) {
                ensureLastSegment();
                uriInfo.setUriType(UriType.batch);

            } else {
//                handleNormalInitialSegment();
            }
        }
    }

//    private void handleNormalInitialSegment() {
//        final Matcher matcher = INITIAL_SEGMENT_PATTERN.matcher(currentPathSegment);
//        if (!matcher.matches()) {
//        }
//
//        final String entityContainerName = URLDecoder.decode(matcher.group(1));
//        final String segmentName = URLDecoder.decode(matcher.group(2));
//        final String keyPredicate = matcher.group(3);
//        final String emptyParentheses = matcher.group(4);
//
//        final EdmEntityContainer entityContainer =
//                entityContainerName == null ? edm.getDefaultEntityContainer() : edm.getEntityContainer(entityContainerName);
//        uriInfo.setEntityContainer(entityContainer);
//
//        final EdmEntitySet entitySet = entityContainer.getEntitySet(segmentName);
//        if (entitySet != null) {
//            uriInfo.setStartEntitySet(entitySet);
//            handleEntitySet(entitySet, keyPredicate);
//        }
////        else {
////            final EdmFunctionImport functionImport = entityContainer.getFunctionImport(segmentName);
////            if (functionImport == null) {
////                throw new UriNotMatchingException(UriNotMatchingException.NOTFOUND.addContent(segmentName));
////            }
////            uriInfo.setFunctionImport(functionImport);
////            handleFunctionImport(functionImport, emptyParentheses, keyPredicate);
////        }
//    }

    private void ensureLastSegment() {
        if (!pathSegments.isEmpty()) {

        }
    }

    private void distributeQueryParameters(String queryString) {
        Map<String, String> queryParameters = RestUtil.extractQueryParameters(queryString);
        for (final String queryOptionString : queryParameters.keySet()) {
            final String value = queryParameters.get(queryOptionString);
            if (queryOptionString.startsWith("$")) {
                SystemQueryOption queryOption;
                queryOption = SystemQueryOption.valueOf(queryOptionString);
                if ("".equals(value)) {

                } else {
                    systemQueryOptions.put(queryOption, value);
                }
            } else {
                otherQueryParameters.put(queryOptionString, value);
            }
        }
    }

    private void handleSystemQueryOptions() {

        for (SystemQueryOption queryOption : systemQueryOptions.keySet()) {
            switch (queryOption) {
                case $format:
                    uriInfo.setFormat(systemQueryOptions.get(SystemQueryOption.$format));
                    break;
                case $filter:
                    uriInfo.setFilter(systemQueryOptions.get(SystemQueryOption.$filter));
                    break;
                case $inlinecount:
                    String inlineCount = systemQueryOptions.get(SystemQueryOption.$inlinecount);
                    if ("allpages".equals(inlineCount)) {
                        uriInfo.setInlineCount(InlineCount.ALLPAGES);
                    } else if ("none".equals(inlineCount)) {
                        uriInfo.setInlineCount(InlineCount.NONE);
                    }
                    break;
                case $orderby:
                    uriInfo.setOrderBy(systemQueryOptions.get(SystemQueryOption.$orderby));
                    break;
                case $skip:
                    uriInfo.setSkip(Integer.valueOf(systemQueryOptions.get(SystemQueryOption.$skip)));
                    break;
                case $top:
                    uriInfo.setTop(Integer.valueOf(systemQueryOptions.get(SystemQueryOption.$top)));
                    break;
                case $select:
                    ArrayList<String> select = new ArrayList<>();
                    String selectStatement = systemQueryOptions.get(SystemQueryOption.$select);
                    for (String selectItemString : selectStatement.split(",")) {
                        select.add(selectItemString);
                    }
                    uriInfo.setSelect(select);
                    break;
                default:
                    break;
            }
        }
    }
}
