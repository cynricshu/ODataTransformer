import odata.api.uri.PathInfo;
import util.RestUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: Cynric
 * Date: 14-4-14
 * Time: 20:18
 */
public class ODataServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String httpMethod = req.getMethod();

        PathInfo pathInfo = RestUtil.buildODataPathInfo(req, 0);

        System.out.println(req.getContextPath());
        System.out.println(req.getServletPath());

        System.out.println("over");
    }


}
