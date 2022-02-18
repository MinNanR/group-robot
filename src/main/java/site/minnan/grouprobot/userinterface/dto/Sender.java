package site.minnan.grouprobot.userinterface.dto;

import lombok.Data;

/**
 * 消息发送者信息
 *
 * @author Minnan on 2022/02/17
 */
@Data
public class Sender {

    private String area;

    private String role;

    private String level;

    private String sex;

    private String title;

    private String user_id;

    private String nickname;

    private Integer age;

    private String card;
}
