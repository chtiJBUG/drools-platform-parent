package org.chtijbug.drools.platform.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

public class DynamicBaseFilter extends OncePerRequestFilter {
    private static Logger logger = LoggerFactory.getLogger(DynamicBaseFilter.class);

    Pattern baseElementPattern = Pattern.compile("<base href=\"/\"");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.debug(">> doFilterInternal()");
        try {
            PrintWriter out = response.getWriter();
            CharResponseWrapper wrapper = new CharResponseWrapper(response);
            filterChain.doFilter(request, wrapper);

            String baseHref = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
            logger.debug("About to replace the base href element with {}", baseHref);
            String modifiedHtml = baseElementPattern.matcher(wrapper.toString()).replaceAll("<base href=\"" + baseHref + "\"");
            logger.debug("Modified HTML content : {}", modifiedHtml);
            // Write our modified text to the real response
            response.setContentLength(modifiedHtml.getBytes().length);
            out.write(modifiedHtml);
            out.close();
        } catch(Throwable t) {
            logger.error(t.getMessage());
        } finally {
            logger.debug("<< doFilterInternal()");
        }
    }
}


class CharResponseWrapper extends HttpServletResponseWrapper {
    private CharArrayWriter output;

    public CharResponseWrapper(HttpServletResponse response) {
        super(response);
        this.output = new CharArrayWriter();
    }

    public String toString() {
        return output.toString();
    }

    public PrintWriter getWriter() {
        return new PrintWriter(output);
    }
}
