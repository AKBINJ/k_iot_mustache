package org.example.demo_ssr_v1_v1._api;

import lombok.Builder;
import lombok.Data;

@Data
// 요청할 때 보내줘야 하는 데이터 RequestDto 임
public class Post {

//    {
//        id: 101,
//        title: 'foo',
//        body: 'bar',
//        userId: 1
//    }

    private Integer id;
    private String title;
    private String body;
    private Integer userId;

    @Builder
    public Post(Integer id, String title, String body, Integer userId) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.userId = userId;
    }
}
