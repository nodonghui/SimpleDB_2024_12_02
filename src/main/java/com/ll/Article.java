package com.ll;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class Article {

    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String title;
    private String body;
    private Boolean isBlind;

    public  Article() {
        this.id=0l;
        this.createdDate=LocalDateTime.now();
        this.modifiedDate=LocalDateTime.now();
        this.title="";
        this.body="";
        this.isBlind=false;
    }

    public Article(Long id,LocalDateTime c,LocalDateTime m, String title, String body, Boolean isBlind) {
        this.id=id;
        this.createdDate=c;
        this.modifiedDate=m;
        this.title=title;
        this.body=body;
        this.isBlind=isBlind;
    }

    public Boolean isBlind() {
        return this.isBlind;
    }

}
