package org.chtijbug.drools.platform.web.filter;

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

    Pattern baseElementPattern = Pattern.compile("<base href=\"/\" />");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        CharResponseWrapper wrapper = new CharResponseWrapper(response);
        filterChain.doFilter(request, wrapper);

        String baseHref = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        String modifiedHtml = baseElementPattern.matcher(wrapper.toString()).replaceAll("<base href=\""+ baseHref + "\" />");

        // Write our modified text to the real response
        response.setContentLength(modifiedHtml.getBytes().length);
        out.write(modifiedHtml);
        out.close();
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
