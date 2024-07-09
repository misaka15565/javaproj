package cn.sudoer.javaproj.service;

import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;

import cn.sudoer.javaproj.bean.KeyPairBean;
import jakarta.servlet.http.Cookie;


@Service
public class UserCookieService {
    private final KeyPairBean keyPairBean;
    // 有效时间1h
    private final long validTime = 3600000;

    public UserCookieService(KeyPairBean keyPairBean) {
        this.keyPairBean = keyPairBean;
    }

    public PublicKey getPublicKey() {
        return keyPairBean.getKeyPair().getPublic();
    }

    public PrivateKey getPrivateKey() {
        return keyPairBean.getKeyPair().getPrivate();
    }

    public String getCookie(String username) {
        // 加密
        // 获取当前时间
        long now = System.currentTimeMillis();
        // 计算过期时间
        long expire = now + validTime;
        // 拼接字符串
        String data = username + "|" + expire;
        // 加密
        return keyPairBean.encrypt(data);
    }

    public boolean checkCookie(String cookie) {
        // 解密
        String data = keyPairBean.decrypt(cookie);
        if (data == null || data.isEmpty()) {
            return false;
        }
        // 解析数据
        String[] arr = data.split("\\|");
        if (arr.length != 2) {
            return false;
        }
        // 获取过期时间
        long expire = Long.parseLong(arr[1]);
        // 获取当前时间
        long now = System.currentTimeMillis();
        // 判断是否过期
        return now < expire;
    }
    public String getUsernameFromCookie(String cookie){
        String data = keyPairBean.decrypt(cookie);
        if (data == null || data.isEmpty()) {
            return "";
        }
        String[] arr = data.split("\\|");
        if (arr.length != 2) {
            return "";
        }
        return arr[0];
    }
    public String getExpireTimeString(String cookie){
        String data = keyPairBean.decrypt(cookie);
        if (data == null || data.isEmpty()) {
            return "";
        }
        String[] arr = data.split("\\|");
        if (arr.length != 2) {
            return "";
        }
        //将时间戳转化为时间字符串
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(Long.parseLong(arr[1])));
    }
    public String getUsernameFromCookies(Cookie[] cookies){
        String authCookie = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("auth")) {
                    authCookie = cookie.getValue();
                    break;
                }
            }
        }
        if(authCookie==null)return null;
        return getUsernameFromCookie(authCookie);
    }
}
