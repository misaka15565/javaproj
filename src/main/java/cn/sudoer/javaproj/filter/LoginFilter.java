package cn.sudoer.javaproj.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.sudoer.javaproj.service.UserCookieService;

@WebFilter(urlPatterns = "/app/*")
public class LoginFilter implements Filter {
    @Autowired
    private final UserCookieService userCookieService;
    public LoginFilter(UserCookieService ucs) {
        userCookieService = ucs;
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化代码（如果需要）
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 检查用户是否已登录，这里的逻辑需要根据你的应用进行调整
        // 例如，检查session或者特定的header是否存在
        Cookie[] allcookie = httpRequest.getCookies();
        //找到auth cookie
        String authCookie = null;
        if (allcookie != null) {
            for (Cookie cookie : allcookie) {
                if (cookie.getName().equals("auth")) {
                    authCookie = cookie.getValue();
                    break;
                }
            }
        }
        if (userCookieService.checkCookie(authCookie)==false) {
            // 用户未登录，重定向到登录页面或返回错误信息
            LoggerFactory.getLogger(getClass()).trace("user not login");
            httpResponse.sendRedirect("/login");
            return;
        }

        // 用户已登录，继续请求的处理
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 清理代码（如果需要）
    }
}