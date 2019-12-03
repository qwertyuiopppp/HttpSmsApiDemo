package xiaoxi.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class XiaoXiHttpClient {

    private String ip ;
    private String account;
    private String apiKey;

    public XiaoXiHttpClient(String ip, String account, String apiKey){
        this.ip = ip;
        this.account = account;
        this.apiKey = apiKey;
    }

    public  String  sendSmsPost(String content,String mobiles){
        String url = "http://" + ip + "/api/sms/mt";
        JSONObject jsonObject = new JSONObject();
        long ts1 = new Date().getTime();
        String ts = String.valueOf(ts1);
        jsonObject.put("mobile", mobiles);
        jsonObject.put("content", content);
        jsonObject.put("userid", account);
        jsonObject.put("ts", ts);
        jsonObject.put("sign", getSign(account,ts,apiKey));
        String result = postJson(url, jsonObject);
        return  result;
    }

    public String getReport(){
        String url = "http://" + ip + "/api/sms/report";
        JSONObject jsonObject = new JSONObject();
        long ts1 = new Date().getTime();
        String ts = String.valueOf(ts1);
        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(new BasicNameValuePair("userid",account));
        nameValuePairList.add(new BasicNameValuePair("ts",ts));
        nameValuePairList.add(new BasicNameValuePair("sign",getSign(account,ts,apiKey)));
        String result =  get(url,nameValuePairList);
        return  result;
    }

    public String getMo(){
        String url = "http://" + ip + "/api/sms/mo";
        JSONObject jsonObject = new JSONObject();
        long ts1 = new Date().getTime();
        String ts = String.valueOf(ts1);
        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(new BasicNameValuePair("userid",account));
        nameValuePairList.add(new BasicNameValuePair("ts",ts));
        nameValuePairList.add(new BasicNameValuePair("sign",getSign(account,ts,apiKey)));
        String result =  get(url,nameValuePairList);
        return  result;
    }

    public  String  sendSmsForm(String content,String mobiles){
        String url = "http://" + ip + "/api/sms/mt";
        Map<String,String> map = new HashMap<String, String>();
        long ts1 = new Date().getTime();
        String ts = String.valueOf(ts1);
        map.put("mobile", mobiles);
        map.put("content", content);
        map.put("userid", account);
        map.put("ts", ts);
        map.put("sign", getSign(account,ts,apiKey));
        String result = postForm(url, map);
        return  result;
    }

    public  String get(String url,List<NameValuePair> nameValuePairList){
        HttpClient client = HttpClientBuilder.create().build();
        String result = null;

        long ts1 = new Date().getTime();
        String ts = String.valueOf(ts1);
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            uriBuilder.addParameters(nameValuePairList);
            HttpGet get = new HttpGet(uriBuilder.build());
            HttpResponse res = client.execute(get);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = res.getEntity();
                result = EntityUtils.toString(entity);
                //response = JSONObject.parseObject(result);
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public String postJson(String url,JSONObject jsonObject){
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        String result = null;

        try {
            StringEntity s = new StringEntity(jsonObject.toString());
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");
            post.setEntity(s);
            HttpResponse res = client.execute(post);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = res.getEntity();
                result = EntityUtils.toString(entity);
                //response = JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public String postForm(String url,Map<String,String> paramsMap){
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        String result = null;

        try {
            List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
            for (String key : paramsMap.keySet())
            {
                pairList.add(new BasicNameValuePair(key,paramsMap.get(key)));
            }
            UrlEncodedFormEntity uefe = new UrlEncodedFormEntity(pairList, "UTF-8");
            uefe.setContentType("application/x-www-form-urlencoded");
            post.setEntity(uefe);

            HttpResponse res = client.execute(post);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = res.getEntity();
                result = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public String strMD5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return result;
    }

    public String getSign(String userid,String ts,String apiKey) {
        String str = userid.trim()+ts.trim()+apiKey.trim();
        String strMD5 = strMD5(str);
        return strMD5;
    }
}
