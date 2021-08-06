package com.argus.shared;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface DbUserDataProperties extends PropertyAccess<DbUserData> {

	@Path("id")
	ModelKeyProvider<DbUserData> id();
	@Path("KEY")
	ValueProvider<DbUserData, Integer> KEY();
	ValueProvider<DbUserData, String>  UserName();
	ValueProvider<DbUserData, String>  EmailAddress();
	ValueProvider<DbUserData, String>  AccountType();
	ValueProvider<DbUserData, String>  RunOwner();
	ValueProvider<DbUserData, String>  HomeFolder();

}