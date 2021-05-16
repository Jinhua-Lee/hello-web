package com.servlet;

import com.util.VerifyCode;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 验证码
 *
 * @author Jinhua
 */
@SuppressWarnings("all")
@WebServlet(name = "VerifyCodeServlet", urlPatterns = "/VerifyCodeServlet")
public class VerifyCodeServlet extends BaseServlet {

    public void getVerifyPic(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        VerifyCode vc = new VerifyCode();
        BufferedImage image = vc.getImage();
        resp.setContentType("image/jpeg");
        VerifyCode.output(image, resp.getOutputStream());
        req.getSession().setAttribute("verifyCode", vc.getText());
        System.out.println(vc.getText());
    }
}
