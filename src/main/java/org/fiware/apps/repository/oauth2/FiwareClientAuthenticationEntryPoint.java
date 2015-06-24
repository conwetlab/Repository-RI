package org.fiware.apps.repository.oauth2;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.pac4j.core.client.Client;
import org.pac4j.core.context.J2EContext;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.RequiresHttpAction;
import org.pac4j.core.util.CommonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public final class FiwareClientAuthenticationEntryPoint implements AuthenticationEntryPoint, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(FiwareClientAuthenticationEntryPoint.class);

    private Client client;

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response,
            final AuthenticationException authException) throws IOException, ServletException {
        logger.debug("client : {}", this.client);
        final WebContext context = new J2EContext(request, response);


        try {
            if (isBrowser(MediaType.parseMediaTypes(request.getHeader("Accept")))) {
                this.client.redirect(context, true, false);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
            }
        } catch (final RequiresHttpAction e) {
            logger.debug("extra HTTP action required : {}", e.getCode());
        }
    }

    public void afterPropertiesSet() throws Exception {
        CommonHelper.assertNotNull("client", this.client);
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(final Client client) {
        this.client = client;
    }

    private boolean isBrowser(List <MediaType> accept) {
        for (MediaType media : accept) {
            String mediaString = media.getType()+"/"+media.getSubtype();
            if(mediaString.equals("text/html") ||
                mediaString.equals("application/xhtml+xml") ||
                mediaString.equals("imagen/webp")) {
                return true;
            }
        }
        return false;
    }
}
