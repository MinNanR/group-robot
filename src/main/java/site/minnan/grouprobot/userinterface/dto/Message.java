package site.minnan.grouprobot.userinterface.dto;

import cn.hutool.json.JSONObject;
import lombok.Data;

/**
 * 消息参数
 *
 * @author Minnan on 2022/02/17
 */
@Data
public class Message {

    private String raw_message;

    private Long self_id;

    private String message_id;

    private String message_type;

    private Long group_id;

    private Sender sender;

    private String sub_type;

    private String user_id;

    private JSONObject anonymous;

    private String post_type;

    private Long time;

    private Long message_seq;

    private Integer font;

    private Long interval;

    private String meta_event_type;

    private MessageStatus status;
}
