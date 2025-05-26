package ru.hits.core.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final String TRACE_ID_HEADER = "X-Trace-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String traceId = request.getHeader(TRACE_ID_HEADER);
        if (traceId == null || traceId.isEmpty()) {
            traceId = UUID.randomUUID().toString();
        }

        Instant start = Instant.now();
        String path = request.getRequestURI();
        String method = request.getMethod();

        MDC.put("traceId", traceId);
        MDC.put("requestPath", path);
        MDC.put("requestMethod", method);
        log.debug("Started processing HTTP {} {}", method, path);

        try {
            response.addHeader(TRACE_ID_HEADER, traceId);

            filterChain.doFilter(request, response);

            long responseTimeMs = Duration.between(start, Instant.now()).toMillis();
            MDC.put("statusCode", String.valueOf(response.getStatus()));
            MDC.put("responseTimeMs", String.valueOf(responseTimeMs));

            log.info("Completed HTTP {} {} with {} in {} ms",
                    method, path, response.getStatus(), responseTimeMs);

        } catch (Exception ex) {
            long responseTimeMs = Duration.between(start, Instant.now()).toMillis();
            MDC.put("responseTimeMs", String.valueOf(responseTimeMs));

            log.error("Error processing HTTP {} {} after {} ms",
                    method, path, responseTimeMs, ex);

            throw ex;
        } finally {
            MDC.clear();
        }
    }
}