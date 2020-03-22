package com.hua.dto;

import com.hua.model.User;
import lombok.Data;

@Data
public class QuesstionDTO {
    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer commontCount;
    private Integer likeCount;
    private User user;
}
