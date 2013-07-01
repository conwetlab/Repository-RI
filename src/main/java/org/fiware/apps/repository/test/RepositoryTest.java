package org.fiware.apps.repository.test;

import junit.framework.TestCase;

import org.fiware.apps.repository.client.CollectionClient;
import org.fiware.apps.repository.client.ResourceClient;
import org.fiware.apps.repository.model.Resource;
import org.fiware.apps.repository.model.ResourceCollection;
import org.junit.Test;

public class RepositoryTest extends TestCase {
	private static final String BASE_URL = "http://10.55.149.20:8080/FiwareRepository/v1";
	//private static final String BASE_URL = "http://130.206.81.36:8080/FiwareRepository/v1";	
	private static CollectionClient cclient = new CollectionClient(BASE_URL);
	private static ResourceClient rclient = new ResourceClient(BASE_URL);

	/*
	@Test	
	public void testPutCol()  {
		ResourceCollection col  = new ResourceCollection();
		col.setCreator("creatorTest");			
		assertTrue(cclient.put("/asd/grr/wvw/kkk", col));
	}
	*/

	@Test	
	public void testPutRes()  {
		
		
		cclient.find(BASE_URL+"/testCollection");
		
		/*ResourceCollection col = new ResourceCollection();
		col.setCreator("Creatorname");
		cclient.put("/testCollection", col);
		*/
	//	col.setCreator("CreatornameUpdate");
	//	cclient.updateCollection("/collectionY", col);
		
		
	//	rclient.insertResourceContentRDF("/abc/cde/efgt12", "C:\\aa.rdf",  "application/rdf+xml");
	//	rclient.insertResourceContentRDF("/abc/cde/efgt2", "C:\\aa.rdf",  "text/turtle");	
	//	rclient.insertResourceContentRDF("/ss/cde/eee24", "C:\\aa.rdf",  "application/rdf+xml");		
	//	rclient.insertResourceContentRDF("/ss/cde/eee235", "C:\\aa.rdf",  "plain/text");
	//	Resource res = rclient.getResource("/ss/cde/eee24.meta");
	//	res.setContentFileName("fdsfsd");
//		rclient.updateResource("/ss/cde/eee235", res);
		
		rclient.insertResourceContentRDF("/testCollection2/testResource", "C:\\setup.log",  "plain/text");
		rclient.insertResourceContentRDF("/testCollection2/testResource1", "C:\\setup.log",  "plain/text");
		
		
	/*	rclient.insertResourceContentRDF("/testCollection/testResource3", "C:\\setup.log",  "text/turtle");
		rclient.insertResourceContentRDF("/testCollection/testResource4", "C:\\setup.log",  "application/rdf+xml");
		rclient.insertResourceContentRDF("/testCollection/collectionA/testResource2", "C:\\setup.log",  "application/rdf+xml");
		rclient.insertResourceContentRDF("/testCollection/collectionA/testResource2", "C:\\setup.log",  "application/rdf+xml");
		rclient.insertResourceContentRDF("/testCollection/collectionB/testResource2", "C:\\setup.log",  "application/rdf+xml");
		rclient.insertResourceContentRDF("/testCollection//collectionC/testResource2", "C:\\setup.log",  "application/rdf+xml");
	*/
	//	ResourceCollection ds = cclient.find("/a/cfa");
	//	cclient.updateCollection("/a/cfa", ds);
	//	rclient.deleteResource("/a/res9995.usdl");
	//rclient.insertResourceContentRDF("/a/cdde/ccc2", "C:\\aa.rdf",  "application/rdf+xml");
		
	//	Resource res = rclient.getResource("/ab/cdde/ccc3.txt.meta");
	//	res.setContentMimeType("application/rdf+xml");
	//	rclient.updateResource("/ab/cdde/ccc3.txt", res);
		
		
		/*
		 * 
		 * 
		 * 
		
		rclient.deleteResource("/collectionA/collectionB/ResourceName");
		cclient.delete("/collectionA");
		
		
		String resid="/collectionA/collectionB/ResourceName";
		Resource res = new Resource();		
		res.setCreator("creatorTest");	
		assertTrue(rclient.put(resid, res));
		rclient.insertResourceContent(resid, "C:\\setup.txt");
		res=rclient.getResource(resid+".meta");
		
		res.setCreator("CreatorName");
		rclient.updateResource(resid, res);		
		ResourceCollection col = cclient.find("/collectionA/collectionB/");
		col.setCreator("CreatorName");
		cclient.updateCollection("/collectionA/collectionB/", col);
		*/
	//	rclient.deleteResource("/collectionA/collectionB/ResourceName");
	//	cclient.delete("/collectionA");
		
	
		
	}

}
