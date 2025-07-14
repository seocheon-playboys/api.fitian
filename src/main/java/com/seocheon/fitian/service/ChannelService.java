package com.seocheon.fitian.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.cloud.firestore.Firestore;
import com.seocheon.fitian.dto.channel.ChannelCreateRequestDto;
import com.seocheon.fitian.dto.channel.ChannelListResponseDto;
import com.seocheon.fitian.dto.channel.ChannelParticipantResponseDto;
import com.seocheon.fitian.dto.channel.ChannelResponseDto;
import com.seocheon.fitian.mapper.ChannelMapper;
import com.seocheon.fitian.mapper.ChannelParticipantMapper;
import com.seocheon.fitian.mapper.MemberMapper;
import com.seocheon.fitian.model.ChannelModel;
import com.seocheon.fitian.model.ChannelParticipantModel;
import com.seocheon.fitian.model.MemberModel;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChannelService {

	private final MemberMapper memberMapper;
	private final ChannelMapper channelMapper;
	private final ChannelParticipantMapper participantMapper;
	private final Firestore firestore;
	
	public ChannelResponseDto createChannel(ChannelCreateRequestDto req, MemberModel creator) {
		if(channelMapper.selectChannelById(req.getBoxCode(), req.getChannelId()) != null) {
			throw new IllegalArgumentException("이미 존재하는 채널 ID 입니다.");
		}
		
		ChannelModel model = new ChannelModel();
		model.setBoxCode(req.getBoxCode());
		model.setChannelId(req.getChannelId());
		model.setChannelName(req.getChannelName());
		model.setType(req.getType());
		model.setCreatedBy(creator.getUid());
		model.setCreatedAt(LocalDateTime.now());

		if(req.getType().equals("public") || req.getType().equals("notice")) {//채널 타입이 public, notice 일 경우 해당 박스의 모든 멤버.
			creator.setRank(null);			
			List<MemberModel> memberList = memberMapper.getAllMember(creator); //creator가 manager,owner 일때만 작동
			for(MemberModel m : memberList) {
				req.getMemberUids().add(m.getUid());
			}
		} else { // public, notice 일 경우 참여자 목록에 채널 생성자 uid 추가
			req.getMemberUids().add(creator.getUid());
		}
		
		channelMapper.insertChannel(model);
		
		for(String uid : req.getMemberUids()) {
			ChannelParticipantModel p = new ChannelParticipantModel();
			p.setBoxCode(req.getBoxCode());
			p.setChannelId(req.getChannelId());
			p.setUid(uid);
			participantMapper.insertParticipant(p);
		}
		
		try {
			Map<String, Object> data = new HashMap<>();
			data.put("channelName", req.getChannelName());
			data.put("type", req.getType());
			data.put("memberUids", req.getMemberUids());
			data.put("createdBy",creator.getUid());
			data.put("createdAt",com.google.cloud.Timestamp.now());
			
			firestore.collection("boxes")
					.document(req.getBoxCode())
					.collection("channels")
					.document(req.getChannelId())
					.set(data);
		} catch (Exception e) {
			throw new RuntimeException("Firestore 채널 문서 생성 실패", e);
		}
		
		return ChannelResponseDto.builder()
				.boxCode(req.getBoxCode())
				.channelId(req.getChannelId())
				.channelName(req.getChannelName())
				.memberUids(req.getMemberUids())
				.build();
	}
	
	public List<ChannelListResponseDto> getChannelList(String boxCode, String uid) {
		List<ChannelModel> models = channelMapper.selectChannelsByUid(boxCode, uid);		
		return models.stream()
				.map(m -> ChannelListResponseDto.builder()
						.channelId(m.getChannelId())
						.channelName(m.getChannelName())
						.type(m.getType())
						.build())
				.toList();
	}
	
	public List<ChannelListResponseDto> getChannelListByType(String boxCode, String type) {
		List<ChannelModel> models = channelMapper.selectChannelsByType(boxCode, type);		
		return models.stream()
				.map(m -> ChannelListResponseDto.builder()
						.channelId(m.getChannelId())
						.channelName(m.getChannelName())
						.type(m.getType())
						.build())
				.toList();
	}
	
	public List<ChannelParticipantResponseDto> getChannelParticipants(String boxCode, String channelId) {
		
		return participantMapper.selectUidsByBoxAndChannel(boxCode, channelId);
	}
	
	@Transactional
	public void inviteParticipant(String boxCode, String channelId, String uid, String inviterUid) {
		List<String> existing = participantMapper.selectExistingUids(boxCode, channelId);
		
		if(existing.contains(uid)) {
			return;
		}
		
		ChannelParticipantModel p = new ChannelParticipantModel();
		p.setBoxCode(boxCode);
		p.setChannelId(channelId);
		p.setUid(uid);
		participantMapper.insertParticipant(p);
		
		try {
			firestore.collection("boxes")
					.document(boxCode)
					.collection("channels")
					.document(channelId)
					.update("memberUids", com.google.cloud.firestore.FieldValue.arrayUnion(uid));
		} catch (Exception e) {
			throw new RuntimeException("Firestore 참여자 추가 실패", e);
		}
	}
	
	@Transactional
	public void inviteParticipants(String boxCode, String channelId, List<String> newUids, String inviterUid) {
		List<String> existing = participantMapper.selectExistingUids(boxCode, channelId);
		
		List<String> toInsert = newUids.stream()
				.filter(uid -> !existing.contains(uid))
				.toList();
		
		for(String uid : toInsert) {
			ChannelParticipantModel p = new ChannelParticipantModel();
			p.setBoxCode(boxCode);
			p.setChannelId(channelId);
			p.setUid(uid);
			participantMapper.insertParticipant(p);
		}
		
		try {
			firestore.collection("boxes")
					.document(boxCode)
					.collection("channels")
					.document(channelId)
					.update("memberUids", com.google.cloud.firestore.FieldValue.arrayUnion(toInsert.toArray()));
		} catch (Exception e) {
			throw new RuntimeException("Firestore 참여자 추가 실패", e);
		}
	}
	
	public void deleteParticipant(String boxCode, String channelId, String targetUid) {
		participantMapper.deleteParticipant(boxCode, channelId, targetUid);
		
		try {
			firestore.collection("boxes")
					.document(boxCode)
					.collection("channels")
					.document(channelId)
					.update("memberUids", com.google.cloud.firestore.FieldValue.arrayRemove(targetUid));
		} catch (Exception e) {
			throw new RuntimeException("Firestore 참여자 제거 실패", e);
		}
	}
	
	public boolean canEnterChannel(String boxCode, String channelId, String uid) {
		return participantMapper.isParticipant(boxCode, channelId, uid);
	}
	
	public void deleteChannel(String boxCode, String channelId) {
		firestore.collection("boxes")
			.document(boxCode)
			.collection("channels")
			.document(channelId)
			.delete();
		
		channelMapper.deleteChannel(boxCode, channelId);
		participantMapper.deleteAllParticipant(boxCode, channelId);
		
	}
}
