# TokensApi

All URIs are relative to *https://directline.botframework.com*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**tokensGenerateTokenForNewConversation**](TokensApi.md#tokensGenerateTokenForNewConversation) | **POST** /v3/directline/tokens/generate | Generate a token for a new conversation |
| [**tokensRefreshToken**](TokensApi.md#tokensRefreshToken) | **POST** /v3/directline/tokens/refresh | Refresh a token |


<a name="tokensGenerateTokenForNewConversation"></a>
# **tokensGenerateTokenForNewConversation**
> Conversation tokensGenerateTokenForNewConversation(tokenParameters)

Generate a token for a new conversation

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.TokensApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://directline.botframework.com");

    TokensApi apiInstance = new TokensApi(defaultClient);
    TokenParameters tokenParameters = new TokenParameters(); // TokenParameters | 
    try {
      Conversation result = apiInstance.tokensGenerateTokenForNewConversation(tokenParameters);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling TokensApi#tokensGenerateTokenForNewConversation");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **tokenParameters** | [**TokenParameters**](TokenParameters.md)|  | [optional] |

### Return type

[**Conversation**](Conversation.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json, text/json, application/xml, text/xml, application/x-www-form-urlencoded
 - **Accept**: application/json, text/json, application/xml, text/xml

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The conversation was successfully created, updated, or retrieved. |  -  |
| **401** | The operation included an invalid or missing Authorization header. |  -  |
| **403** | You are forbidden from performing this action because your token or secret is invalid. |  -  |
| **404** | The requested resource was not found. |  -  |
| **429** | Too many requests have been submitted to this API. This error may be accompanied by a Retry-After header, which includes the suggested retry interval. |  -  |
| **500** | An internal server error has occurred. |  -  |

<a name="tokensRefreshToken"></a>
# **tokensRefreshToken**
> Conversation tokensRefreshToken()

Refresh a token

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.TokensApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://directline.botframework.com");

    TokensApi apiInstance = new TokensApi(defaultClient);
    try {
      Conversation result = apiInstance.tokensRefreshToken();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling TokensApi#tokensRefreshToken");
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

[**Conversation**](Conversation.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, text/json, application/xml, text/xml

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The conversation was successfully created, updated, or retrieved. |  -  |
| **401** | The operation included an invalid or missing Authorization header. |  -  |
| **403** | You are forbidden from performing this action because your token or secret is invalid. |  -  |
| **404** | The requested resource was not found. |  -  |
| **429** | Too many requests have been submitted to this API. This error may be accompanied by a Retry-After header, which includes the suggested retry interval. |  -  |
| **500** | An internal server error has occurred. |  -  |

