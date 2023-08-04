package com.example.board_project.dto;

import com.example.board_project.entity.Board;
import com.example.board_project.entity.Reply;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ReplyDto { //오직 Reply의 정보만 갖고있다.

    private int id;

    private String content;

    private int userId;

    private int boardId;

    private int parentId;

    private List<Integer> childId;

    private Timestamp createTime;

    static public ReplyDto replyToReplyDto(Reply reply) {
        List<Integer> integerList = new ArrayList<>();
        List<Reply> child = reply.getChild();
        for (Reply reply1 : child) {
            integerList.add(reply1.getId());
        }
        return ReplyDto.builder()
                .id(reply.getId())
                .content(reply.getContent())
                .userId(reply.getUser().getId())
                .boardId(reply.getBoard().getId())
                .createTime(reply.getCreateTime())
                .parentId(reply.getParent().getId())
                .childId(integerList)
                .build();
    }
}
