package br.com.jpb.superlogica;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

final class SuperLogicaClient {

	private final String appToken;
	private final String accessToken;
	private final SuperLogicaApiVersion apiVersion = SuperLogicaApiVersion.V2;
	private SuperLogicaEndpoint endpoint;
	private Object[] pathParameters;
	private Entity<?> objectParameter;

	private SuperLogicaClient(String appToken, String accessToken) {
		this.appToken = appToken;
		this.accessToken = accessToken;
	}

	static SuperLogicaClient build(String appToken, String accessToken) {
		return new SuperLogicaClient(appToken, accessToken);
	}

	SuperLogicaClient withEndpoint(SuperLogicaEndpoint endpoint,
			Object... pathParameters) {
		this.endpoint = endpoint;
		this.pathParameters = pathParameters;
		return this;
	}

	<T> SuperLogicaClient withObjectParameter(T t,
			boolean upperCaseParameterKeys) {
		final Form form = new Form();
		this.objectParameter = Entity.form(form);

		ObjectMapper mapper = new ObjectMapper();
		TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {
		};
		Map<String, String> convertValue = mapper.convertValue(t, typeRef);
		convertValue.entrySet().forEach(entry -> {
			form.param(upperCaseParameterKeys ? entry.getKey().toUpperCase()
					: entry.getKey(), entry.getValue());
		});
		return this;
	}

	<T> T getResultList(GenericType<T> returnGenericType) {
		Response response = makeRequest();
		if (returnGenericType == null) {
			return null;
		}
		T readEntity = response.readEntity(returnGenericType);
		reset();
		return readEntity;
	}

	<T> T getSingleResult(Class<T> returnType) {
		Response response = makeRequest();
		if (returnType == null) {
			return null;
		}
		T readEntity = response.readEntity(returnType);
		reset();
		return readEntity;
	}

	private void reset() {
		this.endpoint = null;
		this.pathParameters = null;
		this.objectParameter = null;
	}

	private Response makeRequest() {
		ResteasyClient client = buildClient();
		ResteasyWebTarget target = client.target(
				apiVersion.getFullUrl(endpoint.getEndpoint(pathParameters)));
		Response response = target.request(MediaType.APPLICATION_JSON)
				.header("app_token", appToken)
				.header("access_token", accessToken)
				.method(endpoint.getHttpMethod(), objectParameter);
		return response;
	}

	private ResteasyClient buildClient() {
		return new ResteasyClientBuilder()
				.establishConnectionTimeout(30, TimeUnit.SECONDS)
				.socketTimeout(60, TimeUnit.SECONDS).build();
	}

}