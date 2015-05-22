package org.fiware.apps.repository.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user")
public class User {

	public User(String username) {
		super();
		this.userName = username;
	}

	private String userName;
        private String displayName;
        private String email;
	private String password;
        private String token;

	@XmlElement
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

        @XmlElement
        public String getDisplayName() {
            return displayName;
        }
        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        @XmlElement
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }

	@XmlElement
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	@XmlElement
        public String getToken() {
            return token;
        }
        public void setToken(String token) {
            this.token = token;
        }
}