package com.ssafy.match.file.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "파일 정보", description = "파일 업로드 후 DB에 저장되는 정보 Response Dto Class\n"
    + "파일 uuid\n"
    + "파일명\n"
    + "프로젝트 주소 + 호출 uri + 파일 uuid 를 통해 uri를 클릭하면 바로 다운로드(pdf 같은)\n"
    + "파일 타입\n"
    + "파일 size(바이트)\n")
public class UploadFileResponse {

    @ApiModelProperty(name = "Id", example = "97534f05-7e7f-425d-ac3e-aae8acee8a42")
    private String Id;

    @ApiModelProperty(name = "fileName", example = "파일명")
    private String fileName;

    @ApiModelProperty(name = "fileDownloadUri", example = "http://localhost:8080/api/downloadFile/97534f05-7e7f-425d-ac3e-aae8acee8a42")
    private String fileDownloadUri;

    @ApiModelProperty(name = "fileType", example = "image/png")
    private String fileType;

    @ApiModelProperty(name = "size", example = "33414")
    private long size;

}
