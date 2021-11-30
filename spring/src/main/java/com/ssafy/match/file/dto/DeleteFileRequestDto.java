package com.ssafy.match.file.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value = "파일 삭제 요청", description = "사진, 포폴 변경할 때 기존것을 삭제하기 위한 Request Dto Class")
public class DeleteFileRequestDto {

    @ApiModelProperty(example = "스터디, 프로젝트, 클럽의 id")
    @NotNull
    private Long id;
//
//    @ApiModelProperty(example = "97534f05-7e7f-425d-ac3e-aae8acee8a42")
//    @NotNull
//    private String uuid;

}
