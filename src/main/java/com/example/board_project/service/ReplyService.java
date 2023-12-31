package com.example.board_project.service;


import com.example.board_project.config.auth.PrincipalDetail;
import com.example.board_project.dto.*;
import com.example.board_project.entity.Board;
import com.example.board_project.entity.Reply;
import com.example.board_project.entity.User;
import com.example.board_project.repository.BoardRepository;
import com.example.board_project.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;

    public ReplyService(ReplyRepository replyRepository, BoardRepository boardRepository) {
        this.replyRepository = replyRepository;
        this.boardRepository = boardRepository;
    }

    public void join(ReplySaveDto replySaveDto , @AuthenticationPrincipal PrincipalDetail principalDetail) {
        Board board = boardRepository.findById(replySaveDto.getBoardId()).orElse(null);
        User user = principalDetail.getUser();
     //   assert board != null;   //board가 null이면 오류를 찾아내기위한 assert문
        board.setHit(board.getHit());
        Reply reply = Reply.builder()
                .rootReply(true)
                .parent(null)
                .content(replySaveDto.getContent())
                .user(user)
                .board(board)
                .child(new ArrayList<>())
                .build();
        replyRepository.save(reply);
    }

    public void deleteById(int replyId) {
        replyRepository.deleteById(replyId);
    }


    public void modify(ReplyModifyDto replyModifyDto) {
        Reply reply = replyRepository.findById(replyModifyDto.getReplyId()).get();
        reply.setContent(replyModifyDto.getReplyContent());
    }


    public List<ReplyCommentDto> findByBoardId(int boardId) {
        List<Reply> replyList = replyRepository.findByBoardId(boardId);
        List<ReplyCommentDto> replyCommentDtoList = new ArrayList<>();
        for (Reply reply : replyList) {
            ReplyCommentDto replyCommentDto = ReplyCommentDto.replyToReplyCommentDto(reply);
            replyCommentDtoList.add(replyCommentDto);
        }

        return replyCommentDtoList;
    }
    //대댓글 전용
//    public List<ReplyCommentDto> CommentReplyFindByBoardId(int boardId) {
//        List<Reply> replyList = replyRepository.findByBoardId(boardId);
//        List<ReplyCommentDto> replySelectDtoList = new ArrayList<>();
//        for (Reply reply : replyList) {
//            ReplyCommentDto replyCommentDto = ReplyCommentDto.replyToReplyCommentDto(reply);
//            replySelectDtoList.add(replyCommentDto);
//        }
//        return replySelectDtoList;
//    }

    public  List<ReplySelectDto> findAll() {
        List<Reply> replyList = replyRepository.findAll();
        List<ReplySelectDto> replySelectDtoList = new ArrayList<>();
        for (Reply reply : replyList) {
            replySelectDtoList.add(ReplySelectDto.replyToReplySelectDto(reply));
        }
        return replySelectDtoList;
    }

    public List<ReplyCommentDto> commentReply(ReplyDto replyDto,@AuthenticationPrincipal PrincipalDetail principalDetail) {
        Board board = boardRepository.findById(replyDto.getBoardId()).orElse(null);
        Reply parent = replyRepository.findById(replyDto.getParentId()).orElse(null);
        Reply reply = Reply.builder()
                .rootReply(false)
                .child(new ArrayList<>())
                .parent(parent)
                .board(board)
                .content(replyDto.getContent())
                .user(principalDetail.getUser())
                .build();
        replyRepository.save(reply);
        return findByBoardId(board.getId());
    }
}
