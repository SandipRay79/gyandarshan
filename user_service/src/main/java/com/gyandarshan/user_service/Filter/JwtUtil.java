package com.gyandarshan.user_service.Filter;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {

    // Minimum 32 characters for HS256
    private static final String SECRET_KEY =
            "4c8f0a2b9d1e7f5c3a6b8d9e0f1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9";

    // Generate JWT Token
    public String generateToken(String username) {

        try {

            JWSSigner signer =
                    new MACSigner(SECRET_KEY.getBytes());

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(username)
                    .issueTime(new Date())
                    .expirationTime(
                            new Date(System.currentTimeMillis()
                                    + 1000 * 60 * 60)
                    )
                    .claim("role", "USER")
                    .build();

            SignedJWT signedJWT =
                    new SignedJWT(
                            new JWSHeader(JWSAlgorithm.HS256),
                            claimsSet
                    );

            signedJWT.sign(signer);

            return signedJWT.serialize();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Validate Token
    public boolean validateToken(String token) {

        try {

            SignedJWT signedJWT =
                    SignedJWT.parse(token);

            JWSVerifier verifier =
                    new MACVerifier(SECRET_KEY.getBytes());

            return signedJWT.verify(verifier)
                    &&
                    new Date().before(
                            signedJWT.getJWTClaimsSet()
                                    .getExpirationTime()
                    );

        } catch (Exception e) {
            return false;
        }
    }

    // Extract Username
    public String extractUsername(String token) {

            try {

                SignedJWT signedJWT =
                        SignedJWT.parse(token);

                return signedJWT.getJWTClaimsSet()
                        .getSubject();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
}
