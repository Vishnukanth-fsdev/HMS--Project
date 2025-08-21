package com.authservice.controller;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authservice.dto.APIResponse;
import com.authservice.dto.LoginDto;
import com.authservice.dto.RefreshToken;
import com.authservice.dto.UserDto;
import com.authservice.services.AuthService;
import com.authservice.services.JwtService;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtService jwtService;
	
	@PostMapping("/register")
	public ResponseEntity<APIResponse<String>> register(@RequestBody UserDto userdto){
		APIResponse<String> response=authService.register(userdto);
		return new ResponseEntity<>(response,HttpStatusCode.valueOf(response.getStatus()));
	}
	
	@PostMapping("/login")
	public ResponseEntity<APIResponse<String>> login(@RequestBody LoginDto logindto){
		APIResponse<String> response=new APIResponse<>();
		UsernamePasswordAuthenticationToken token =new UsernamePasswordAuthenticationToken(logindto.getUsername(),logindto.getPassword());
		try {
			Authentication authenticate=authManager.authenticate(token);
			if(authenticate.isAuthenticated()) {
				String jwtToken=jwtService.generateToken(logindto.getUsername(),
						authenticate.getAuthorities().iterator().next().getAuthority());
				//String refreshToken=jwtService.refreshToken(logindto.getUsername(),
						//authenticate.getAuthorities().iterator().next().getAuthority());
				response.setMessage("Login Successfull");
				response.setStatus(200);
				response.setData(jwtToken);
				return new ResponseEntity<>(response,HttpStatusCode.valueOf(response.getStatus()));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		response.setMessage("Login Failed");
		response.setStatus(500);
		response.setData("Un-Authorized Access");
		return new ResponseEntity<>(response,HttpStatusCode.valueOf(response.getStatus()));
		
	}
	
	/*
	 * @PostMapping("/refresh") public ResponseEntity<APIResponse<String>>
	 * refreshToken(@RequestBody RefreshToken request){ String
	 * refreshToken=request.getRefreshToken(); System.out.println(refreshToken);
	 * if(jwtService.validateTokenAndRetrieveSubject(refreshToken) != null) { String
	 * username=jwtService.getUsernameFromToken(refreshToken); String
	 * newToken=jwtService.generateToken(username,
	 * jwtService.getUsernameFromTokenRole(refreshToken)); APIResponse<String>
	 * response=new APIResponse<>(); response.setMessage("New Token Generated");
	 * response.setStatus(200); response.setData(newToken); return new
	 * ResponseEntity<>(response,HttpStatusCode.valueOf(response.getStatus()));
	 * 
	 * } APIResponse<String> response=new APIResponse<>();
	 * response.setMessage("New Token Not Generated"); response.setStatus(200);
	 * response.setData("Something wrong"); return new
	 * ResponseEntity<>(response,HttpStatusCode.valueOf(response.getStatus()));
	 * 
	 * }
	 */

}
