package dev.plotnikov.polystore.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.plotnikov.polystore.services.CheckTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@AllArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final CheckTokenService checkAuthorization;
    private final List<String> fileExtensions = Arrays.asList("html", "js", "json", "csv", "css", "png", "svg", "eot", "ttf", "woff", "woff2", "appcache", "jpg", "jpeg", "gif", "ico");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().equals("/api/login") || request.getServletPath().equals("/api/token/refresh") || request.getServletPath().equals("/js/main.js")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    String token = authorizationHeader.substring("Bearer ".length());
                    UsernamePasswordAuthenticationToken authenticationToken = checkAuthorization.checkAuthorization(token);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception exception) {
                    log.error("Error logging in: {}", exception.getMessage());
                    response.setHeader("error", exception.getMessage());
                    response.setStatus(FORBIDDEN.value());
                    Map<String, String> error = new HashMap<>();
                    error.put("reason", exception.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
            } else {
                doFilter(request, response, filterChain);
            }
        }
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String path = request.getServletPath();

        boolean isApi = path.startsWith("/api");
        boolean isWs = path.startsWith("/gs-guide-websocket");
        boolean isSwagger = path.startsWith("/swagger-ui");
        boolean isSwaggerSec = path.startsWith("/v3");
        boolean isResourceFile = !isApi && fileExtensions.stream().anyMatch(path::contains);

        if (isApi || isWs || isSwagger || isSwaggerSec) {
            chain.doFilter(request, response);
        } else if (isResourceFile) {
            resourceToResponse("static" + path, response);
        } else {
            resourceToResponse("static/index.html", response);
        }
    }

    private void resourceToResponse(String resourcePath, HttpServletResponse response) throws IOException {
        InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resourcePath);

        if (inputStream == null) {
            response.sendError(NOT_FOUND.value(), NOT_FOUND.getReasonPhrase());
            return;
        }
        inputStream.transferTo(response.getOutputStream());
    }
}
