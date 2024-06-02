# SessionApi

All URIs are relative to *https://directline.botframework.com*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**sessionGetSessionId**](SessionApi.md#sessionGetSessionId) | **GET** /v3/directline/session/getsessionid |  |


<a name="sessionGetSessionId"></a>
# **sessionGetSessionId**
> Object sessionGetSessionId()



### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.SessionApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://directline.botframework.com");

    SessionApi apiInstance = new SessionApi(defaultClient);
    try {
      Object result = apiInstance.sessionGetSessionId();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling SessionApi#sessionGetSessionId");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

**Object**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, text/json, application/xml, text/xml

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | OK |  -  |

