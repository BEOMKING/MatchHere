package com.ssafy.match.invite.service;

import com.ssafy.match.invite.dto.InviteLinkResponseDto;

public interface LinkService {

    InviteLinkResponseDto makeInviteLink(String category, Long projectId);
}
