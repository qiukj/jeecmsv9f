package com.jeecms.cms.task.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecms.cms.service.SpiderJobCallable;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by linzuk on 2018/2/26.
 * 爬虫任务：每天定时爬取一个网站的数据
 */
public class SpiderJob {

    @Autowired
    private SpiderJobCallable callable;

    public void execute() {
        try {
            // 执行作业
            doSpiderJob(callable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doSpiderJob(SpiderJobCallable callable) throws IOException {
        // 登入
        String ssoUrl = login();
        System.out.println("sso: " + ssoUrl);
        // 单点登入
        sso(ssoUrl);
        // 请求页面
        String json = pageDate(1);
        // 提取总页数
        int totalPage = totalPage(json);
        System.out.println(totalPage);
        // 开始抓取数据
        for (int i = 1; i <= totalPage; i++) {
            String dataJson = pageDate(i);
            System.out.println(i + " : " + dataJson);
            // 从json提出去有用的数据
            boolean isBreak = fetchUsableData(dataJson, callable);
            System.out.println("第"+i+"页数据获取完毕！");
            if (isBreak) break;
        }
    }

    // APP登入请求
    private static String login() throws IOException {
        Connection conn = Jsoup.connect("http://sso.diexun.com/ClientsLogin/index")
                .cookies(cookies)
                .timeout(10*1000)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")
                .ignoreContentType(true)
                .data("info", "eyJjcHVfaW5mbyI6IjIwMTcxMTIwMDkwNjU3NDY0IiwiUGNUeXBlIjoiMTAwIiwiaGRfaW5mbyI6InVuZGVmaW5lZCIsIlZlciI6IjEuMi4wIiwicGFzc3dvcmQiOiI2NTQzMjEiLCJwY19uYW1lIjoiaVBhZDYsMTEiLCJ1c2VybmFtZSI6ImRlc2hlbmcxNjkiLCJhdXRoa2V5IjoiZTE5NGJlMDlmNzlhYTZiZDA5OGJhOTg1YjRmZWI0OGEiLCJuZXRfaW5mbyI6InVuZGVmaW5lZCIsImFwcGlkIjoiMiJ9");
        Connection.Response resp = conn.method(Connection.Method.POST).execute();
        cookies.putAll(encodeAll(resp.cookies()));
        JSONObject json = JSON.parseObject(resp.body());
        Integer status = json.getInteger("status");
        if (2 != status) {
            System.out.println("登入失败: " + resp.body());
            throw new RuntimeException("登入失败");
        }
        return json.getString("uri");
    }

    // 网页单点登入请求
    private static void sso(String ssoUrl) throws IOException {
        Connection conn = Jsoup.connect(ssoUrl).cookies(cookies).timeout(10*1000);
        Connection.Response resp = conn.method(Connection.Method.GET).execute();
        cookies.putAll(encodeAll(resp.cookies()));
        String body = resp.body();
        System.out.println(body);
    }

    // 蕾丝列表页面请求
    private static String pageDate(int page) throws IOException {
        Connection conn = Jsoup.connect("http://www.sxxl.com/StyleGallery-index-extid-32940-cid-2-channel-32209.html")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")
                .data("p", "" + page)
                .ignoreContentType(true)
//                .header("Cookie", "screenWidth=1920; 28ae21cdfc8d8236e4d783cb94411e97=d6advWeQCXkmIYGSMRWnbCwnzmCRlRwzAeNjPGBQ9BdoGtegtLLJ; haveShownLatest_2085=1; haveShownLatest_32209=1; lastViewCategory=2; %5F_ad_view=19; %5F_ad_date=1519380166; Hm_lvt_a3b242930672e1d3dd7781c8cd80b09a=1519369183,1519375791; Hm_lpvt_a3b242930672e1d3dd7781c8cd80b09a=1519380167; loginInfo=ec37sjnR400qk0Lob07xBmvN6tTFy416rMvgSpaj94VCs0ATdW0p67IzvrH5QSpQwTqvQUbPqsSD2zj0Bieilh0PIEiKpncZABZtrHNeuGeKVDSrxCeMbhkmpBsyxuqIDS3iG%2BykAQq4ZXehcwSlPfhG8n9b6ezAzwSIk5fknE%2F%2BRQLuMNJHO8lV%2FRiMeSj%2BMrfQ6SKmYQLTzyoCyKF16z%2BqAxaWDA1QGWRzoBhzu5ya91pIJ3R89jXYtD4rt4yTKNT8yHtdRrJsKsmnc4a5rhGej71OScCGvfCL4Ow252k5YcQqeP95A84tcwVSJcqa4gi1Pw3PrJy%2FX48BrghdtCup7dbJvjS9V53OQ69LW8QmM0dRonoQQ7tXrwNQjkESDF27SBpvzwd9q8NOSRUqCNqxgLA0bClfoNYe7GVoaeMISQ59FOPdB%2F9%2Bliopza3xx%2FJ5URBSgWE8di6TuD2OlfSp2m70eL53mt6PR721Ky81jnSEZIJgEQ8ULCwzNtATp780uBYjxy%2FZrra3BXoHxen338WNjhDo4%2BN7ifU015nwhiS7UiAtkle%2FXTMLCfgmOo1QNYz%2BMaF9jxSW8fiog4Y7Hjp8OSKSVUsVGBLxL6tzclo9t2A2OApxND0TTvpa0MMxtKhUm1bEyiPdfWfakgAle8OAar0teWXxsnn5b5DtJmegvkzIa1%2B0MBo5LvztD9FEZuYx10NMCPNS35HjZgrSE4kVOdY8tSg3V93Utqc%2BNsUiSr1Int%2FIE8tqhblUKBSaI9Ni5bEFISS8hQ0BrbC7vRxGoUQTEKEXfHLmqynAWlzhu4b%2Fpz0QmYqkOg%2BR8wopY7JZP240mx%2B5TgYnlnMLghQztf0mRipoUxIQFMbNzLctfaTYTf38JqonmXpJ5wnLkSa8u4O0uTTHfdMFtQX1XMwwd2q6fLxUYOtUKl3n1kU9%2FrXFTXhPX9Fms4SSsJGw%2BwjsODzFi3Qog7dCV9WyzJVurcqT583NNxZfDVRtAnrIgp%2F4lZ0GwTP8GbYZPQitG3Un4Pt21Pl9gCxDA6QFjWZ%2FDOGrtr%2B6FkA; PHPSESSID=26554c3447ec7feea70eeda2cda28232; 73935d94db91e82f4bbec2322d03f55a=d6advWeQCXkmIYGSMRWnbCwnzmCRlRwzAeNjPGBQ9BdoGtegtLLJ; lastViewChannel=32209");
                .cookies(cookies);
        Connection.Response resp = conn.method(Connection.Method.GET).cookies(cookies).timeout(60*1000).execute();
        cookies.putAll(encodeAll(resp.cookies()));
        Pattern p = Pattern.compile("var pageData = .*?;\tvar extid");
        Matcher m = p.matcher(resp.body());
        String pageData = m.find() ? m.group(0) : "";
        return pageData.substring("var pageData = ".length(), pageData.length() - ";\tvar extid".length());
    }

    private static int totalPage(String json) {
        JSONObject jsonObj = JSON.parseObject(json);
        String pageMin = jsonObj.getString("page_min");
        Pattern p = Pattern.compile("</strong>/.*?&nbsp;&nbsp;");
        Matcher m = p.matcher(pageMin);
        String page = m.find() ? m.group(0) : "";
        return Integer.parseInt(page.substring("</strong>/".length(), page.length() - "&nbsp;&nbsp;".length()));
    }

    private static boolean fetchUsableData(String dataJson, SpiderJobCallable callable) throws IOException {
        System.out.println(dataJson);
        JSONObject jsonObj = JSON.parseObject(dataJson);
        JSONArray list = jsonObj.getJSONArray("list");
        for (int i = 0; i < 1; i++) {//list.size()
            JSONObject obj = list.getJSONObject(i);
            // 信息提取: id、title
            String id = obj.getString("id");
            String title = obj.getString("picture_title");
            // 信息提取: 图片
            JSONArray subsidiaries = obj.getJSONArray("subsidiary");
            List<String> pictures = new LinkedList<>();
            if (null == subsidiaries) {
                String vipPicture = obj.getString("vip_picture");
                pictures.add(vipPicture);
            } else {
                for (int j = 0; j < subsidiaries.size(); j++) {
                    JSONObject subsidiary = subsidiaries.getJSONObject(j);
                    String vipPicture = subsidiary.getString("vip_picture");
                    pictures.add(vipPicture);
                }
            }
            // 回调数据
            boolean isBreak = callable.callback(id, title, pictures);
            if (isBreak) return true;
        }
        return false;
    }

    private static Map<String, String> cookies = new HashMap<>(); // 保存cookie

    private static Map<String, String> encodeAll(Map<String, String> m) {
        Set<String> keys =  m.keySet();

        for (String key : keys) {
            m.put(key, m.get(key).replaceAll("%252","%2"));
        }
        return m;
    }

}
