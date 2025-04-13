package com.seocheon.fitian.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonModel {

	private int id;
	private String title;
	private String language;
	private String content;
	private List<CommonModel> commonModelList;
}
