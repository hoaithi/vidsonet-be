package com.hoaithidev.vidsonet.utils;

import com.hoaithidev.vidsonet.dto.request.UserRequest;
import com.hoaithidev.vidsonet.entity.User;
import com.hoaithidev.vidsonet.exception.AppException;
import com.hoaithidev.vidsonet.exception.ErrorCode;
import com.hoaithidev.vidsonet.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final UserRepository userRepository;
    private final StringHttpMessageConverter stringHttpMessageConverter;


    @Value("${jwt.signerKey}")
    String SecretKey;
    public String generateToken(String email) throws JOSEException {
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256).build();
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(email)
                .expirationTime(new Date(new Date().getTime() + 1000*60*60*24))
                .issueTime(new Date())
                .issuer("hoaithidev.com")
                .claim("scope", email.equals("admin@gmail.com")?"ADMIN":"USER")
                .build();
        Payload payload = new Payload(claims.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        JWSSigner signer = new MACSigner(SecretKey.getBytes());
        jwsObject.sign(signer);
        return jwsObject.serialize();
    }

    public boolean verifyToken(String token) throws JOSEException, ParseException {
        JWSObject jwsObject = JWSObject.parse(token);
        JWSVerifier verifier = new MACVerifier(SecretKey.getBytes());
        return jwsObject.verify(verifier);
    }
    public boolean isTokenExpired(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            // Kiểm tra nếu claims null
            if (claims == null) {
                throw new IllegalArgumentException("Invalid JWT: Claims is null");
            }

            Date expirationTime = claims.getExpirationTime();
            return expirationTime != null && expirationTime.before(new Date());
        } catch (ParseException e) {
            System.err.println("Invalid or malformed JWT: " + e.getMessage());
            return true; // Xem token như đã hết hạn nếu không parse được
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return true;
        }
    }
}
