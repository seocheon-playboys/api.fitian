package com.seocheon.fitian.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seocheon.fitian.auth.security.CustomUserDetails;
import com.seocheon.fitian.dto.BoxSummaryDto;
import com.seocheon.fitian.dto.UpdateBoxRequest;
import com.seocheon.fitian.dto.channel.ChannelCreateRequestDto;
import com.seocheon.fitian.mapper.BoxMapper;
import com.seocheon.fitian.mapper.MemberMapper;
import com.seocheon.fitian.model.BoxModel;
import com.seocheon.fitian.model.MemberModel;
import com.seocheon.fitian.model.ResponseModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoxService {

	private final BoxMapper mapper;
	private final MemberMapper memberMapper;
	private final ChannelService channelService;
	
	public BoxModel getBoxByCode(String boxCode) {
		
		return mapper.getBoxByCode(boxCode);
	}
	
	public List<BoxSummaryDto> getBoxList() {
		
		return mapper.getBoxList();
	}
	
	@Transactional
	public BoxModel createBox(BoxModel model, CustomUserDetails userDetails) {

		mapper.createBox(model);
		
		MemberModel owner = userDetails.getMember();
		owner.setRank("owner");
		owner.setBoxCode(model.getBoxCode());
		memberMapper.updateMember(owner);
		
		BoxModel box = mapper.getBoxByCode(model.getBoxCode());
		
		ChannelCreateRequestDto req = new ChannelCreateRequestDto();

    	List<String> members = List.of(userDetails.getUsername());    	
    	req.setBoxCode(model.getBoxCode());
    	req.setChannelId(req.getBoxCode()+"_general");
    	req.setChannelName("general");
    	req.setType("public");
    	req.setMemberUids(members);
    	
    	log.info("Channel DTO = {}",req);
    	channelService.createChannel(req,owner);
		
		return box;
	}
	
	@Transactional
	public BoxModel updateBox(UpdateBoxRequest request, String boxCode) {
		
		BoxModel box = mapper.getBoxByCode(boxCode);
		
		if (request.getBoxContact() != null && !request.getBoxContact().isBlank()) {
	        box.setBoxContact(request.getBoxContact());
	    }
		if (request.getBoxAddress() != null && !request.getBoxAddress().isBlank()) {
	        box.setBoxAddress(request.getBoxAddress());
	    }
		if (request.getBoxScript() != null && !request.getBoxScript().isBlank()) {
	        box.setBoxScript(request.getBoxScript());
	    }
		if (request.getBoxInsta() != null && !request.getBoxInsta().isBlank()) {
	        box.setBoxInsta(request.getBoxInsta());
	    }
		if (request.getBoxFeeUrl() != null && !request.getBoxFeeUrl().isBlank()) {
	        box.setBoxFeeUrl(request.getBoxFeeUrl());
	    }
		if (request.getBoxTimeTableUrl() != null && !request.getBoxTimeTableUrl().isBlank()) {
	        box.setBoxTimeTableUrl(request.getBoxTimeTableUrl());
	    }
		
		int updatedRows = mapper.updateBox(box);
		
		if(updatedRows == 0) {
			throw new IllegalStateException("업데이트에 실패했습니다.");
		}
		
		return box;
	}
	
	@Transactional
	public ResponseModel deleteBox(BoxModel model) {
		ResponseModel res = new ResponseModel();
		
		mapper.deleteBox(model);
		
		res.setMessage("박스를 삭제했습니다.");
		
		return res;
	}
}
