<%--
  Created by IntelliJ IDEA.
  User: xq0001
  Date: 2018/11/28
  Time: 11:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript">
        var websocekt = null;

        function load() {//初始化websocekt
            //获取订单号
            var id = document.getElementById("oid").innerHTML;
            //建立链接
            if ('WebSocket' in window) {
                websocekt = new WebSocket("ws://" + document.location.host + "/websocket/" + id);

                websocekt.onopen = function () {

                }

                websocekt.onclose = function () {

                }

                websocekt.onerror = function () {

                }

                websocekt.onmessage = function (event) {
                    console.log("返回数据");
                    console.log(event);
                    fillData(event.data);
                    location.href = "#"//需要跳转的页面
                }
            } else {
                alert("浏览器不支持websocekt")
            }

            //设置监听
            function fillData(data) {
                document.getElementById("message").innerHTML = data;
            }

        }

        // window.onload = load();
    </script>
</head>
<body onload="load()">
当前是支付页面，订单号是<span id="oid">${oid}</span><br>请扫码支付
<img src="/payment/image">
<span id="message"></span>
</body>
</html>
