# ConversationsApi

All URIs are relative to *https://directline.botframework.com*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**conversationsGetActivities**](ConversationsApi.md#conversationsGetActivities) | **GET** /v3/directline/conversations/{conversationId}/activities | Get activities in this conversation. This method is paged with the &#39;watermark&#39; parameter. |
| [**conversationsPostActivity**](ConversationsApi.md#conversationsPostActivity) | **POST** /v3/directline/conversations/{conversationId}/activities | Send an activity |
| [**conversationsReconnectToConversation**](ConversationsApi.md#conversationsReconnectToConversation) | **GET** /v3/directline/conversations/{conversationId} | Get information about an existing conversation |
| [**conversationsStartConversation**](ConversationsApi.md#conversationsStartConversation) | **POST** /v3/directline/conversations | Start a new conversation |
| [**conversationsUpload**](ConversationsApi.md#conversationsUpload) | **POST** /v3/directline/conversations/{conversationId}/upload | Upload file(s) and send as attachment(s) |


<a name="conversationsGetActivities"></a>
# **conversationsGetActivities**
> ActivitySet conversationsGetActivities(conversationId, watermark)

Get activities in this conversation. This method is paged with the &#39;watermark&#39; parameter.

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ConversationsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://directline.botframework.com");

    ConversationsApi apiInstance = new ConversationsApi(defaultClient);
    String conversationId = "conversationId_example"; // String | Conversation ID
    String watermark = "watermark_example"; // String | (Optional) only returns activities newer than this watermark
    try {
      ActivitySet result = apiInstance.conversationsGetActivities(conversationId, watermark);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ConversationsApi#conversationsGetActivities");
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
| **conversationId** | **String**| Conversation ID | |
| **watermark** | **String**| (Optional) only returns activities newer than this watermark | [optional] |

### Return type

[**ActivitySet**](ActivitySet.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, text/json, application/xml, text/xml

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A set of activities and a watermark are returned. |  -  |
| **400** | The URL, body, or headers in the request are malformed or invalid. |  -  |
| **401** | The operation included an invalid or missing Authorization header. |  -  |
| **403** | You are forbidden from performing this action because your token or secret is invalid. |  -  |
| **404** | The requested resource was not found. |  -  |
| **429** | Too many requests have been submitted to this API. This error may be accompanied by a Retry-After header, which includes the suggested retry interval. |  -  |
| **500** | An internal server error has occurred. |  -  |

<a name="conversationsPostActivity"></a>
# **conversationsPostActivity**
> ResourceResponse conversationsPostActivity(conversationId, activity)

Send an activity

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ConversationsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://directline.botframework.com");

    ConversationsApi apiInstance = new ConversationsApi(defaultClient);
    String conversationId = "conversationId_example"; // String | Conversation ID
    Activity activity = new Activity(); // Activity | Activity to send
    try {
      ResourceResponse result = apiInstance.conversationsPostActivity(conversationId, activity);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ConversationsApi#conversationsPostActivity");
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
| **conversationId** | **String**| Conversation ID | |
| **activity** | [**Activity**](Activity.md)| Activity to send | |

### Return type

[**ResourceResponse**](ResourceResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json, text/json, application/xml, text/xml, application/x-www-form-urlencoded
 - **Accept**: application/json, text/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The operation succeeded. |  -  |
| **204** | The operation succeeded. No content was returned. |  -  |
| **400** | The URL, body, or headers in the request are malformed or invalid. |  -  |
| **401** | The operation included an invalid or missing Authorization header. |  -  |
| **403** | You are forbidden from performing this action because your token or secret is invalid. |  -  |
| **404** | The requested resource was not found. |  -  |
| **429** | Too many requests have been submitted to this API. This error may be accompanied by a Retry-After header, which includes the suggested retry interval. |  -  |
| **500** | An internal server error has occurred. |  -  |
| **502** | The bot is unavailable or returned an error. |  -  |

<a name="conversationsReconnectToConversation"></a>
# **conversationsReconnectToConversation**
> Conversation conversationsReconnectToConversation(conversationId, watermark)

Get information about an existing conversation

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ConversationsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://directline.botframework.com");

    ConversationsApi apiInstance = new ConversationsApi(defaultClient);
    String conversationId = "conversationId_example"; // String | 
    String watermark = "watermark_example"; // String | 
    try {
      Conversation result = apiInstance.conversationsReconnectToConversation(conversationId, watermark);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ConversationsApi#conversationsReconnectToConversation");
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
| **conversationId** | **String**|  | |
| **watermark** | **String**|  | [optional] |

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
| **400** | The URL, body, or headers in the request are malformed or invalid. |  -  |
| **401** | The operation included an invalid or missing Authorization header. |  -  |
| **403** | You are forbidden from performing this action because your token or secret is invalid. |  -  |
| **404** | The requested resource was not found. |  -  |
| **429** | Too many requests have been submitted to this API. This error may be accompanied by a Retry-After header, which includes the suggested retry interval. |  -  |
| **500** | An internal server error has occurred. |  -  |

<a name="conversationsStartConversation"></a>
# **conversationsStartConversation**
> Conversation conversationsStartConversation(tokenParameters)

Start a new conversation

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ConversationsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://directline.botframework.com");

    ConversationsApi apiInstance = new ConversationsApi(defaultClient);
    TokenParameters tokenParameters = new TokenParameters(); // TokenParameters | 
    try {
      Conversation result = apiInstance.conversationsStartConversation(tokenParameters);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ConversationsApi#conversationsStartConversation");
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
| **201** | The conversation was successfully created. |  -  |
| **400** | The URL, body, or headers in the request are malformed or invalid. |  -  |
| **401** | The operation included an invalid or missing Authorization header. |  -  |
| **403** | You are forbidden from performing this action because your token or secret is invalid. |  -  |
| **404** | The requested resource was not found. |  -  |
| **409** | The object you are trying to create already exists. |  -  |
| **429** | Too many requests have been submitted to this API. This error may be accompanied by a Retry-After header, which includes the suggested retry interval. |  -  |
| **500** | An internal server error has occurred. |  -  |

<a name="conversationsUpload"></a>
# **conversationsUpload**
> ResourceResponse conversationsUpload(conversationId, _file, userId)

Upload file(s) and send as attachment(s)

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.ConversationsApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("https://directline.botframework.com");

    ConversationsApi apiInstance = new ConversationsApi(defaultClient);
    String conversationId = "conversationId_example"; // String | 
    File _file = new File("/path/to/file"); // File | 
    String userId = "userId_example"; // String | 
    try {
      ResourceResponse result = apiInstance.conversationsUpload(conversationId, _file, userId);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ConversationsApi#conversationsUpload");
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
| **conversationId** | **String**|  | |
| **_file** | **File**|  | |
| **userId** | **String**|  | [optional] |

### Return type

[**ResourceResponse**](ResourceResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: application/json, text/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The operation succeeded. |  -  |
| **202** | The request was accepted for processing. |  -  |
| **204** | The operation succeeded. No content was returned. |  -  |
| **400** | The URL, body, or headers in the request are malformed or invalid. |  -  |
| **401** | The operation included an invalid or missing Authorization header. |  -  |
| **403** | You are forbidden from performing this action because your token or secret is invalid. |  -  |
| **404** | The requested resource was not found. |  -  |
| **429** | Too many requests have been submitted to this API. This error may be accompanied by a Retry-After header, which includes the suggested retry interval. |  -  |
| **500** | An internal server error has occurred. |  -  |
| **502** | The bot is unavailable or returned an error. |  -  |

