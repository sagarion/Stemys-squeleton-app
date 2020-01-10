/**
 *                   Stemys SA
 * 
 * @author Guillaume Milani
 * @date 01.2019
 * 
 * This file describes a REST controller which will
 * serve content over HTTP, exposing multiple endpoints.
 */

package io.stemys.app.api;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.ops4j.pax.cdi.api.OsgiService;

import io.stemys.binding.smartfactory.model.Smartfactory;
import io.stemys.binding.smartfactory.service.SmartfactoryService;
import io.stemys.platform.core.thing.model.ItemValue;

/**
 * The @Named annotation gives a name to this controller, this name must be
 * reused in src/main/resources/OSGI-INF/blueprint/rest.xml for activating the
 * controller.
 * 
 * The @Path annotation describes the root path of this endpoint in the REST
 * API.
 * 
 * For more documentation please see io.stemys.app.api.SwitchButtonRestService
 */
@Named("SmartfactoryRestService")
@Path("/Smartfactory")
public class SmartfactoryRestService {
	@Inject
	@OsgiService
	private SmartfactoryService smartFactoryService;

	@GET
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	public Response get() {
		List<Smartfactory> sondes = this.smartFactoryService.findAll();
		return Response.ok(sondes).build();
	}

	@GET
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	@Path("/{id}/smartfactory")
	public Response getState(@PathParam("id") String id) {
		// We want this sonde's temperatures until now
		List<ItemValue<?>> values = this.smartFactoryService.getItemValues(id, Smartfactory.ITEM_RUNTIME, null, null);
		if (values.isEmpty()) {
			return Response.status(204).build();
		}

		ItemValue<?> lastValue = values.get(values.size() - 1);

		return Response.ok( (Integer) lastValue.getValue()).build();
	}
}
