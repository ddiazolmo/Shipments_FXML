package ins.marianao.shipments.fxml.services;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cat.institutmarianao.shipmentsws.model.Address;
import cat.institutmarianao.shipmentsws.model.Shipment;
import cat.institutmarianao.shipmentsws.model.Shipment.Category;
import cat.institutmarianao.shipmentsws.model.Shipment.Status;
import ins.marianao.shipments.fxml.manager.ResourceManager;

public class ServiceQueryShipments extends ServiceQueryBase<Shipment>{

	public static final String PATH_REST_SHIPMENTS = "shipments";
	
	private Category category;
	private Status status;
	private Address receiver;
	private String courier;
	
	public ServiceQueryShipments(Category category, Status status, Address receiver, String courier) {
		this.category = category;
		this.status = status;
		this.receiver = receiver;
		this.courier = courier;
	}

	@Override
	protected List<Shipment> customCall() throws Exception {
		Client client = ResourceManager.getInstance().getWebClient();

		WebTarget webTarget = client.target(ResourceManager.getInstance().getParam("web.service.host.url")).path(PATH_REST_SHIPMENTS).path(PATH_QUERY_ALL);
		
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
		
		List<Shipment> shipments = new LinkedList<Shipment>();
		try {
			Response response = invocationBuilder.get();

			if (response.getStatus() != Response.Status.OK.getStatusCode()) 
				throw new Exception(ResourceManager.getInstance().responseErrorToString(response));

			shipments = response.readEntity(new GenericType<List<Shipment>>(){});
			
		} catch (ResponseProcessingException e) {
			e.printStackTrace();
			throw new Exception(ResourceManager.getInstance().getText("error.service.response.processing")+" "+e.getMessage());
		} catch (ProcessingException e) {
			e.printStackTrace();
			throw new Exception(ResourceManager.getInstance().getText("error.service.processing")+" "+e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return shipments;
	}

}
