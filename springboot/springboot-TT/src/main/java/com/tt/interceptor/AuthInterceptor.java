package com.tt.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tt.common.JwtUtil;
import com.tt.common.Result;
import com.tt.common.ServiceExceptionEnum;
import com.tt.common.UserContext;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final Set<String> ADMIN_PATHS = Set.of(
            "/app/query.staff.infos",
            "/app/user.staff.add",
            "/app/user.staff.modify",
            "/app/user.staff.delete",
            "/app/community.saveCommunity",
            "/app/community.updateCommunity",
            "/app/community.deleteCommunity",
            "/app/property.saveProperty",
            "/app/property.updateProperty",
            "/app/property.deleteProperty"
    );

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if (!StringUtils.hasText(token)) {
            write(response, 401, ServiceExceptionEnum.TOKEN_INVALID);
            return false;
        }
        try {
            Claims claims = jwtUtil.parse(token);
            UserContext.set(
                    claims.getSubject(),
                    claims.get("userName", String.class),
                    claims.get("role", String.class),
                    claims.get("storeId", String.class)
            );
        } catch (Exception e) {
            write(response, 401, ServiceExceptionEnum.TOKEN_INVALID);
            return false;
        }
        if (ADMIN_PATHS.contains(request.getServletPath()) && !UserContext.isAdmin()) {
            write(response, 403, ServiceExceptionEnum.FORBIDDEN);
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }

    private void write(HttpServletResponse response, int status, ServiceExceptionEnum error) throws Exception {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(Result.fail(error.getCode(), error.getMessage())));
    }
}
