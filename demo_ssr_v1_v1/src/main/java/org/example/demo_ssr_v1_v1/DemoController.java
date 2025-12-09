package org.example.demo_ssr_v1_v1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * temp - 화면 연결 확인용 (추후 삭제)
 * */
@Controller
public class DemoController {

    // user
    @GetMapping("/")
    public String index() {
        return "index";
    }

    //////user////
    //http://localhost:8080/join-form
    @GetMapping("/join-form")
    public String joinForm() {
        return "user/join-form";
    }

    //http://localhost:8080/login-form
    @GetMapping("/login-form")
    public String loginForm() {
        return "user/login-form";
    }

    //http://localhost:8080/update-form
    @GetMapping("/update-form")
    public String updateForm() {
        return "board/update-form";
    }


    // board
    //http://localhost:8080/board/update-form
    @GetMapping("/board/update-form")
    public String boardUpdateForm() {
        return "/board/update-form";
    }

    //http://localhost:8080/board/save-form
    @GetMapping("/board/save-form")
    public String boardSaveForm() {
        return "/board/save-form";
    }

    //http://localhost:8080/board/detail/1
    @GetMapping("/board/detail/{id}")
    public String boardDetail() {
        return "/board/detail";
    }
}
