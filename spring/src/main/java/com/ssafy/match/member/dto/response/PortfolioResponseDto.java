package com.ssafy.match.member.dto.response;


import com.ssafy.match.file.dto.DBFileDto;
import com.ssafy.match.member.entity.Member;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class PortfolioResponseDto {
    private DBFileDto portfolio;

    @ApiModelProperty(name = "portfolio_uri", example = "https://naver.com")
    private String portfolio_uri;

    public static PortfolioResponseDto of(Member member) {
        return PortfolioResponseDto.builder()
                .portfolio(DBFileDto.of(member.getPortfolio()))
                .portfolio_uri(member.getPortfolio_uri())
                .build();
    }

    @Builder
    public PortfolioResponseDto(DBFileDto portfolio, String portfolio_uri) {
        this.portfolio = portfolio;
        this.portfolio_uri = portfolio_uri;
    }
}
