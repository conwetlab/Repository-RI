package org.fiware.apps.repository.test;

import junit.framework.TestCase;

import org.fiware.apps.repository.client.CollectionClient;
import org.fiware.apps.repository.client.ResourceClient;
import org.junit.Test;

public class FinestTest extends TestCase {
	//http://localhost:8080/FiwareRepository/v1
	private static final String BASE_URL = "http://130.206.81.36:8080/FiwareRepository/v1";	
	private static CollectionClient cclient = new CollectionClient(BASE_URL);
	private static ResourceClient rclient = new ResourceClient(BASE_URL);



	@Test	
	public void testPutRes()  {
		
		rclient.insertResourceContentRDF("/finest/034/example1_maritime_v0.3.4.ttl", "C:\\test\\example1_maritime_v0.3.4.ttl",  "example/rdf+ttl");
		rclient.insertResourceContentRDF("/finest/034/example2_maritime_v0.3.4.ttl", "C:\\test\\example2_maritime_v0.3.4.ttl",  "example/rdf+ttl");
		rclient.insertResourceContentRDF("/finest/034/example3_maritime_v0.3.4.ttl", "C:\\test\\example3_maritime_v0.3.4.ttl",  "example/rdf+ttl");
		rclient.insertResourceContentRDF("/finest/034/example4_truck_v0.3.4.ttl", "C:\\test\\example4_truck_v0.3.4.ttl",  "example/rdf+ttl");
		rclient.insertResourceContentRDF("/finest/034/example5_truck_v0.3.4.ttl", "C:\\test\\example5_truck_v0.3.4.ttl",  "example/rdf+ttl");
		rclient.insertResourceContentRDF("/finest/034/example6_truck_v0.3.4.ttl", "C:\\test\\example6_truck_v0.3.4.ttl",  "example/rdf+ttl");
		rclient.insertResourceContentRDF("/finest/034/example7_multiModal_v0.3.4.ttl", "C:\\test\\example7_multiModal_v0.3.4.ttl",  "example/rdf+ttl");
		rclient.insertResourceContentRDF("/finest/034/example8_multiModal_v0.3.4.ttl", "C:\\test\\example8_multiModal_v0.3.4.ttl",  "example/rdf+ttl");
		rclient.insertResourceContentRDF("/finest/034/example9_multiModal_v0.3.4.ttl", "C:\\test\\example9_multiModal_v0.3.4.ttl",  "example/rdf+ttl");
	}

}
