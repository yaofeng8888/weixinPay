package com.yf.weixinpay.servlet;

import com.yf.weixinpay.util.PayCommonUtil;
import com.yf.weixinpay.util.ZxingUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class BuyServlet extends HttpServlet {
    private Random random = new Random();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
     //获取用户要购买的商品
        req.setCharacterEncoding("UTF-8");
        String body  =req.getParameter("body");
        String price = "1"; //微信的价格单位是分
        String order = random.nextInt(1000000)+"";//商户订单号，实际开发参照自己的需求
     //生成二维码
        //1、先得到二维码的原始字符串
            //按照微信的要求生成字符串

        //2、将字符串生成二维码
        try {
        String result  = PayCommonUtil.weixin_pay(price,body,order);//获取到二维码的字符串
            System.out.println(result);
            BufferedImage image = ZxingUtil.createImage(result,300,300);
            //跳转到支付页面，显示二维码
            req.setAttribute("image",image);
            req.setAttribute("oid",order);
            resp.sendRedirect("/payment.jsp");
        }catch (Exception e){
            e.printStackTrace();
        }
     //跳转到支付页面，显示二维码
    }
}
