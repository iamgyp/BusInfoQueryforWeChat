package com.wechat.servlet;  
  
import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.PrintWriter;
import java.util.Arrays;  
import java.util.Map;  
  
import java.util.ResourceBundle;

import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  

import org.apache.http.client.methods.HttpUriRequest;
  




import com.guyiping.GetBusInfo;
import com.guyiping.GetMessage;
import com.wechat.utils.EncryptUtil;  
import com.wechat.utils.XmlUtil;  
  
public class WeChatServlet extends HttpServlet {  
  
    private static final long serialVersionUID = 1L;  
      
    //微信平台上填的Token和这里需要一致  
    public static final String Token = "weixin";  
  
    /** 
     * 微信公众平台验证调用方法 
     */  
    @Override  
    protected void doGet(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {  
    	
        String signature = request.getParameter("signature");  
        String timestamp = request.getParameter("timestamp");  
        String nonce = request.getParameter("nonce");  
        String[] ArrTmp = { Token, timestamp, nonce };  
        Arrays.sort(ArrTmp);  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < ArrTmp.length; i++) {  
            sb.append(ArrTmp[i]);  
        }  
        String pwd = EncryptUtil.Encrypt(sb.toString());  
        String echostr = request.getParameter("echostr");  
        //System.out.println("pwd=="+pwd);  
        //System.out.println("echostr=="+echostr);  
        if(pwd.equals(signature)){  
            if(!"".equals(echostr) && echostr != null){  
                response.getWriter().print(echostr);  
            }  
        }  
        
    	/*
    	ResourceBundle rb =
                ResourceBundle.getBundle("LocalStrings",request.getLocale());
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            out.println("<html>");
            out.println("<head>");

            String title = rb.getString("helloworld.title");

            out.println("<title>" + title + "</title>");
            out.println("</head>");
            out.println("<body bgcolor=\"white\">");
            out.println("</body>");
            out.println("</html>");
            */
    }  
  
    /** 
     * 用户向公众平台发信息并自动返回信息 
     */  
    @Override  
    protected void doPost(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {  
        StringBuffer sb = new StringBuffer();  
        String line;  
        Map<String, String> map = null;  
        String message = null;
        try {  
            request.setCharacterEncoding("UTF-8");  
            BufferedReader reader = request.getReader();  
            while ((line = reader.readLine()) != null) {  
                sb.append(line);  
            }  
            map = XmlUtil.xml2Map(sb.toString());  
            
            String UserInputMsgType = map.get("xml.MsgType");
            String UserInputContent =  map.get("xml.Content");
            
            GetBusInfo getBusInfo = GetBusInfo.getInstance();

			message = GetMessage.getMessage(UserInputMsgType,UserInputContent);
			
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        sb = new StringBuffer();  
        setResponseMessage(map,sb,message);
        response.setCharacterEncoding("UTF-8");  
        //System.out.println(sb.toString());  
        response.getWriter().print(sb.toString());  
    }  
  
  
    @Override  
    public void destroy() {  
        super.destroy();  
    }  
  
    @Override  
    public void init() throws ServletException {  
        super.init();  
    }  
    
    
    public void setResponseMessage(Map<String, String> map,StringBuffer sb, String message)
    {
    	sb.append("<xml><ToUserName><![CDATA[").append(  
                map.get("xml.FromUserName")).append(  
                "]]></ToUserName><FromUserName><![CDATA[").append(  
                map.get("xml.ToUserName")).append(  
                "]]></FromUserName><CreateTime>").append(  
                map.get("xml.CreateTime")).append(  
                "</CreateTime><MsgType><![CDATA[text]]></MsgType>").append(  
                "<Content><![CDATA[").append(message); 
        sb.append("]]></Content>").append("<FuncFlag>0</FuncFlag></xml>");  
    }
  
}