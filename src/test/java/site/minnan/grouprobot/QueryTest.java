package site.minnan.grouprobot;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.minnan.grouprobot.application.service.QueryService;
import site.minnan.grouprobot.userinterface.dto.Message;

import java.io.File;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = GroupRobotApplication.class)
public class QueryTest {

    @Autowired
    private QueryService queryService;

    static final HttpCookie cookie = new HttpCookie("isolate-web-session-id", "cdf4857f-b45f-406e-94a6-2ee1cb4480c9");

    static final String userAgent = "ozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.0 Mobile/14E304 Safari/602.1 Edg/98.0.4758.102";

    static final String reservePageUrl = "https://hk.sz.gov.cn:8118/passInfo/confirmOrder?checkinDate=&t=&s=";

    @Test
    public void testQueryByName() {
        Message message = new Message();
        message.setGroup_id(123L);
        message.setRaw_message("查询MinnanLum");
        queryService.queryByName(message);
    }

    @Test
    public void testLogin() {
        String url = "https://hk.sz.gov.cn:8118/user/login";
        Map<String, Object> form = new HashMap<>();
        form.put("certType", 2);
        form.put("certNo", "QzMzMzk2NDY5");
        form.put("verifyCode", "H7VYAL");
        form.put("pwd", "YWFiOTRkODExNDI0Mjg4MzdiOTNmZjgyNzZlM2IwNzQ=");
        HttpResponse execute = HttpUtil.createPost(url)
                .cookie(cookie)
                .form(form)
                .execute();
        System.out.println(execute.body());
    }

    @Test
    public void testGetList(){
        String url = "https://hk.sz.gov.cn:8118/districtHousenumLog/getList";
        HttpResponse execute = HttpUtil.createPost(url)
                .cookie(cookie)
                .execute();
        System.out.println(execute.body());
    }

    @Test
    public void testGetVerifyCode() {
        String url = "https://hk.sz.gov.cn:8118/user/getVerify?0.1450774746151562";
        HttpResponse execute = HttpUtil.createGet(url)
                .header("user-agent", "ozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.0 Mobile/14E304 Safari/602.1 Edg/98.0.4758.102")
                .cookie(cookie)
                .execute();
        execute.writeBody(FileUtil.touch("D:\\code\\jiankangyizhan\\spider\\1.png"));
    }

    @Test
    public void testReserve(){
        String url = "https://hk.sz.gov.cn:8118/reserveHouse";
        HttpResponse execute = HttpUtil.createPost(url)
                .cookie(cookie)
                .execute();
        System.out.println(execute.getStatus());
    }
}
