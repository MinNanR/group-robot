package site.minnan.grouprobot.userinterface.fascade;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.minnan.grouprobot.application.service.QueryService;
import site.minnan.grouprobot.userinterface.dto.Message;

@Slf4j
@RestController
public class MsgController {

    @Autowired
    private QueryService queryService;

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
}
