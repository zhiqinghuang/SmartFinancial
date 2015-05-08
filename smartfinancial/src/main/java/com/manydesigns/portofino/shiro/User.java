package com.manydesigns.portofino.shiro;

import com.manydesigns.elements.annotations.Password;
import com.manydesigns.elements.annotations.Required;

public class User {

	@Required
	public String username;

	@Required
	public String email;

	@Required
	@Password(confirmationRequired = true)
	public String password;

}