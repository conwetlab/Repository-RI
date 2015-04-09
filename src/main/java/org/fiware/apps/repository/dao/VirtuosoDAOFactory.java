package org.fiware.apps.repository.dao;
import org.fiware.apps.repository.dao.impl.VirtuosoResourceDAO;

public abstract class VirtuosoDAOFactory {
    
    static public VirtuosoResourceDAO getVirtuosoResourceDAO() {
        return new VirtuosoResourceDAO();
    }
}
