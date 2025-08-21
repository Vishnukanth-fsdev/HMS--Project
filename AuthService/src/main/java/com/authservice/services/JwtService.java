package com.authservice.services;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class JwtService {
	
	private static final String SECRET_KEY="my-super-secret-key";
	private static final long EXPIRATION_TIME=86400000;
	private static final long REFRESH_TIME= 86400000*7;
	
	
	public String generateToken(String username, String role) {
		return JWT.create()
				.withSubject(username)
				.withClaim("role", role)
				.withIssuedAt(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
				.sign(Algorithm.HMAC256(SECRET_KEY));
	}
	
	public String validateTokenAndRetrieveSubject(String token) {
		return JWT.require(Algorithm.HMAC256(SECRET_KEY))
				.build()
				.verify(token)
				.getSubject();
	}
	
	/*
	 * public String refreshToken(String username,String role) { return JWT.create()
	 * .withSubject(username) .withClaim("role",role) .withIssuedAt(new Date())
	 * .withExpiresAt(new Date(System.currentTimeMillis()+REFRESH_TIME))
	 * .sign(Algorithm.HMAC256(SECRET_KEY)); }
	 * 
	 * public String getUsernameFromToken(String token) { DecodedJWT decodedJWT =
	 * JWT.require(Algorithm.HMAC256(SECRET_KEY)) .build() .verify(token); return
	 * decodedJWT.getSubject(); }
	 * 
	 * public String getUsernameFromTokenRole(String token) { DecodedJWT decodedJWT
	 * = JWT.require(Algorithm.HMAC256(SECRET_KEY)) .build() .verify(token); return
	 * decodedJWT.getClaim("role").asString(); }
	 */
}
