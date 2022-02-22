package site.minnan.grouprobot.userinterface.fascade;

import cn.hutool.core.io.IoUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.minnan.grouprobot.application.service.QueryService;
import site.minnan.grouprobot.infrastrucutre.util.RedisUtil;
import site.minnan.grouprobot.userinterface.dto.Message;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Controller
public class MsgController {

    @Autowired
    private QueryService queryService;

    @Autowired
    private RedisUtil redisUtil;

    private static final String messageUrl = "http://127.0.0.1:5700/send_group_msg";

    @PostMapping("onMessage")
    public void onMessage(@RequestBody Message message) {
        if ("heartbeat".equals(message.getMeta_event_type())) {
            log.info("心跳消息，心跳时间：{}", message.getTime());
        } else {
            log.info(JSONUtil.toJsonStr(message));
            queryService.queryByName(message);
        }
    }

    @PostMapping(value = "/districtHousenumLog/getList", produces = "application/json")
    @ResponseBody
    public void getList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = "https://hk.sz.gov.cn:8118/districtHousenumLog/getList";
        HttpRequest post = HttpUtil.createPost(url);
        Cookie[] cookies = request.getCookies();
        if (cookies !=null && cookies.length > 0) {
            List<HttpCookie> httpCookies = Stream.of(cookies).map(e -> new HttpCookie(e.getName(), e.getValue())).collect(Collectors.toList());
            post.cookie(httpCookies);
        }
        HttpResponse execute = post.execute();
        if (execute.getStatus() != 200) {
            response.sendError(execute.getStatus());
        } else {
            String responseBody = execute.body();
            JSONObject object = JSONUtil.parseObj(responseBody);
            JSONArray data = object.getJSONArray("data");
            JSONObject lastItem = data.getJSONObject(data.size() - 1);
            if (lastItem.getInt("count") == 0) {
                lastItem.set("count", 10);
            }
            ServletOutputStream os = response.getOutputStream();
            os.write(data.toString().getBytes());
        }
//        return "{\"status\":200,\"msg\":\"成功\",\"data\":[{\"id\":0,\"date\":\"2022-02-22\",\"count\":0,\"total\":825,\"timespan\":1645530242193,\"sign\":\"fca73351f16db8bdd9975d9e457d55b8\"},{\"id\":0,\"date\":\"2022-02-23\",\"count\":0,\"total\":810,\"timespan\":1645530242193,\"sign\":\"c9f28c5f752cfb9ba72cc49d1c6ceab6\"},{\"id\":0,\"date\":\"2022-02-24\",\"count\":0,\"total\":800,\"timespan\":1645530242194,\"sign\":\"f5678702ad892657d02fb8e8a3180252\"},{\"id\":0,\"date\":\"2022-02-25\",\"count\":0,\"total\":800,\"timespan\":1645530242194,\"sign\":\"16041fcb10c270a6c23cfbf305200258\"},{\"id\":0,\"date\":\"2022-02-26\",\"count\":0,\"total\":802,\"timespan\":1645530242194,\"sign\":\"b40aa5396c524c32210d01a745143660\"},{\"id\":0,\"date\":\"2022-02-27\",\"count\":0,\"total\":800,\"timespan\":1645530242195,\"sign\":\"e676a82428ce73fb10371ef2a9fdd486\"},{\"id\":0,\"date\":\"2022-02-28\",\"count\":10,\"total\":700,\"timespan\":1645530242195,\"sign\":\"40896389ed90f2d06a4d475cd0e2f4fc\"}]}";
    }

    @GetMapping("/passInfo/userCenterIsCanReserve")
    public void detailPage(HttpServletResponse response) throws IOException {
        log.info("/passInfo/userCenterIsCanReserve");
        classResource("/static/detail.html", response.getOutputStream());
        String token = (String) redisUtil.getValue("isolate-web-session-id");
        response.addCookie(new Cookie("isolate-web-session-id",  token));
    }

    @GetMapping("/js/dict/reserve.js")
    public void reservejs(HttpServletResponse response) throws IOException {
        classResource("/static/reserve.js", response.getOutputStream());
    }

    @GetMapping("/css/m_n.min.css")
    public void css(HttpServletResponse response) throws IOException {
        classResource("static/m_n.min.css", response.getOutputStream());
    }

    @GetMapping("/js/dict/need/layer.css")
    public void layer(HttpServletResponse response) throws IOException {
        classResource("static/layer.css", response.getOutputStream());
    }

    public void classResource(String path, OutputStream os) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        IoUtil.copy(resource.getInputStream(), os);
    }

    @RequestMapping("/**")
    public void fallback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String method = request.getMethod();
        String baseUrl = "https://218.17.85.229:8118";
        HttpRequest httpRequest;
        if ("GET".equalsIgnoreCase(method)) {
            String queryString = Optional.ofNullable(request.getQueryString()).map(e -> "?" + e).orElse("");
            httpRequest = HttpUtil.createGet(baseUrl + request.getRequestURI() + queryString);

        } else {
            httpRequest = HttpUtil.createPost(baseUrl + request.getRequestURI());
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String parameterName = parameterNames.nextElement();
                httpRequest.form(parameterName, request.getParameter(parameterName));
            }
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            List<HttpCookie> httpCookies = Stream.of(cookies).map(e -> new HttpCookie(e.getName(), e.getValue())).collect(Collectors.toList());
            httpRequest.cookie(httpCookies);
        }
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            httpRequest.header(headerName, request.getHeader(headerName));
        }

        HttpResponse midResponse = httpRequest.execute();
        IoUtil.copy(midResponse.bodyStream(), response.getOutputStream());

        for (HttpCookie cookie : midResponse.getCookies()) {
            response.addCookie(new Cookie(cookie.getName(), cookie.getValue()));
        }
        midResponse.headers().forEach((name, value) -> {
            String s = String.join(",", value);
            response.addHeader(name, s);
        });


    }
}
