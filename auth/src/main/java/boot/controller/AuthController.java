package boot.controller;

import boot.repository.User;
import boot.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by DobryninAM on 22.09.2017.
 */
@RestController
public class AuthController {

    @Autowired
    private UserRepo userRepo;
    private Map<Integer, String> loggedUsers = new ConcurrentHashMap<>();

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    private String hello() {
        return "hello world";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET, params = {"login", "email", "pass", "passconfirm"})
    private String register(@RequestParam("login")          String login,
                            @RequestParam("email")          String email,
                            @RequestParam("pass")           String pass,
                            @RequestParam("passconfirm")    String passconfirm,
                            HttpServletResponse response) {
        if (userRepo.findByLogin(login) == null) {
            if (pass.equals(passconfirm)) {
                User user = new User(login, pass, email);
                userRepo.save(user);
                int hash = login.hashCode() % 100_000_000 + pass.hashCode() % 7482 + user.getEmail().hashCode() % 100_000_000;
                Cookie cookie = new Cookie("SessionId", String.valueOf(hash));
                response.addCookie(cookie);
                loggedUsers.put(hash, user.getLogin());
                return "success: user " + login + " has created";
            }
        }
        return "failure";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET, params = {"login", "pass"})
    private String login(@RequestParam("login")     String login,
                         @RequestParam("pass")      String password,
                         HttpServletResponse response) {
        User user = userRepo.findByLogin(login);
        if (user == null) {
            return "faliure: user not found";
        } else if (user.getPassword().equals(password)) {
            int hash = login.hashCode() % 100_000_000 + password.hashCode() % 7482 + user.getEmail().hashCode() % 100_000_000;
            Cookie cookie = new Cookie("SessionId", String.valueOf(hash));
            response.addCookie(cookie);
            loggedUsers.put(hash, user.getLogin());
            return "success: user "+ login + " has logged in";
        }
        return "failure";
    }

    @RequestMapping(value = "/access", method = RequestMethod.GET, params = {"token"})
    private boolean access(@RequestParam("token") String token) {
        return loggedUsers.containsKey(Integer.parseInt(token));
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    private String logout(HttpServletResponse response, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("SessionId")) {
                    loggedUsers.remove(Integer.parseInt(cookie.getValue()));
                }
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        return "logged out";
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET, params = {"token"})
    private @ResponseBody User getCurrentUser(@RequestParam("token") String token){
        return userRepo.findByLogin(loggedUsers.get(Integer.parseInt(token)));
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    private String mainPage() {
        return "please login or register";
    }
}
