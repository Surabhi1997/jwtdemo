package com.example.jwt.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.DatatypeConverter;
import java.util.Date;

@RestController
public class MyRestController {

    private static final String SECRET_KEY = "84DAF5295E5BB1DF";

    @GetMapping("/gettoken")
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

    @PostMapping("/verifyToken")
    public String verifyToken(@RequestParam(value = "token", defaultValue = "NA") String token)
    {
        System.out.println("Token verification inititated");
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
        catch(ExpiredJwtException e)
        {
            System.out.println("Token was expired");
        }
        catch(Exception e)
        {
            response = "INVALID TOKEN";
            System.out.println("Error while parsing token");
        }
        return response;
    }
}
