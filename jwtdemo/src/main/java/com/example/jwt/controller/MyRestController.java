package com.example.jwt.controller;

import com.example.jwt.JwtdemoApplication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.DatatypeConverter;
import java.util.Date;

@RestController
@RequestMapping("/token")
public class MyRestController {

    Logger logger = LoggerFactory.getLogger(JwtdemoApplication.class);
    private static final String SECRET_KEY = "84DAF5295E5BB1DF";

    @GetMapping
    public String getToken()
    {
        Date issuedTime = new Date(System.currentTimeMillis());
        String token = Jwts.builder()
                .setSubject("JWT Token demo")
                .setIssuedAt(issuedTime)
                .setIssuer("surabhi")
                .claim("TEST_CLAIM", "Hello")
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();

        return token;
    }

    @PostMapping("/verify")
    public String verifyToken(@RequestHeader(value = "token", defaultValue = "NA") String token)
    {
        logger.debug("Token verification initiated");
        String response = "INVALID TOKEN";
        try{
            Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                    .parseClaimsJws(token)
                    .getBody();

            if(claims.get("TEST_CLAIM").equals("Hello"))
            {
                response = "VALID TOKEN";
            }
        }
        catch(Exception e)
        {
            response = "INVALID TOKEN";
            logger.debug("INVALID TOKEN");
        }
        return response;
    }
}
