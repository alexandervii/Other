package cn.com.alex.server.inter;

import cn.com.alex.server.domain.Person;

interface IDraftTools {

	/** 根据编号获取内容*/
	String getDraft(String draftId);
	/** 设置内容*/
	void setDraft(String draftText);
	/** 设置人信息*/
	void setPerson(in Person person);
}
