package dev.remo.remo.Utils.JWTAuth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import dev.remo.remo.Models.Users.User;

import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class); // Logger for logging errors

  @Value("${jwt.accessToken.secret}")
  private String accessTokenSecret;

  @Value("${jwt.refreshToken.secret}")
  private String refreshTokenSecret;

  @Value("${jwt.accessToken.expiration}")
  private long accessTokenExpirationMs;

  @Value("${jwt.refreshToken.expiration}")
  private long refreshTokenExpirationMs;
  
  private Key getAccessTokenKey() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenSecret));
  }

  private Key getRefreshTokenKey() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshTokenSecret));
  }

  public String generateAccessToken(Authentication authentication) {
    return buildToken(((User) authentication.getPrincipal()).getUsername(), accessTokenExpirationMs,
        getAccessTokenKey());
  }

  public String generateRefreshToken(Authentication authentication) {
    return buildToken(((User) authentication.getPrincipal()).getUsername(), refreshTokenExpirationMs,
        getRefreshTokenKey());
  }

  private String buildToken(String subject, long expiration, Key key) {
    return Jwts.builder()
        .setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public String getEmailFromAccessToken(String token) {
    return Jwts.parserBuilder().setSigningKey(getAccessTokenKey()).build()
        .parseClaimsJws(token).getBody().getSubject();
  }

  public String getEmailFromRefreshToken(String token) {
    return Jwts.parserBuilder().setSigningKey(getRefreshTokenKey()).build()
        .parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateAccessToken(String token) {
    return validateJwtToken(token, getAccessTokenKey());
}

public boolean validateRefreshToken(String token) {
    return validateJwtToken(token, getRefreshTokenKey());
}

  public boolean validateJwtToken(String token, Key key) {
    Jwts.parserBuilder().setSigningKey(key).build().parse(token);
    return true;
  }

  public void setJwtCookie(HttpServletResponse response, String token) {
    Cookie cookie = new Cookie("refreshToken", token);
    cookie.setHttpOnly(true);
    cookie.setSecure(true); // Enable in production
    cookie.setPath("/");
    cookie.setMaxAge(24 * 60 * 60); // 1 day
    response.addCookie(cookie);
  }

  public void cleanJwtCookie(HttpServletResponse response) {
    Cookie cookie = new Cookie("refreshToken", null);
    cookie.setHttpOnly(true);
    cookie.setSecure(true);
    cookie.setPath("/");
    cookie.setMaxAge(0);
    response.addCookie(cookie);
  }
}