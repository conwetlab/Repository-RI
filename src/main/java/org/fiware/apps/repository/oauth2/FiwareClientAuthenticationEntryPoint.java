package org.fiware.apps.repository.oauth2;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.fiware.apps.repository.dao.impl.MongoResourceDAO;
import org.fiware.apps.repository.exceptions.db.DatasourceException;
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

        // Check if client is a web browser
        try {
            if (isBrowser(MediaType.parseMediaTypes(request.getHeader("Accept"))) && isHTMLinfo(request.getRequestURI())) {
                this.client.redirect(context, true, false);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
            }
        } catch (final RequiresHttpAction e) {
            logger.debug("extra HTTP action required : {}", e.getCode());
        }
    }

    @Override
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
            if(mediaString.equalsIgnoreCase("text/html") ||
                mediaString.equalsIgnoreCase("application/xhtml+xml") ||
                mediaString.equalsIgnoreCase("imagen/webp")) {
                return true;
            }
        }
        return false;
    }

    private boolean isHTMLinfo(String uri) {
        MongoResourceDAO resourceDAO = new MongoResourceDAO();
        try {
            return uri.regionMatches(true, uri.length()-5, ".meta", 0, 5) || !resourceDAO.isResource(uri.substring(uri.indexOf("collec/")+7, uri.length()));
        } catch (DatasourceException ex) {
            return false;
        }
    }
}
