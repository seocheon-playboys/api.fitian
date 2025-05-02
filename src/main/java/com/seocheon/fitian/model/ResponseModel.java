package com.seocheon.fitian.model;

import java.util.List;

import com.seocheon.fitian.auth.dto.JwtTokenPair;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseModel {

	private MemberModel MemberModel;
	private List<MemberModel> MemberModelList;
	private WodModel wodModel;
	private BoxModel boxModel;
	private List<BoxModel> boxModelList;
	private RecordModel recordModel;
	private List<RecordModel> recordModelList;
	private ArticleModel articleModel;
	private List<ArticleModel> articleModelList;
	private List<CommonModel> commonModelList;
	private FCMtokenModel FCMtokenModel;
	private List<FCMtokenModel> FCMtokenModelList;
	private String[] BoxCodeList;
	private String message;
	private JwtTokenPair jwtTokenPair;
}
