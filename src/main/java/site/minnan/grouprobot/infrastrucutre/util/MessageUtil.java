package site.minnan.grouprobot.infrastrucutre.util;

import cn.hutool.http.HttpUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息回复工具类
 *
 * @author Minnan on 2022/02/17
 */
@Component
public class MessageUtil {

    private static final String messageUrl = "http://127.0.0.1:5700/send_group_msg";

    private static final String MESSAGE_TYPE_GROUP = "group";

    private static final String MESSAGE_TYPE_PRIVATE = "private";

    /**
     * 发送群消息
     *
     * @param groupId 群号
     * @param messageContent 消息内容
     */
    public void sendGroupText(Long groupId, String messageContent) {
        Map<String, Object> content = new HashMap<>();
        content.put("group_id", groupId);
        content.put("message_type", MESSAGE_TYPE_GROUP);
        content.put("message", messageContent);
        content.put("auto_escape", true);
        HttpUtil.createPost(messageUrl)
                .form(content)
                .execute();
    }

    /**
     * 发送群消息，解析cq码
     *
     * @param groupId   群号
     * @param cqContent 内容
     */
    public void sendGroupCQ(Long groupId, String cqContent) {
        Map<String, Object> content = new HashMap<>();
        content.put("group_id", groupId);
        content.put("message_type", MESSAGE_TYPE_GROUP);
        content.put("message", cqContent);
        content.put("auto_escape", false);
        HttpUtil.createPost(messageUrl)
                .form(content)
                .execute();
    }

}
