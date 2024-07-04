package cn.sudoer.javaproj.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.LoggerFactory;

@Entity
@Table(name = "sys_user")
public class SysUser {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String username;
    @Column
    private String passwordHash;
    @Column
    private String email;

    public SysUser() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
    static public String getHashString(String password) {
        String salt = "salt";
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // 对密码加盐
            password = password + salt;
            // Add password bytes to digest
            md.update(password.getBytes());

            // Get the hash's bytes
            byte[] bytes = md.digest();

            // Convert the bytes to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }

            // Set the password hash
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            LoggerFactory.getLogger(SysUser.class).error("Error in hashing password", e);
            return null;
            // Handle exception
        }
    }
    public void setPasswordHashByClearText(String password) {
        this.passwordHash = getHashString(password);
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
