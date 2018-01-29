package boot.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessFilter implements Filter {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String hash;
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName() + " " + cookie.getValue());
                if (cookie.getName().equals("SessionId")) {
                    hash = cookie.getValue();
                    boolean access = restTemplate.getForObject("http://localhost:8081/access?token="+hash.trim(), Boolean.class);
                    if (access){
                        filterChain.doFilter(servletRequest, servletResponse);
                    }
                }
            }
        }
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.sendRedirect("http://localhost:8081/");

    }

    @Override
    public void destroy() {

    }
}
