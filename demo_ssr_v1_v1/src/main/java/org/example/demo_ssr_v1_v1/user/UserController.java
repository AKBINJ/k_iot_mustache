package org.example.demo_ssr_v1_v1.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.demo_ssr_v1_v1._core.errors.exception.Exception403;
import org.example.demo_ssr_v1_v1._core.errors.exception.Exception404;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 사용자 Controller (표현 계층)
 *  핵심 개념 :
 *  - HTTP 요청을 받아서 처리
 *  - 요청 데이터 검증 및 파라미터 바인딩
 *  - Service 레이어에 비즈니스 로직을 위임
 *  - 응답 데이터를 View에 전달 함
 */

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    // 회원 정보 수정 화면 요청
    // http://localhost:8080/user/update
    @GetMapping("/user/update")
    public String updateForm(Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        User user = userService.회원정보수정화면(sessionUser.getId());
        model.addAttribute("user", user);
        return "user/update-form";
    }

    // 회원 정보 수정 기능 요청 - 더팅 체킹
    // http://localhost:8080/user/update
    @PostMapping("/user/update")
    public String updateProc(UserRequest.UpdateDTO updateDTO, HttpSession session) {
//        // 1. 인증 검사
//        User sessionUser = (User) session.getAttribute("sessionUser");
//        // LoginInterceptor
//
//        // 인가 처리 (DB 정보 조회)
//        User user = userRepository.findById(sessionUser.getId());
//        if(user == null) {
//            throw new Exception404("사용자를 찾을 수 없습니다.");
//        }
//
//        if(!user.isOwner(sessionUser.getId()) == false) {
//            throw new Exception403("회원 정보 수정 권한이 없습니다.");
//        }
//
//        // 2. 유효성 검사
//        // 3. 세션 메모리에 있던 기존 상태값을 변경 처리
//        try {
//            updateDTO.validate();
//            User updateUser = userRepository.updateById(sessionUser.getId(), updateDTO);
//            // 세션에 정보 갱신
//            session.setAttribute("sessionUser", updateUser);
//            // 수정 후 리다이렉트 처리 - 게시판 목록으로 이동
//            return "redirect:/";
//        } catch (Exception e) {
//            return "user/update-form";
//        }

        User sessionUser = (User) session.getAttribute("sessionUser");

        try {
            // 유효성 검사 (형식 검사)
            updateDTO.validate();
            User updateUser = userService.회원정보수정(updateDTO, sessionUser.getId());
            // 회원 정보 수정은 세션 갱신해주어야 한다.
            session.setAttribute("sessionUser", updateUser);
            return "redirect:/";
        } catch (RuntimeException e) {
            return "user/update-form";
        }
    }

    // 로그아웃 기능 요청
    // http://localhost:8080/logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {

        // 세션 무효화
        session.invalidate();

        return "redirect:/";
    }

    // 로그인 화면 요청
    // http://localhost:8080/login
    @GetMapping("/login")
    public String loginForm() {

        return "user/login-form";
    }

    // JWT 토큰 기반 인증 X -> 세션 기반 인증 처리
    // 로그인 기능 요청
    // http://localhost:8080/login
    @PostMapping("/login")
    public String loginProc(UserRequest.LoginDTO loginDTO, HttpSession session) {
        // 1. 인증 검사 X - 로그인 요청
        // 2. 유효성 검사
        // 3. db 에 사용자 이름과 비밀번호 확인
        // 4. 로그인 성공 또는 실패 처리
        // 5. 웹 서버는 사용자의 정보를 세션 메모리에 저장시켜야
        //      다음 번 요청이 오더라도 알 수 있다 - 세션 저장 처리
        try {
            // 유효성 검사
            loginDTO.validate();
            User sessionUser = userService.로그인(loginDTO);
            session.setAttribute("sessionUser", sessionUser);
            return "redirect:/";

        } catch (Exception e) {
            // 로그인 실패 시 다시 로그인 화면으로 처리
            return "user/login-form";
        }
    }


    // 회원가입 화면 요청
    // http://localhost:8080/join
    @GetMapping("/join")
    public String joinFrom() {
        return "user/join-form";
    }

    // 회원가입 기능 요청
    // http://localhost:8080/join
    @PostMapping("/join")
    public String joinProc(UserRequest.JoinDTO joinDTO) {

        // 1. 인증검사 (X) - 필요 없음
        // 2. 유효셩 검사 (엉망인 데이터를 저장할 수 없음
        // 3. 사용자 이름 중복 체크
        // 4. 처저아 요ㅓㅊㅇ

        joinDTO.validate();
        userService.회원가입(joinDTO);
        return "redirect:/login";
    }
}
