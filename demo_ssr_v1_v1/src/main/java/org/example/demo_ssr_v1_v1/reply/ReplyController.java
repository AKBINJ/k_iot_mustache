package org.example.demo_ssr_v1_v1.reply;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.demo_ssr_v1_v1._core.errors.exception.Exception401;
import org.example.demo_ssr_v1_v1.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class ReplyController {

    private final ReplyService replyService;

    /**
     * 댓글 작성 기능 요청
     * @param saveDto
     * @param session
     * @return
     */
    @PostMapping("/reply/save")
    public String saveProc(ReplyRequest.SaveDTO saveDto, HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        saveDto.validate();
        replyService.댓글작성(saveDto, sessionUser.getId());
        return "redirect:/board/" + saveDto.getBoardId();
    }

    @PostMapping("/reply/{id}/delete")
    public String deleteProc(@PathVariable(name = "id") Long replyId, HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        Long boardId = replyService.댓글삭제(replyId, sessionUser.getId());

        // 댓글 삭제 후 게시글 상세보기 리다이렉트 처리
        return "redirect:/board/" + boardId;

    }
}
