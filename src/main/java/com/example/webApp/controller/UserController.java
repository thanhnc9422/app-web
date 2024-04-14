package com.example.webApp.controller;

import com.example.webApp.models.Conversation;
import com.example.webApp.models.JwtRequest;
import com.example.webApp.models.User;
import com.example.webApp.util.Crypto;
import com.example.webApp.util.JwtTokenUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import repositories.ConversationRepository;
import repositories.UserRepository;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/user")
@RestController
public class UserController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ConversationRepository conversationRepository;
    @Autowired
    Crypto crypto;
    @Autowired
    User user;

    @GetMapping("/test1")
    public ResponseEntity test1() {
        JSONObject jsonObject = new JSONObject();
        User user = new User();
        for (int i = 0; i < 1000000; i++) {
            jsonObject.clear();
            user.clear();
            user.setUserName("abcdefgh");
            user.setId("abcdefgh");
            user.setPassword("abcdefgh");
            user.setRole("abcdefgh");
            jsonObject.put("obj",user);
        }
        return ResponseEntity.ok("");
    }

    @GetMapping("/test2")
    public ResponseEntity test2() {
//        List<JSONObject> jsonObjectList = new ArrayList<>();

        for (int i = 0; i < 10000000; i++) {
            JSONObject jsonObject = new JSONObject();
            User user = new User();
            user.setUserName("abcdefgh");
            user.setId("abcdefgh");
            user.setPassword("abcdefgh");
            user.setRole("abcdefgh");
            jsonObject.put("obj",user);
//            jsonObjectList.add(jsonObject);
        }
//        jsonObjectList = null;
        return ResponseEntity.ok("");
    }

    @PostMapping("/login")
    public ResponseEntity Login(@RequestBody JwtRequest authenticationRequest, HttpServletResponse response) {
        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();

        try {
            String a = crypto.encrypt(password);
            user = userRepository.findOneByUserNameAndPassword(username, crypto.encrypt(password));
            if (username == null) {
                return ResponseEntity.ok("NOT FOUND");
            } else {
                try {
                    authenticate(username, password);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (Exception e) {
            return ResponseEntity.ok(e.toString());
        }
        final String token = jwtTokenUtil.generateToken(user);

        ResponseCookie cookie = ResponseCookie.from("jwt", token) // key & value
                .secure(true).httpOnly(true)
                .path("/")
                .sameSite("None")
                .domain(null)
                .maxAge(-1)
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        System.out.println("jwt: " + token);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/listFriend")
    public ResponseEntity listFriend() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PostMapping("/listChat")
    public ResponseEntity listChat(@RequestBody Map<String, String> data) {
        List<String> list = new ArrayList<>();
        list.add(data.get("id1"));
        list.add(data.get("id2"));
        List<Conversation> conversationList = conversationRepository.findByMembersIn(list);
        for (Conversation c :
                conversationList) {
            if (c.getMembers().contains(data.get("id1")) && c.getMembers().contains(data.get("id2"))) {
                return ResponseEntity.ok(c);
            }
        }
        return ResponseEntity.ok(null);
    }

    private void authenticate(String username, String password) throws Exception {
        try {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
