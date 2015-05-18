package org.fiware.apps.repository.oauth2;

/*
 * #%L
 * FiwareMarketplace
 * %%
 * Copyright (C) 2014 CoNWeT Lab, Universidad Politécnica de Madrid
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of copyright holders nor the names of its contributors
 *    may be used to endorse or promote products derived from this software 
 *    without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

import java.util.logging.Level;
import java.util.logging.Logger;
import org.fiware.apps.repository.dao.impl.MongoUserDAO;
import org.fiware.apps.repository.exceptions.db.DatasourceException;
import org.fiware.apps.repository.exceptions.db.NotFoundException;
import static org.mockito.Mockito.*;

import org.fiware.apps.repository.model.User;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pac4j.core.context.WebContext;

public class FIWAREClientTest {

	@Mock private MongoUserDAO userDAO;
	@InjectMocks private FIWAREClient client = new FIWAREClient();


	@Before 
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testRequiresStateParameter() {
		assertEquals(client.requiresStateParameter(),false);
	}

	private void testExtractUserProfile(boolean userExist) {
		try {
			// This JSON simulates a response from the IdM
			String userName = "user";
			String displayName = "User Name";
			String email = "user@fiware.org";
			String json = "{\"id\":1,\"actorId\":2487,\"nickName\":\"" + userName + "\","
					+ "\"displayName\":\"" + displayName + "\",\"email\":\"" + email + "\"}";

			// Mock
                        User user = new User(userName + "_old");
			if (userExist) {
				
				when(userDAO.getUser(userName)).thenReturn(user);
			} else {
				when(userDAO.getUser(userName)).thenReturn(null, user);
			}

			// Call the function
			FIWAREProfile profile = client.extractUserProfile(json);

			// Check the profile
			assertEquals(profile.getId(),userName);
			assertEquals(profile.getDisplayName(),displayName);
			assertEquals(profile.getEmail(),email);

			// Capture the user saved in the database
			ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
			verify(userDAO).updateUser(captor.capture());

			User storedUser = captor.getValue();
			assertEquals(storedUser.getUserName(),userName);
			assertEquals(storedUser.getEmail(),email);
			assertEquals(storedUser.getPassword(),"");
		} catch (Exception ex) {
			fail("Exception " + ex + " not expected");
		}
	}

	@Test
	public void testExtractUserProfileUserExists() {
		testExtractUserProfile(true);
	}
	
	@Test
	public void testExtractUserProfileUserDoesNotExist() {
		testExtractUserProfile(false);
	}
	
	@Test
	public void testExtractUserProfileNull() {
		// Call the function
		FIWAREProfile profile = client.extractUserProfile(null);
		
		// Assertions
		assertNull(profile);
            try {
                verify(userDAO, never()).updateUser(any(User.class));
            } catch (NotFoundException | DatasourceException ex) {
                fail(ex.getLocalizedMessage());
            }
	}

	@Test
	public void testGetProfileUrl() {
		          assertEquals(client.getProfileUrl(null),"https://account.lab.fiware.org/user");
	}

	private void testHasBeenCancelled(String error, boolean cancelled) {
		WebContext context = mock(WebContext.class);
		when(context.getRequestParameter(anyString())).thenReturn(error);
		assertEquals(client.hasBeenCancelled(context),cancelled);
	}

	@Test
	public void testCancelled() {
		testHasBeenCancelled("access_denied", true);
	}

	@Test
	public void testNotCancelled() {
		testHasBeenCancelled("internal_error", false);
	}

	@Test
	public void testNewClient() {
		assertEquals(client.newClient().getClass(),FIWAREClient.class);
	}
}
