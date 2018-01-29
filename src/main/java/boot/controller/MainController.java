package boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by DobryninAM on 22.09.2017.
 */
@RestController
public class MainController {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    private String register(HttpServletRequest request, HttpServletResponse response) {
        return redirect(response, request, "http://localhost:8081/register");
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    private String login(HttpServletRequest request, HttpServletResponse response) {
        return redirect(response, request, "http://localhost:8081/login");
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    private String checkLogin(HttpServletRequest request, HttpServletResponse response) {
        return redirect(response, request, "http://localhost:8082/");
    }

    private String redirect(HttpServletResponse response, HttpServletRequest request, String url) {
        HttpHeaders requestHeaders = new HttpHeaders();
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
            for (Cookie cookie : cookies) {
                requestHeaders.add("Cookie", cookie.getName() + "=" + cookie.getValue());
            }
        HttpEntity requestEntity = new HttpEntity(null, requestHeaders);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        for (String s : request.getParameterMap().keySet()) {
            builder.queryParam(s, request.getParameter(s));
        }
        ResponseEntity newResponse = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                requestEntity,
                String.class);


        if (newResponse.getHeaders().get("Set-Cookie") != null) {
            for (String s1 : newResponse.getHeaders().get("Set-Cookie")) {
                String[] cookie = s1.split("=");
                response.addCookie(new Cookie(cookie[0], cookie[1]));
            }
        }


        String rss = String.valueOf(newResponse.getBody());
        return rss;
    }

}
