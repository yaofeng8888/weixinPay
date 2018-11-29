package com.yf.weixinpay.util;

import com.yf.weixinpay.websocket.Websocekt;
import org.jdom.JDOMException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import javax.xml.bind.Element;
import java.io.*;
import java.util.*;

//处理微信返回的数据
public class ResultServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            weixin_notify(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    public void weixin_notify(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JDOMException {
        String writeContent = "默认支付失败";
        String path = request.getServletContext().getRealPath("file");//实际开发中删除
        File file = new File(path);//实际开发中删除
        if (!file.exists()) {
            file.mkdirs();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(path + "/result.txt");//实际开发中删除
        //读取参数
        InputStream inputStream;
        StringBuffer sb = new StringBuffer();
        inputStream = request.getInputStream();
        String s;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null) {
            sb.append(s);
        }
        in.close();
        inputStream.close();


        //解析xml成map
        Map<String, String> m = new HashMap<String, String>();
        m = XMLUtil.doXMLParse(sb.toString());
        //过滤空 设置TreeMap
        SortedMap<Object, Object> packgeParams = new TreeMap<Object, Object>();
        Iterator it = m.keySet().iterator();
        while (it.hasNext()) {
            String parameter = (String) it.next();
            String parameterValue = m.get(parameter);

            String v = "";
            if (null != parameterValue) {
                v = parameterValue.trim();
            }
            packgeParams.put(parameter, v);

        }

        //账号信息
        String key = PayConfigUtils.API_KEY;
        System.err.println(packgeParams);
        String out_trade_no = (String) packgeParams.get("out_trade_no");
        //判断签名是否正确
        if (PayCommonUtil.isTenpaySign("UTF-8", packgeParams, key)) {
            //处理业务
            String resXml = "";
            if ("SUCCESS".equals((String) packgeParams.get("result_code"))) {
                //支付成功
                String mch_id = (String) packgeParams.get("mch_id");
                String openid = (String) packgeParams.get("openid");
                String is_subscribe = (String) packgeParams.get("is_subscribe");
                String tatal_fee = (String) packgeParams.get("total_fee");

                //执行自己需要的业务逻辑

                writeContent = "订单" + out_trade_no + "支付成功";
                //通知微信，异步确认成功，必须写，不然会一直通知后台，八次后就任务交易失败，钱就退回到用户手中
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg>" + "</xml>";
                //知道支付成功。通知页面，跳转到支付成功的页面  websocket
                //找到这个订单对应的websocket
//               Session session =  Websocekt.getAllClients().get(out_trade_no);//当前订单号所对应的websocekt链接
//                if(session!=null){
//                        session.getAsyncRemote().sendText("支付成功");
//                }
                    Websocekt.sendMessage(out_trade_no,"支付成功");
            } else {
                writeContent = "订单" + out_trade_no + "支付失败:";
                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[报文为空]]></return_msg>" + "</xml>";
            }
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            bufferedOutputStream.write(resXml.getBytes());
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        }else {
            writeContent = "订单"+out_trade_no+"通知签名验证失败，支付失败";

        }
        fileOutputStream.write(writeContent.getBytes());//实际开发中删除
        fileOutputStream.close();//实际开发中给删除
    }

}
