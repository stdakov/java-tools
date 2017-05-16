package helper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

public class JwtTokenFactory {

    private String privateKey = "secret_key";
    private Integer expSeconds = 84600;

    public String generate(Serializable object) throws IOException {
        String stringObject = ObjectToString.toString(object);
        return Jwts.builder()
                .setSubject(stringObject)
                .setExpiration(expirationSeconds(expSeconds))
                .signWith(SignatureAlgorithm.HS512, this.privateKey)
                .compact();
    }

    public Object parse(String token) throws IOException, ClassNotFoundException {
        String stringObject = Jwts.parser().setSigningKey(this.privateKey).parseClaimsJws(token).getBody().getSubject();

        return ObjectToString.fromString(stringObject);
    }

    private Date expirationSeconds(int seconds) {
        return new Date(System.currentTimeMillis() + (seconds * 1000));
    }
}
