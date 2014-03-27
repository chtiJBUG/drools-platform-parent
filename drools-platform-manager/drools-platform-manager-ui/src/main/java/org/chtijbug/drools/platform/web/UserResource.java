package org.chtijbug.drools.platform.web;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@Controller
public class UserResource {

    @RequestMapping(value= "/username", produces = TEXT_PLAIN_VALUE)
    @ResponseBody
    public String username(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}