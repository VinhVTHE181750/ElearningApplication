package team2.elearningapplication.config;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SimpleCORSFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {


        HttpServletResponse response = (HttpServletResponse) res;

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "1800");
        response.setHeader("Access-Control-Allow-Headers", "Origin, Authorization, Cache-Control, Pragma, Content-Type, Accept, Accept-Encoding, X-Requested-With, remember-me");
        response.setHeader("Access-Control-Expose-Headers", "Set-Cookie");

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        // Do nothing
    }

    @Override
    public void destroy() {
        // Do nothing
    }

}
