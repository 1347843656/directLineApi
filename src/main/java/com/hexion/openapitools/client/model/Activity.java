/*
 * Bot Connector - Direct Line API - v3.0
 * Direct Line 3.0  ===============      The Direct Line API is a simple REST API for connecting directly to a single bot. This API is intended for developers  writing their own client applications, web chat controls, mobile apps, or service-to-service applications that will  talk to their bot.    Within the Direct Line API, you will find:    * An **authentication mechanism** using standard secret/token patterns  * The ability to **send** messages from your client to your bot via an HTTP POST message  * The ability to **receive** messages by **WebSocket** stream, if you choose  * The ability to **receive** messages by **polling HTTP GET**, if you choose  * A stable **schema**, even if your bot changes its protocol version    Direct Line 1.1 and 3.0 are both available and supported. This document describes Direct Line 3.0. For information  on Direct Line 1.1, visit the [Direct Line 1.1 reference documentation](/en-us/restapi/directline/).    # Authentication: Secrets and Tokens    Direct Line allows you to authenticate all calls with either a secret (retrieved from the Direct Line channel  configuration page) or a token (which you may get at runtime by converting your secret).    A Direct Line **secret** is a master key that can access any conversation, and create tokens. Secrets do not expire.    A Direct Line **token** is a key for a single conversation. It expires but can be refreshed.    If you're writing a service-to-service application, using the secret may be simplest. If you're writing an application  where the client runs in a web browser or mobile app, you may want to exchange your secret for a token, which only  works for a single conversation and will expire unless refreshed. You choose which security model works best for you.    Your secret or token is communicated in the ```Authorization``` header of every call, with the Bearer scheme.  Example below.    ```  -- connect to directline.botframework.com --  POST /v3/directline/conversations/abc123/activities HTTP/1.1  Authorization: Bearer RCurR_XV9ZA.cwA.BKA.iaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0  [other HTTP headers, omitted]  ```    You may notice that your Direct Line client credentials are different from your bot's credentials. This is  intentional, and it allows you to revise your keys independently and lets you share client tokens without  disclosing your bot's password.     ## Exchanging a secret for a token    This operation is optional. Use this step if you want to prevent clients from accessing conversations they aren't  participating in.    To exchange a secret for a token, POST to /v3/directline/tokens/generate with your secret in the auth header  and no HTTP body.    ```  -- connect to directline.botframework.com --  POST /v3/directline/tokens/generate HTTP/1.1  Authorization: Bearer RCurR_XV9ZA.cwA.BKA.iaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0  [other headers]    -- response from directline.botframework.com --  HTTP/1.1 200 OK  [other headers]    {    \"conversationId\": \"abc123\",    \"token\": \"RCurR_XV9ZA.cwA.BKA.iaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0y8qbOF5xPGfiCpg4Fv0y8qqbOF5x8qbOF5xn\",    \"expires_in\": 1800  }  ```    If successful, the response is a token suitable for one conversation. The token expires in the seconds  indicated in the ```expires_in``` field (30 minutes in the example above) and must be refreshed before then to  remain useful.    This call is similar to ```/v3/directline/conversations```. The difference is that the call to  ```/v3/directline/tokens/generate``` does not start the conversation, does not contact the bot, and does not  create a streaming WebSocket URL.  * Call ```/v3/directline/conversations``` if you will distribute the token to client(s) and want them to     initiate the conversation.  * Call ```/v3/directline/conversations``` if you intend to start the conversation immediately.      ## Refreshing a token    A token may be refreshed an unlimited number of times unless it is expired.    To refresh a token, POST to /v3/directline/tokens/refresh. This method is valid only for unexpired tokens.    ```  -- connect to directline.botframework.com --  POST /v3/directline/tokens/refresh HTTP/1.1  Authorization: Bearer RCurR_XV9ZA.cwA.BKA.iaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0y8qbOF5xPGfiCpg4Fv0y8qqbOF5x8qbOF5xn  [other headers]    -- response from directline.botframework.com --  HTTP/1.1 200 OK  [other headers]    {    \"conversationId\": \"abc123\",    \"token\": \"RCurR_XV9ZA.cwA.BKA.y8qbOF5xPGfiCpg4Fv0y8qqbOF5x8qbOF5xniaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0\",    \"expires_in\": 1800  }  ```       # REST calls for a Direct Line conversation    Direct Line conversations are explicitly opened by clients and may run as long as the bot and client participate  (and have valid credentials). While the conversation is open, the bot and client may both send messages. More than  one client may connect to a given conversation and each client may participate on behalf of multiple users.    ## Starting a conversation    Clients begin by explicitly starting a conversation. If successful, the Direct Line service replies with a  JSON object containing a conversation ID, a token, and a WebSocket URL that may be used later.    ```  -- connect to directline.botframework.com --  POST /v3/directline/conversations HTTP/1.1  Authorization: Bearer RCurR_XV9ZA.cwA.BKA.iaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0y8qbOF5xPGfiCpg4Fv0y8qqbOF5x8qbOF5xn  [other headers]    -- response from directline.botframework.com --  HTTP/1.1 201 Created  [other headers]    {    \"conversationId\": \"abc123\",    \"token\": \"RCurR_XV9ZA.cwA.BKA.iaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0y8qbOF5xPGfiCpg4Fv0y8qqbOF5x8qbOF5xn\",    \"expires_in\": 1800,    \"streamUrl\": \"https://directline.botframework.com/v3/directline/conversations/abc123/stream?t=RCurR_XV9ZA.cwA...\"  }  ```    If the conversation was started, an HTTP 201 status code is returned. HTTP 201 is the code that clients  will receive under most circumstances, as the typical use case is for a client to start a new conversation.  Under certain conditions -- specifically, when the client has a token scoped to a single conversation AND  when that conversation was started with a prior call to this URL -- this method will return HTTP 200 to signify  the request was acceptable but that no conversation was created (as it already existed).    You have 60 seconds to connect to the WebSocket URL. If the connection cannot be established during this time,  use the reconnect method below to generate a new stream URL.    This call is similar to ```/v3/directline/tokens/generate```. The difference is that the call to  ```/v3/directline/conversations``` starts the conversation, contacts the bot, and creates a streaming WebSocket  URL, none of which occur when generating a token.  * Call ```/v3/directline/conversations``` if you will distribute the token to client(s) and want them to    initiate the conversation.  * Call ```/v3/directline/conversations``` if you intend to start the conversation immediately.    ## Reconnecting to a conversation    If a client is using the WebSocket interface to receive messages but loses its connection, it may need to reconnect.  Reconnecting requires generating a new WebSocket stream URL, and this can be accomplished by sending a GET request  to the ```/v3/directline/conversations/{id}``` endpoint.    The ```watermark``` parameter is optional. If supplied, the conversation replays from the watermark,  guaranteeing no messages are lost. If ```watermark``` is omitted, only messages received after the reconnection  call (```GET /v3/directline/conversations/abc123```) are replayed.    ```  -- connect to directline.botframework.com --  GET /v3/directline/conversations/abc123?watermark=0000a-42 HTTP/1.1  Authorization: Bearer RCurR_XV9ZA.cwA.BKA.iaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0y8qbOF5xPGfiCpg4Fv0y8qqbOF5x8qbOF5xn  [other headers]    -- response from directline.botframework.com --  HTTP/1.1 200 OK  [other headers]    {    \"conversationId\": \"abc123\",    \"token\": \"RCurR_XV9ZA.cwA.BKA.iaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0y8qbOF5xPGfiCpg4Fv0y8qqbOF5x8qbOF5xn\",    \"streamUrl\": \"https://directline.botframework.com/v3/directline/conversations/abc123/stream?watermark=000a-4&t=RCurR_XV9ZA.cwA...\"  }  ```    You have 60 seconds to connect to the WebSocket stream URL. If the connection cannot be established during this  time, issue another reconnect request to get an updated stream URL.    ## Sending an Activity to the bot    Using the Direct Line 3.0 protocol, clients and bots may exchange many different Bot Framework v3 Activities,  including Message Activities, Typing Activities, and custom activities that the bot supports.    To send any one of these activities to the bot,    1. the client formulates the Activity according to the Activity schema (see below)  2. the client issues a POST message to ```/v3/directline/conversations/{id}/activities```  3. the service returns when the activity was delivered to the bot, with an HTTP status code reflecting the     bot's status code. If the POST was successful, the service returns a JSON payload containing the ID of the     Activity that was sent.    Example follows.    ```  -- connect to directline.botframework.com --  POST /v3/directline/conversations/abc123/activities HTTP/1.1  Authorization: Bearer RCurR_XV9ZA.cwA.BKA.iaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0  [other headers]    {    \"type\": \"message\",    \"from\": {      \"id\": \"user1\"    },    \"text\": \"hello\"  }    -- response from directline.botframework.com --  HTTP/1.1 200 OK  [other headers]    {    \"id\": \"0001\"  }  ```    The client's Activity is available in the message retrieval path (either polling GET or WebSocket) and is not  returned inline.    The total time to POST a message to a Direct Line conversation is:    * Transit time to the Direct Line service,  * Internal processing time within Direct Line (typically less than 120ms)  * Transit time to the bot  * Processing time within the bot  * Transit time for HTTP responses to travel back to the client.    If the bot generates an error, that error will trigger an HTTP 502 error (\"Bad Gateway\") in  the ```POST /v3/directline/conversations/{id}/activities``` call.    ### Sending one or more attachments by URL    Clients may optionally send attachments, such as images or documents. If the client already has a URL for the  attachment, the simplest way to send it is to include the URL in the ```contentUrl``` field of an Activity  attachment object. This applies to HTTP, HTTPS, and ```data:``` URIs.    ### Sending a single attachment by upload    Often, clients have an image or document on a device but no URL that can be included in the activity.    To upload an attachment, POST a single attachment to  the ```/v3/directline/conversations/{conversationId}/upload``` endpoint. The ```Content-Type```  and ```Content-Disposition``` headers control the attachment's type and filename, respectively.    A user ID is required. Supply the ID of the user sending the attachment as a ```userId``` parameter in the URL.    If uploading a single attachment, a message activity is sent to the bot when the upload completes.    On completion, the service returns the ID of the activity that was sent.    ```  -- connect to directline.botframework.com --  POST /v3/directline/conversations/abc123/upload?userId=user1 HTTP/1.1  Authorization: Bearer RCurR_XV9ZA.cwA.BKA.iaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0  Content-Type: image/jpeg  Content-Disposition: name=\"file\"; filename=\"badjokeeel.jpg\"  [other headers]    [JPEG content]    -- response from directline.botframework.com --  HTTP/1.1 200 OK  [other headers]    {    \"id\": \"0003\"  }  ```    ### Sending multiple attachments by upload    If uploading multiple attachments, use ```multipart/form-data``` as the content type and include each  attachment as a separate part. Each attachment's type and filename may be included in the ```Content-Type```  and ```Content-Disposition``` headers in each part.    An activity may be included by adding a part with content type of ```application/vnd.microsoft.activity```.  Other parts in the payload are attached to this activity before it is sent. If an Activity is not included,  an empty Activity is created as a wrapper for the attachments.    ```  -- connect to directline.botframework.com --  POST /v3/directline/conversations/abc123/upload?userId=user1 HTTP/1.1  Authorization: Bearer RCurR_XV9ZA.cwA.BKA.iaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0  Content-Type: multipart/form-data; boundary=----DD4E5147-E865-4652-B662-F223701A8A89  [other headers]    ----DD4E5147-E865-4652-B662-F223701A8A89  Content-Type: image/jpeg  Content-Disposition: form-data; name=\"file\"; filename=\"badjokeeel.jpg\"  [other headers]    [JPEG content]    ----DD4E5147-E865-4652-B662-F223701A8A89  Content-Type: application/vnd.microsoft.activity  [other headers]    {    \"type\": \"message\",    \"from\": {      \"id\": \"user1\"    },    \"text\": \"Hey I just IM'd you\\n\\nand this is crazy\\n\\nbut here's my webhook\\n\\nso POST me maybe\"  }    ----DD4E5147-E865-4652-B662-F223701A8A89            -- response from directline.botframework.com --  HTTP/1.1 200 OK  [other headers]    {    \"id\": \"0004\"  }  ```    ## Receiving Activities from the bot    Direct Line 3.0 clients may choose from two different mechanisms for retrieving messages:    1. A **streaming WebSocket**, which pushes messages efficiently to clients.  2. A **polling GET** interface, which is available for clients unable to use WebSockets or for clients     retrieving the conversation history.    **Not all activities are available via the polling GET interface.** A table of activity availability follows.    |Activity type|Availability|  |-------------|--------|  |Message|Polling GET and WebSocket|  |Typing|WebSocket only|  |ConversationUpdate|Not sent/received via client|  |ContactRelationUpdate|Not supported in Direct Line|  |EndOfConversation|Polling GET and WebSocket|  |All other activity types|Polling GET and WebSocket|    ### Receiving Activities by WebSocket    To connect via WebSocket, a client uses the StreamUrl when starting a conversation. The stream URL is  preauthorized and does NOT require an Authorization header containing the client's secret or token.    ```  -- connect to wss://directline.botframework.com --  GET /v3/directline/conversations/abc123/stream?t=RCurR_XV9ZA.cwA...\" HTTP/1.1  Upgrade: websocket  Connection: upgrade  [other headers]    -- response from directline.botframework.com --  HTTP/1.1 101 Switching Protocols  [other headers]  ```    The Direct Line service sends the following messages:    * An **ActivitySet**, which contains one or more activities and a watermark (described below)  * An empty message, which the Direct Line service uses to ensure the connection is still valid  * Additional types, to be defined later. These types are identified by the properties in the JSON root.    ActivitySets contain messages sent by the bot and by all users. Example ActivitySet:    ```  {    \"activities\": [{      \"type\": \"message\",      \"channelId\": \"directline\",      \"conversation\": {        \"id\": \"abc123\"      },      \"id\": \"abc123|0000\",      \"from\": {        \"id\": \"user1\"      },      \"text\": \"hello\"    }],    \"watermark\": \"0000a-42\"  }  ```    Clients should keep track of the \"watermark\" value from each ActivitySet so they can use it on reconnect.  **Note** that a ```null``` or missing watermark should be ignored and should not overwrite a prior watermark  in the client.    Clients should ignore empty messages.    Clients may send their own empty messages to verify connectivity. The Direct Line service will ignore these.    The service may forcibly close the connection under certain conditions. If the client has not received an  EndOfConversation activity, it may reconnect by issuing a GET request to the conversation endpoint to get a  new stream URL (see above).    The WebSocket stream contains live updates and very recent messages (since the call to get the WebSocket call  was issued) but it does not include messages sent prior to the most recent POST  to ```/v3/directline/conversations/{id}```. To retrieve messages sent earlier in the conversation, use the  GET mechanism below.    ### Receiving Activities by GET    The GET mechanism is useful for clients who are unable to use the WebSocket, or for clients wishing to retrieve  the conversation history.    To retrieve messages, issue a GET call to the conversation endpoint. Optionally supply a watermark, indicating  the most recent message seen. The watermark field accompanies all GET/WebSocket messages as a property in the  ActivitySet.    ```  -- connect to directline.botframework.com --  GET /v3/directline/conversations/abc123/activities?watermark=0001a-94 HTTP/1.1  Authorization: Bearer RCurR_XV9ZA.cwA.BKA.iaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0  [other headers]    -- response from directline.botframework.com --  HTTP/1.1 200 OK  [other headers]    {    \"activities\": [{      \"type\": \"message\",      \"channelId\": \"directline\",      \"conversation\": {        \"id\": \"abc123\"      },      \"id\": \"abc123|0000\",      \"from\": {        \"id\": \"user1\"      },      \"text\": \"hello\"    }, {      \"type\": \"message\",      \"channelId\": \"directline\",      \"conversation\": {        \"id\": \"abc123\"      },      \"id\": \"abc123|0001\",      \"from\": {        \"id\": \"bot1\"      },      \"text\": \"Nice to see you, user1!\"    }],    \"watermark\": \"0001a-95\"  }  ```    Clients should page through the available activities by advancing the ```watermark``` value until no activities  are returned.      ### Timing considerations     Most clients wish to retain a complete message history. Even though Direct Line is a multi-part protocol with  potential timing gaps, the protocol and service is designed to make it easy to build a reliable client.    1. The ```watermark``` field sent in the WebSocket stream and GET response is reliable. You will not miss     messages as long as you replay the watermark verbatim.  2. When starting a conversation and connecting to the WebSocket stream, any Activities sent after the POST but     before the socket is opened are replayed before new messages.  3. When refreshing history by GET call while connected to the WebSocket, Activities may be duplicated across both     channels. Keeping a list of all known Activity IDs will allow you to reject duplicate messages should they occur.    Clients using the polling GET interface should choose a polling interval that matches their intended use.    * Service-to-service applications often use a polling interval of 5s or 10s.  * Client-facing applications often use a polling interval of 1s, and fire an additional request ~300ms after    every message the client sends to rapidly pick up a bot's response. This 300ms delay should be adjusted    based on the bot's speed and transit time.    ## Ending a conversation    Either a client or a bot may signal the end of a DirectLine conversation. This operation halts communication  and prevents the bot and the client from sending messages. Messages may still be retrieved via the GET mechanism.  Sending this messages is as simple as POSTing an EndOfConversation activity.    ```  -- connect to directline.botframework.com --  POST /v3/directline/conversations/abc123/activities HTTP/1.1  Authorization: Bearer RCurR_XV9ZA.cwA.BKA.iaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0  [other headers]    {    \"type\": \"endOfConversation\",    \"from\": {      \"id\": \"user1\"    }  }    -- response from directline.botframework.com --  HTTP/1.1 200 OK  [other headers]    {    \"id\": \"0004\"  }  ```    ## REST API errors    HTTP calls to the Direct Line service follow standard HTTP error conventions:    * 2xx status codes indicate success. (Direct Line 3.0 uses 200 and 201.)  * 4xx status codes indicate an error in your request.    * 401 indicates a missing or malformed Authorization header (or URL token, in calls where a token parameter      is allowed).    * 403 indicates an unauthorized client.      * If calling with a valid but expired token, the ```code``` field is set to ```TokenExpired```.    * 404 indicates a missing path, site, conversation, etc.  * 5xx status codes indicate a service-side error.    * 500 indicates an error inside the Direct Line service.    * 502 indicates an error was returned by the bot. **This is a common error code.**  * 101 is used in the WebSocket connection path, although this is likely handled by your WebSocket client.    When an error message is returned, error detail may be present in a JSON response. Look for an ```error```  property with ```code``` and ```message``` fields.    ```  -- connect to directline.botframework.com --  POST /v3/directline/conversations/abc123/activities HTTP/1.1  [detail omitted]    -- response from directline.botframework.com --  HTTP/1.1 502 Bad Gateway  [other headers]    {    \"error\": {      \"code\": \"BotRejectedActivity\",      \"message\": \"Failed to send activity: bot returned an error\"    }  }  ```    The contents of the ```message``` field may change. The HTTP status code and values in the ```code```  property are stable.    # Schema    The Direct Line 3.0 schema is identical to the Bot Framework v3 schema.    When a bot sends an Activity to a client through Direct Line:    * attachment cards are preserved,  * URLs for uploaded attachments are hidden with a private link, and  * the ```channelData``` property is preserved without modification.    When a client sends an Activity to a bot through Direct Line:    * the ```type``` property contains the kind of activity you are sending (typically ```message```),  * the ```from``` property must be populated with a user ID, chosen by your client,  * attachments may contain URLs to existing resources or URLs uploaded through the Direct Line attachment    endpoint, and  * the ```channelData``` property is preserved without modification.    Clients and bots may send Activities of any type, including Message Activities, Typing Activities, and  custom Activity types.    Clients may send a single Activity at a time.    ```  {    \"type\": \"message\",    \"channelId\": \"directline\",    \"from\": {      \"id\": \"user1\"    },    \"text\": \"hello\"  }  ```    Clients receive multiple Activities as part of an ActivitySet. The ActivitySet has an array of activities  and a watermark field.    ```  {    \"activities\": [{      \"type\": \"message\",      \"channelId\": \"directline\",      \"conversation\": {        \"id\": \"abc123\"      },      \"id\": \"abc123|0000\",      \"from\": {        \"id\": \"user1\"      },      \"text\": \"hello\"    }],    \"watermark\": \"0000a-42\"  }  ```    # Libraries for the Direct Line API    The Direct Line API is designed to be coded directly, but the Bot Framework includes libraries and controls that  help you to embed Direct-Line-powered bots into your application.    * The [Bot Framework Web Chat control](https://github.com/Microsoft/BotFramework-WebChat) is an easy way to embed    the Direct Line protocol into a webpage.  * [Direct Line Nuget package](https://www.nuget.org/packages/Microsoft.Bot.Connector.DirectLine) with libraries for    .Net 4.5, UWP, and .Net Standard.  * [DirectLineJs](https://github.com/Microsoft/BotFramework-DirectLineJs), also available on    [NPM](https://www.npmjs.com/package/botframework-directlinejs)  * You may generate your own from the [Direct Line Swagger file](swagger.json)    Our [BotBuilder-Samples GitHub repo](https://github.com/Microsoft/BotBuilder-Samples) also contains samples for    [C#](https://github.com/Microsoft/BotBuilder-Samples/tree/master/CSharp/core-DirectLine) and    [JavaScript](https://github.com/Microsoft/BotBuilder-Samples/tree/master/Node/core-DirectLine).
 *
 * The version of the OpenAPI document: v3
 * Contact: botframework@microsoft.com
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package com.hexion.openapitools.client.model;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.hexion.openapitools.client.JSON;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.Map.Entry;

/**
 * An Activity is the basic communication type for the Bot Framework 3.0 protocol.
 */
@ApiModel(description = "An Activity is the basic communication type for the Bot Framework 3.0 protocol.")
@javax.annotation.Generated(value = "com.hexion.openapitools.codegen.languages.JavaClientCodegen", date = "2024-06-02T09:58:47.641374700+08:00[Asia/Hong_Kong]")
public class Activity {
  public static final String SERIALIZED_NAME_TYPE = "type";
  @SerializedName(SERIALIZED_NAME_TYPE)
  private String type;

  public static final String SERIALIZED_NAME_ID = "id";
  @SerializedName(SERIALIZED_NAME_ID)
  private String id;

  public static final String SERIALIZED_NAME_TIMESTAMP = "timestamp";
  @SerializedName(SERIALIZED_NAME_TIMESTAMP)
  private OffsetDateTime timestamp;

  public static final String SERIALIZED_NAME_LOCAL_TIMESTAMP = "localTimestamp";
  @SerializedName(SERIALIZED_NAME_LOCAL_TIMESTAMP)
  private OffsetDateTime localTimestamp;

  public static final String SERIALIZED_NAME_LOCAL_TIMEZONE = "localTimezone";
  @SerializedName(SERIALIZED_NAME_LOCAL_TIMEZONE)
  private String localTimezone;

  public static final String SERIALIZED_NAME_SERVICE_URL = "serviceUrl";
  @SerializedName(SERIALIZED_NAME_SERVICE_URL)
  private String serviceUrl;

  public static final String SERIALIZED_NAME_CHANNEL_ID = "channelId";
  @SerializedName(SERIALIZED_NAME_CHANNEL_ID)
  private String channelId;

  public static final String SERIALIZED_NAME_FROM = "from";
  @SerializedName(SERIALIZED_NAME_FROM)
  private ChannelAccount from;

  public static final String SERIALIZED_NAME_CONVERSATION = "conversation";
  @SerializedName(SERIALIZED_NAME_CONVERSATION)
  private ConversationAccount conversation;

  public static final String SERIALIZED_NAME_RECIPIENT = "recipient";
  @SerializedName(SERIALIZED_NAME_RECIPIENT)
  private ChannelAccount recipient;

  public static final String SERIALIZED_NAME_TEXT_FORMAT = "textFormat";
  @SerializedName(SERIALIZED_NAME_TEXT_FORMAT)
  private String textFormat;

  public static final String SERIALIZED_NAME_ATTACHMENT_LAYOUT = "attachmentLayout";
  @SerializedName(SERIALIZED_NAME_ATTACHMENT_LAYOUT)
  private String attachmentLayout;

  public static final String SERIALIZED_NAME_MEMBERS_ADDED = "membersAdded";
  @SerializedName(SERIALIZED_NAME_MEMBERS_ADDED)
  private List<ChannelAccount> membersAdded = null;

  public static final String SERIALIZED_NAME_MEMBERS_REMOVED = "membersRemoved";
  @SerializedName(SERIALIZED_NAME_MEMBERS_REMOVED)
  private List<ChannelAccount> membersRemoved = null;

  public static final String SERIALIZED_NAME_REACTIONS_ADDED = "reactionsAdded";
  @SerializedName(SERIALIZED_NAME_REACTIONS_ADDED)
  private List<MessageReaction> reactionsAdded = null;

  public static final String SERIALIZED_NAME_REACTIONS_REMOVED = "reactionsRemoved";
  @SerializedName(SERIALIZED_NAME_REACTIONS_REMOVED)
  private List<MessageReaction> reactionsRemoved = null;

  public static final String SERIALIZED_NAME_TOPIC_NAME = "topicName";
  @SerializedName(SERIALIZED_NAME_TOPIC_NAME)
  private String topicName;

  public static final String SERIALIZED_NAME_HISTORY_DISCLOSED = "historyDisclosed";
  @SerializedName(SERIALIZED_NAME_HISTORY_DISCLOSED)
  private Boolean historyDisclosed;

  public static final String SERIALIZED_NAME_LOCALE = "locale";
  @SerializedName(SERIALIZED_NAME_LOCALE)
  private String locale;

  public static final String SERIALIZED_NAME_TEXT = "text";
  @SerializedName(SERIALIZED_NAME_TEXT)
  private String text;

  public static final String SERIALIZED_NAME_SPEAK = "speak";
  @SerializedName(SERIALIZED_NAME_SPEAK)
  private String speak;

  public static final String SERIALIZED_NAME_INPUT_HINT = "inputHint";
  @SerializedName(SERIALIZED_NAME_INPUT_HINT)
  private String inputHint;

  public static final String SERIALIZED_NAME_SUMMARY = "summary";
  @SerializedName(SERIALIZED_NAME_SUMMARY)
  private String summary;

  public static final String SERIALIZED_NAME_SUGGESTED_ACTIONS = "suggestedActions";
  @SerializedName(SERIALIZED_NAME_SUGGESTED_ACTIONS)
  private SuggestedActions suggestedActions;

  public static final String SERIALIZED_NAME_ATTACHMENTS = "attachments";
  @SerializedName(SERIALIZED_NAME_ATTACHMENTS)
  private List<Attachment> attachments = null;

  public static final String SERIALIZED_NAME_ENTITIES = "entities";
  @SerializedName(SERIALIZED_NAME_ENTITIES)
  private List<Entity> entities = null;

  public static final String SERIALIZED_NAME_CHANNEL_DATA = "channelData";
  @SerializedName(SERIALIZED_NAME_CHANNEL_DATA)
  private Object channelData;

  public static final String SERIALIZED_NAME_ACTION = "action";
  @SerializedName(SERIALIZED_NAME_ACTION)
  private String action;

  public static final String SERIALIZED_NAME_REPLY_TO_ID = "replyToId";
  @SerializedName(SERIALIZED_NAME_REPLY_TO_ID)
  private String replyToId;

  public static final String SERIALIZED_NAME_LABEL = "label";
  @SerializedName(SERIALIZED_NAME_LABEL)
  private String label;

  public static final String SERIALIZED_NAME_VALUE_TYPE = "valueType";
  @SerializedName(SERIALIZED_NAME_VALUE_TYPE)
  private String valueType;

  public static final String SERIALIZED_NAME_VALUE = "value";
  @SerializedName(SERIALIZED_NAME_VALUE)
  private Object value;

  public static final String SERIALIZED_NAME_NAME = "name";
  @SerializedName(SERIALIZED_NAME_NAME)
  private String name;

  public static final String SERIALIZED_NAME_RELATES_TO = "relatesTo";
  @SerializedName(SERIALIZED_NAME_RELATES_TO)
  private ConversationReference relatesTo;

  public static final String SERIALIZED_NAME_CODE = "code";
  @SerializedName(SERIALIZED_NAME_CODE)
  private String code;

  public static final String SERIALIZED_NAME_EXPIRATION = "expiration";
  @SerializedName(SERIALIZED_NAME_EXPIRATION)
  private OffsetDateTime expiration;

  public static final String SERIALIZED_NAME_IMPORTANCE = "importance";
  @SerializedName(SERIALIZED_NAME_IMPORTANCE)
  private String importance;

  public static final String SERIALIZED_NAME_DELIVERY_MODE = "deliveryMode";
  @SerializedName(SERIALIZED_NAME_DELIVERY_MODE)
  private String deliveryMode;

  public static final String SERIALIZED_NAME_LISTEN_FOR = "listenFor";
  @SerializedName(SERIALIZED_NAME_LISTEN_FOR)
  private List<String> listenFor = null;

  public static final String SERIALIZED_NAME_TEXT_HIGHLIGHTS = "textHighlights";
  @SerializedName(SERIALIZED_NAME_TEXT_HIGHLIGHTS)
  private List<TextHighlight> textHighlights = null;

  public static final String SERIALIZED_NAME_SEMANTIC_ACTION = "semanticAction";
  @SerializedName(SERIALIZED_NAME_SEMANTIC_ACTION)
  private SemanticAction semanticAction;

  public Activity() {
  }

  public Activity type(String type) {
    
    this.type = type;
    return this;
  }

   /**
   * Contains the activity type.
   * @return type
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Contains the activity type.")

  public String getType() {
    return type;
  }


  public void setType(String type) {
    this.type = type;
  }


  public Activity id(String id) {
    
    this.id = id;
    return this;
  }

   /**
   * Contains an ID that uniquely identifies the activity on the channel.
   * @return id
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Contains an ID that uniquely identifies the activity on the channel.")

  public String getId() {
    return id;
  }


  public void setId(String id) {
    this.id = id;
  }


  public Activity timestamp(OffsetDateTime timestamp) {
    
    this.timestamp = timestamp;
    return this;
  }

   /**
   * Contains the date and time that the message was sent, in UTC, expressed in ISO-8601 format.
   * @return timestamp
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Contains the date and time that the message was sent, in UTC, expressed in ISO-8601 format.")

  public OffsetDateTime getTimestamp() {
    return timestamp;
  }


  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }


  public Activity localTimestamp(OffsetDateTime localTimestamp) {
    
    this.localTimestamp = localTimestamp;
    return this;
  }

   /**
   * Contains the local date and time of the message, expressed in ISO-8601 format.  For example, 2016-09-23T13:07:49.4714686-07:00.
   * @return localTimestamp
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Contains the local date and time of the message, expressed in ISO-8601 format.  For example, 2016-09-23T13:07:49.4714686-07:00.")

  public OffsetDateTime getLocalTimestamp() {
    return localTimestamp;
  }


  public void setLocalTimestamp(OffsetDateTime localTimestamp) {
    this.localTimestamp = localTimestamp;
  }


  public Activity localTimezone(String localTimezone) {
    
    this.localTimezone = localTimezone;
    return this;
  }

   /**
   * Contains the name of the local timezone of the message, expressed in IANA Time Zone database format.  For example, America/Los_Angeles.
   * @return localTimezone
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Contains the name of the local timezone of the message, expressed in IANA Time Zone database format.  For example, America/Los_Angeles.")

  public String getLocalTimezone() {
    return localTimezone;
  }


  public void setLocalTimezone(String localTimezone) {
    this.localTimezone = localTimezone;
  }


  public Activity serviceUrl(String serviceUrl) {
    
    this.serviceUrl = serviceUrl;
    return this;
  }

   /**
   * Contains the URL that specifies the channel&#39;s service endpoint. Set by the channel.
   * @return serviceUrl
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Contains the URL that specifies the channel's service endpoint. Set by the channel.")

  public String getServiceUrl() {
    return serviceUrl;
  }


  public void setServiceUrl(String serviceUrl) {
    this.serviceUrl = serviceUrl;
  }


  public Activity channelId(String channelId) {
    
    this.channelId = channelId;
    return this;
  }

   /**
   * Contains an ID that uniquely identifies the channel. Set by the channel.
   * @return channelId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Contains an ID that uniquely identifies the channel. Set by the channel.")

  public String getChannelId() {
    return channelId;
  }


  public void setChannelId(String channelId) {
    this.channelId = channelId;
  }


  public Activity from(ChannelAccount from) {
    
    this.from = from;
    return this;
  }

   /**
   * Get from
   * @return from
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public ChannelAccount getFrom() {
    return from;
  }


  public void setFrom(ChannelAccount from) {
    this.from = from;
  }


  public Activity conversation(ConversationAccount conversation) {
    
    this.conversation = conversation;
    return this;
  }

   /**
   * Get conversation
   * @return conversation
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public ConversationAccount getConversation() {
    return conversation;
  }


  public void setConversation(ConversationAccount conversation) {
    this.conversation = conversation;
  }


  public Activity recipient(ChannelAccount recipient) {
    
    this.recipient = recipient;
    return this;
  }

   /**
   * Get recipient
   * @return recipient
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public ChannelAccount getRecipient() {
    return recipient;
  }


  public void setRecipient(ChannelAccount recipient) {
    this.recipient = recipient;
  }


  public Activity textFormat(String textFormat) {
    
    this.textFormat = textFormat;
    return this;
  }

   /**
   * Format of text fields Default:markdown
   * @return textFormat
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Format of text fields Default:markdown")

  public String getTextFormat() {
    return textFormat;
  }


  public void setTextFormat(String textFormat) {
    this.textFormat = textFormat;
  }


  public Activity attachmentLayout(String attachmentLayout) {
    
    this.attachmentLayout = attachmentLayout;
    return this;
  }

   /**
   * The layout hint for multiple attachments. Default: list.
   * @return attachmentLayout
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The layout hint for multiple attachments. Default: list.")

  public String getAttachmentLayout() {
    return attachmentLayout;
  }


  public void setAttachmentLayout(String attachmentLayout) {
    this.attachmentLayout = attachmentLayout;
  }


  public Activity membersAdded(List<ChannelAccount> membersAdded) {
    
    this.membersAdded = membersAdded;
    return this;
  }

  public Activity addMembersAddedItem(ChannelAccount membersAddedItem) {
    if (this.membersAdded == null) {
      this.membersAdded = new ArrayList<>();
    }
    this.membersAdded.add(membersAddedItem);
    return this;
  }

   /**
   * The collection of members added to the conversation.
   * @return membersAdded
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The collection of members added to the conversation.")

  public List<ChannelAccount> getMembersAdded() {
    return membersAdded;
  }


  public void setMembersAdded(List<ChannelAccount> membersAdded) {
    this.membersAdded = membersAdded;
  }


  public Activity membersRemoved(List<ChannelAccount> membersRemoved) {
    
    this.membersRemoved = membersRemoved;
    return this;
  }

  public Activity addMembersRemovedItem(ChannelAccount membersRemovedItem) {
    if (this.membersRemoved == null) {
      this.membersRemoved = new ArrayList<>();
    }
    this.membersRemoved.add(membersRemovedItem);
    return this;
  }

   /**
   * The collection of members removed from the conversation.
   * @return membersRemoved
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The collection of members removed from the conversation.")

  public List<ChannelAccount> getMembersRemoved() {
    return membersRemoved;
  }


  public void setMembersRemoved(List<ChannelAccount> membersRemoved) {
    this.membersRemoved = membersRemoved;
  }


  public Activity reactionsAdded(List<MessageReaction> reactionsAdded) {
    
    this.reactionsAdded = reactionsAdded;
    return this;
  }

  public Activity addReactionsAddedItem(MessageReaction reactionsAddedItem) {
    if (this.reactionsAdded == null) {
      this.reactionsAdded = new ArrayList<>();
    }
    this.reactionsAdded.add(reactionsAddedItem);
    return this;
  }

   /**
   * The collection of reactions added to the conversation.
   * @return reactionsAdded
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The collection of reactions added to the conversation.")

  public List<MessageReaction> getReactionsAdded() {
    return reactionsAdded;
  }


  public void setReactionsAdded(List<MessageReaction> reactionsAdded) {
    this.reactionsAdded = reactionsAdded;
  }


  public Activity reactionsRemoved(List<MessageReaction> reactionsRemoved) {
    
    this.reactionsRemoved = reactionsRemoved;
    return this;
  }

  public Activity addReactionsRemovedItem(MessageReaction reactionsRemovedItem) {
    if (this.reactionsRemoved == null) {
      this.reactionsRemoved = new ArrayList<>();
    }
    this.reactionsRemoved.add(reactionsRemovedItem);
    return this;
  }

   /**
   * The collection of reactions removed from the conversation.
   * @return reactionsRemoved
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The collection of reactions removed from the conversation.")

  public List<MessageReaction> getReactionsRemoved() {
    return reactionsRemoved;
  }


  public void setReactionsRemoved(List<MessageReaction> reactionsRemoved) {
    this.reactionsRemoved = reactionsRemoved;
  }


  public Activity topicName(String topicName) {
    
    this.topicName = topicName;
    return this;
  }

   /**
   * The updated topic name of the conversation.
   * @return topicName
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The updated topic name of the conversation.")

  public String getTopicName() {
    return topicName;
  }


  public void setTopicName(String topicName) {
    this.topicName = topicName;
  }


  public Activity historyDisclosed(Boolean historyDisclosed) {
    
    this.historyDisclosed = historyDisclosed;
    return this;
  }

   /**
   * Indicates whether the prior history of the channel is disclosed.
   * @return historyDisclosed
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Indicates whether the prior history of the channel is disclosed.")

  public Boolean getHistoryDisclosed() {
    return historyDisclosed;
  }


  public void setHistoryDisclosed(Boolean historyDisclosed) {
    this.historyDisclosed = historyDisclosed;
  }


  public Activity locale(String locale) {
    
    this.locale = locale;
    return this;
  }

   /**
   * A locale name for the contents of the text field.  The locale name is a combination of an ISO 639 two- or three-letter culture code associated with a language  and an ISO 3166 two-letter subculture code associated with a country or region.  The locale name can also correspond to a valid BCP-47 language tag.
   * @return locale
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "A locale name for the contents of the text field.  The locale name is a combination of an ISO 639 two- or three-letter culture code associated with a language  and an ISO 3166 two-letter subculture code associated with a country or region.  The locale name can also correspond to a valid BCP-47 language tag.")

  public String getLocale() {
    return locale;
  }


  public void setLocale(String locale) {
    this.locale = locale;
  }


  public Activity text(String text) {
    
    this.text = text;
    return this;
  }

   /**
   * The text content of the message.
   * @return text
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The text content of the message.")

  public String getText() {
    return text;
  }


  public void setText(String text) {
    this.text = text;
  }


  public Activity speak(String speak) {
    
    this.speak = speak;
    return this;
  }

   /**
   * The text to speak.
   * @return speak
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The text to speak.")

  public String getSpeak() {
    return speak;
  }


  public void setSpeak(String speak) {
    this.speak = speak;
  }


  public Activity inputHint(String inputHint) {
    
    this.inputHint = inputHint;
    return this;
  }

   /**
   * Indicates whether your bot is accepting,  expecting, or ignoring user input after the message is delivered to the client.
   * @return inputHint
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Indicates whether your bot is accepting,  expecting, or ignoring user input after the message is delivered to the client.")

  public String getInputHint() {
    return inputHint;
  }


  public void setInputHint(String inputHint) {
    this.inputHint = inputHint;
  }


  public Activity summary(String summary) {
    
    this.summary = summary;
    return this;
  }

   /**
   * The text to display if the channel cannot render cards.
   * @return summary
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The text to display if the channel cannot render cards.")

  public String getSummary() {
    return summary;
  }


  public void setSummary(String summary) {
    this.summary = summary;
  }


  public Activity suggestedActions(SuggestedActions suggestedActions) {
    
    this.suggestedActions = suggestedActions;
    return this;
  }

   /**
   * Get suggestedActions
   * @return suggestedActions
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public SuggestedActions getSuggestedActions() {
    return suggestedActions;
  }


  public void setSuggestedActions(SuggestedActions suggestedActions) {
    this.suggestedActions = suggestedActions;
  }


  public Activity attachments(List<Attachment> attachments) {
    
    this.attachments = attachments;
    return this;
  }

  public Activity addAttachmentsItem(Attachment attachmentsItem) {
    if (this.attachments == null) {
      this.attachments = new ArrayList<>();
    }
    this.attachments.add(attachmentsItem);
    return this;
  }

   /**
   * Attachments
   * @return attachments
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Attachments")

  public List<Attachment> getAttachments() {
    return attachments;
  }


  public void setAttachments(List<Attachment> attachments) {
    this.attachments = attachments;
  }


  public Activity entities(List<Entity> entities) {
    
    this.entities = entities;
    return this;
  }

  public Activity addEntitiesItem(Entity entitiesItem) {
    if (this.entities == null) {
      this.entities = new ArrayList<>();
    }
    this.entities.add(entitiesItem);
    return this;
  }

   /**
   * Represents the entities that were mentioned in the message.
   * @return entities
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Represents the entities that were mentioned in the message.")

  public List<Entity> getEntities() {
    return entities;
  }


  public void setEntities(List<Entity> entities) {
    this.entities = entities;
  }


  public Activity channelData(Object channelData) {
    
    this.channelData = channelData;
    return this;
  }

   /**
   * Contains channel-specific content.
   * @return channelData
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Contains channel-specific content.")

  public Object getChannelData() {
    return channelData;
  }


  public void setChannelData(Object channelData) {
    this.channelData = channelData;
  }


  public Activity action(String action) {
    
    this.action = action;
    return this;
  }

   /**
   * Indicates whether the recipient of a contactRelationUpdate was added or removed from the sender&#39;s contact list.
   * @return action
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Indicates whether the recipient of a contactRelationUpdate was added or removed from the sender's contact list.")

  public String getAction() {
    return action;
  }


  public void setAction(String action) {
    this.action = action;
  }


  public Activity replyToId(String replyToId) {
    
    this.replyToId = replyToId;
    return this;
  }

   /**
   * Contains the ID of the message to which this message is a reply.
   * @return replyToId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Contains the ID of the message to which this message is a reply.")

  public String getReplyToId() {
    return replyToId;
  }


  public void setReplyToId(String replyToId) {
    this.replyToId = replyToId;
  }


  public Activity label(String label) {
    
    this.label = label;
    return this;
  }

   /**
   * A descriptive label for the activity.
   * @return label
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "A descriptive label for the activity.")

  public String getLabel() {
    return label;
  }


  public void setLabel(String label) {
    this.label = label;
  }


  public Activity valueType(String valueType) {
    
    this.valueType = valueType;
    return this;
  }

   /**
   * The type of the activity&#39;s value object.
   * @return valueType
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The type of the activity's value object.")

  public String getValueType() {
    return valueType;
  }


  public void setValueType(String valueType) {
    this.valueType = valueType;
  }


  public Activity value(Object value) {
    
    this.value = value;
    return this;
  }

   /**
   * A value that is associated with the activity.
   * @return value
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "A value that is associated with the activity.")

  public Object getValue() {
    return value;
  }


  public void setValue(Object value) {
    this.value = value;
  }


  public Activity name(String name) {
    
    this.name = name;
    return this;
  }

   /**
   * The name of the operation associated with an invoke or event activity.
   * @return name
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The name of the operation associated with an invoke or event activity.")

  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
  }


  public Activity relatesTo(ConversationReference relatesTo) {
    
    this.relatesTo = relatesTo;
    return this;
  }

   /**
   * Get relatesTo
   * @return relatesTo
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public ConversationReference getRelatesTo() {
    return relatesTo;
  }


  public void setRelatesTo(ConversationReference relatesTo) {
    this.relatesTo = relatesTo;
  }


  public Activity code(String code) {
    
    this.code = code;
    return this;
  }

   /**
   * The a code for endOfConversation activities that indicates why the conversation ended.
   * @return code
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The a code for endOfConversation activities that indicates why the conversation ended.")

  public String getCode() {
    return code;
  }


  public void setCode(String code) {
    this.code = code;
  }


  public Activity expiration(OffsetDateTime expiration) {
    
    this.expiration = expiration;
    return this;
  }

   /**
   * The time at which the activity should be considered to be \&quot;expired\&quot; and should not be presented to the recipient.
   * @return expiration
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The time at which the activity should be considered to be \"expired\" and should not be presented to the recipient.")

  public OffsetDateTime getExpiration() {
    return expiration;
  }


  public void setExpiration(OffsetDateTime expiration) {
    this.expiration = expiration;
  }


  public Activity importance(String importance) {
    
    this.importance = importance;
    return this;
  }

   /**
   * The importance of the activity.
   * @return importance
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The importance of the activity.")

  public String getImportance() {
    return importance;
  }


  public void setImportance(String importance) {
    this.importance = importance;
  }


  public Activity deliveryMode(String deliveryMode) {
    
    this.deliveryMode = deliveryMode;
    return this;
  }

   /**
   * A delivery hint to signal to the recipient alternate delivery paths for the activity.  The default delivery mode is \&quot;default\&quot;.
   * @return deliveryMode
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "A delivery hint to signal to the recipient alternate delivery paths for the activity.  The default delivery mode is \"default\".")

  public String getDeliveryMode() {
    return deliveryMode;
  }


  public void setDeliveryMode(String deliveryMode) {
    this.deliveryMode = deliveryMode;
  }


  public Activity listenFor(List<String> listenFor) {
    
    this.listenFor = listenFor;
    return this;
  }

  public Activity addListenForItem(String listenForItem) {
    if (this.listenFor == null) {
      this.listenFor = new ArrayList<>();
    }
    this.listenFor.add(listenForItem);
    return this;
  }

   /**
   * List of phrases and references that speech and language priming systems should listen for
   * @return listenFor
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "List of phrases and references that speech and language priming systems should listen for")

  public List<String> getListenFor() {
    return listenFor;
  }


  public void setListenFor(List<String> listenFor) {
    this.listenFor = listenFor;
  }


  public Activity textHighlights(List<TextHighlight> textHighlights) {
    
    this.textHighlights = textHighlights;
    return this;
  }

  public Activity addTextHighlightsItem(TextHighlight textHighlightsItem) {
    if (this.textHighlights == null) {
      this.textHighlights = new ArrayList<>();
    }
    this.textHighlights.add(textHighlightsItem);
    return this;
  }

   /**
   * The collection of text fragments to highlight when the activity contains a ReplyToId value.
   * @return textHighlights
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The collection of text fragments to highlight when the activity contains a ReplyToId value.")

  public List<TextHighlight> getTextHighlights() {
    return textHighlights;
  }


  public void setTextHighlights(List<TextHighlight> textHighlights) {
    this.textHighlights = textHighlights;
  }


  public Activity semanticAction(SemanticAction semanticAction) {
    
    this.semanticAction = semanticAction;
    return this;
  }

   /**
   * Get semanticAction
   * @return semanticAction
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public SemanticAction getSemanticAction() {
    return semanticAction;
  }


  public void setSemanticAction(SemanticAction semanticAction) {
    this.semanticAction = semanticAction;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Activity activity = (Activity) o;
    return Objects.equals(this.type, activity.type) &&
        Objects.equals(this.id, activity.id) &&
        Objects.equals(this.timestamp, activity.timestamp) &&
        Objects.equals(this.localTimestamp, activity.localTimestamp) &&
        Objects.equals(this.localTimezone, activity.localTimezone) &&
        Objects.equals(this.serviceUrl, activity.serviceUrl) &&
        Objects.equals(this.channelId, activity.channelId) &&
        Objects.equals(this.from, activity.from) &&
        Objects.equals(this.conversation, activity.conversation) &&
        Objects.equals(this.recipient, activity.recipient) &&
        Objects.equals(this.textFormat, activity.textFormat) &&
        Objects.equals(this.attachmentLayout, activity.attachmentLayout) &&
        Objects.equals(this.membersAdded, activity.membersAdded) &&
        Objects.equals(this.membersRemoved, activity.membersRemoved) &&
        Objects.equals(this.reactionsAdded, activity.reactionsAdded) &&
        Objects.equals(this.reactionsRemoved, activity.reactionsRemoved) &&
        Objects.equals(this.topicName, activity.topicName) &&
        Objects.equals(this.historyDisclosed, activity.historyDisclosed) &&
        Objects.equals(this.locale, activity.locale) &&
        Objects.equals(this.text, activity.text) &&
        Objects.equals(this.speak, activity.speak) &&
        Objects.equals(this.inputHint, activity.inputHint) &&
        Objects.equals(this.summary, activity.summary) &&
        Objects.equals(this.suggestedActions, activity.suggestedActions) &&
        Objects.equals(this.attachments, activity.attachments) &&
        Objects.equals(this.entities, activity.entities) &&
        Objects.equals(this.channelData, activity.channelData) &&
        Objects.equals(this.action, activity.action) &&
        Objects.equals(this.replyToId, activity.replyToId) &&
        Objects.equals(this.label, activity.label) &&
        Objects.equals(this.valueType, activity.valueType) &&
        Objects.equals(this.value, activity.value) &&
        Objects.equals(this.name, activity.name) &&
        Objects.equals(this.relatesTo, activity.relatesTo) &&
        Objects.equals(this.code, activity.code) &&
        Objects.equals(this.expiration, activity.expiration) &&
        Objects.equals(this.importance, activity.importance) &&
        Objects.equals(this.deliveryMode, activity.deliveryMode) &&
        Objects.equals(this.listenFor, activity.listenFor) &&
        Objects.equals(this.textHighlights, activity.textHighlights) &&
        Objects.equals(this.semanticAction, activity.semanticAction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, id, timestamp, localTimestamp, localTimezone, serviceUrl, channelId, from, conversation, recipient, textFormat, attachmentLayout, membersAdded, membersRemoved, reactionsAdded, reactionsRemoved, topicName, historyDisclosed, locale, text, speak, inputHint, summary, suggestedActions, attachments, entities, channelData, action, replyToId, label, valueType, value, name, relatesTo, code, expiration, importance, deliveryMode, listenFor, textHighlights, semanticAction);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Activity {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    localTimestamp: ").append(toIndentedString(localTimestamp)).append("\n");
    sb.append("    localTimezone: ").append(toIndentedString(localTimezone)).append("\n");
    sb.append("    serviceUrl: ").append(toIndentedString(serviceUrl)).append("\n");
    sb.append("    channelId: ").append(toIndentedString(channelId)).append("\n");
    sb.append("    from: ").append(toIndentedString(from)).append("\n");
    sb.append("    conversation: ").append(toIndentedString(conversation)).append("\n");
    sb.append("    recipient: ").append(toIndentedString(recipient)).append("\n");
    sb.append("    textFormat: ").append(toIndentedString(textFormat)).append("\n");
    sb.append("    attachmentLayout: ").append(toIndentedString(attachmentLayout)).append("\n");
    sb.append("    membersAdded: ").append(toIndentedString(membersAdded)).append("\n");
    sb.append("    membersRemoved: ").append(toIndentedString(membersRemoved)).append("\n");
    sb.append("    reactionsAdded: ").append(toIndentedString(reactionsAdded)).append("\n");
    sb.append("    reactionsRemoved: ").append(toIndentedString(reactionsRemoved)).append("\n");
    sb.append("    topicName: ").append(toIndentedString(topicName)).append("\n");
    sb.append("    historyDisclosed: ").append(toIndentedString(historyDisclosed)).append("\n");
    sb.append("    locale: ").append(toIndentedString(locale)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    speak: ").append(toIndentedString(speak)).append("\n");
    sb.append("    inputHint: ").append(toIndentedString(inputHint)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
    sb.append("    suggestedActions: ").append(toIndentedString(suggestedActions)).append("\n");
    sb.append("    attachments: ").append(toIndentedString(attachments)).append("\n");
    sb.append("    entities: ").append(toIndentedString(entities)).append("\n");
    sb.append("    channelData: ").append(toIndentedString(channelData)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    replyToId: ").append(toIndentedString(replyToId)).append("\n");
    sb.append("    label: ").append(toIndentedString(label)).append("\n");
    sb.append("    valueType: ").append(toIndentedString(valueType)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    relatesTo: ").append(toIndentedString(relatesTo)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    expiration: ").append(toIndentedString(expiration)).append("\n");
    sb.append("    importance: ").append(toIndentedString(importance)).append("\n");
    sb.append("    deliveryMode: ").append(toIndentedString(deliveryMode)).append("\n");
    sb.append("    listenFor: ").append(toIndentedString(listenFor)).append("\n");
    sb.append("    textHighlights: ").append(toIndentedString(textHighlights)).append("\n");
    sb.append("    semanticAction: ").append(toIndentedString(semanticAction)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }


  public static HashSet<String> openapiFields;
  public static HashSet<String> openapiRequiredFields;

  static {
    // a set of all properties/fields (JSON key names)
    openapiFields = new HashSet<String>();
    openapiFields.add("type");
    openapiFields.add("id");
    openapiFields.add("timestamp");
    openapiFields.add("localTimestamp");
    openapiFields.add("localTimezone");
    openapiFields.add("serviceUrl");
    openapiFields.add("channelId");
    openapiFields.add("from");
    openapiFields.add("conversation");
    openapiFields.add("recipient");
    openapiFields.add("textFormat");
    openapiFields.add("attachmentLayout");
    openapiFields.add("membersAdded");
    openapiFields.add("membersRemoved");
    openapiFields.add("reactionsAdded");
    openapiFields.add("reactionsRemoved");
    openapiFields.add("topicName");
    openapiFields.add("historyDisclosed");
    openapiFields.add("locale");
    openapiFields.add("text");
    openapiFields.add("speak");
    openapiFields.add("inputHint");
    openapiFields.add("summary");
    openapiFields.add("suggestedActions");
    openapiFields.add("attachments");
    openapiFields.add("entities");
    openapiFields.add("channelData");
    openapiFields.add("action");
    openapiFields.add("replyToId");
    openapiFields.add("label");
    openapiFields.add("valueType");
    openapiFields.add("value");
    openapiFields.add("name");
    openapiFields.add("relatesTo");
    openapiFields.add("code");
    openapiFields.add("expiration");
    openapiFields.add("importance");
    openapiFields.add("deliveryMode");
    openapiFields.add("listenFor");
    openapiFields.add("textHighlights");
    openapiFields.add("semanticAction");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

 /**
  * Validates the JSON Object and throws an exception if issues found
  *
  * @param jsonObj JSON Object
  * @throws IOException if the JSON Object is invalid with respect to Activity
  */
  public static void validateJsonObject(JsonObject jsonObj) throws IOException {
      if (jsonObj == null) {
        if (Activity.openapiRequiredFields.isEmpty()) {
          return;
        } else { // has required fields
          throw new IllegalArgumentException(String.format("The required field(s) %s in Activity is not found in the empty JSON string", Activity.openapiRequiredFields.toString()));
        }
      }

      Set<Entry<String, JsonElement>> entries = jsonObj.entrySet();
      // check to see if the JSON string contains additional fields
      for (Entry<String, JsonElement> entry : entries) {
        if (!Activity.openapiFields.contains(entry.getKey())) {
          throw new IllegalArgumentException(String.format("The field `%s` in the JSON string is not defined in the `Activity` properties. JSON: %s", entry.getKey(), jsonObj.toString()));
        }
      }
      if ((jsonObj.get("type") != null && !jsonObj.get("type").isJsonNull()) && !jsonObj.get("type").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `type` to be a primitive type in the JSON string but got `%s`", jsonObj.get("type").toString()));
      }
      if ((jsonObj.get("id") != null && !jsonObj.get("id").isJsonNull()) && !jsonObj.get("id").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `id` to be a primitive type in the JSON string but got `%s`", jsonObj.get("id").toString()));
      }
      if ((jsonObj.get("localTimezone") != null && !jsonObj.get("localTimezone").isJsonNull()) && !jsonObj.get("localTimezone").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `localTimezone` to be a primitive type in the JSON string but got `%s`", jsonObj.get("localTimezone").toString()));
      }
      if ((jsonObj.get("serviceUrl") != null && !jsonObj.get("serviceUrl").isJsonNull()) && !jsonObj.get("serviceUrl").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `serviceUrl` to be a primitive type in the JSON string but got `%s`", jsonObj.get("serviceUrl").toString()));
      }
      if ((jsonObj.get("channelId") != null && !jsonObj.get("channelId").isJsonNull()) && !jsonObj.get("channelId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `channelId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("channelId").toString()));
      }
      // validate the optional field `from`
      if (jsonObj.get("from") != null && !jsonObj.get("from").isJsonNull()) {
        ChannelAccount.validateJsonObject(jsonObj.getAsJsonObject("from"));
      }
      // validate the optional field `conversation`
      if (jsonObj.get("conversation") != null && !jsonObj.get("conversation").isJsonNull()) {
        ConversationAccount.validateJsonObject(jsonObj.getAsJsonObject("conversation"));
      }
      // validate the optional field `recipient`
      if (jsonObj.get("recipient") != null && !jsonObj.get("recipient").isJsonNull()) {
        ChannelAccount.validateJsonObject(jsonObj.getAsJsonObject("recipient"));
      }
      if ((jsonObj.get("textFormat") != null && !jsonObj.get("textFormat").isJsonNull()) && !jsonObj.get("textFormat").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `textFormat` to be a primitive type in the JSON string but got `%s`", jsonObj.get("textFormat").toString()));
      }
      if ((jsonObj.get("attachmentLayout") != null && !jsonObj.get("attachmentLayout").isJsonNull()) && !jsonObj.get("attachmentLayout").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `attachmentLayout` to be a primitive type in the JSON string but got `%s`", jsonObj.get("attachmentLayout").toString()));
      }
      if (jsonObj.get("membersAdded") != null && !jsonObj.get("membersAdded").isJsonNull()) {
        JsonArray jsonArraymembersAdded = jsonObj.getAsJsonArray("membersAdded");
        if (jsonArraymembersAdded != null) {
          // ensure the json data is an array
          if (!jsonObj.get("membersAdded").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `membersAdded` to be an array in the JSON string but got `%s`", jsonObj.get("membersAdded").toString()));
          }

          // validate the optional field `membersAdded` (array)
          for (int i = 0; i < jsonArraymembersAdded.size(); i++) {
            ChannelAccount.validateJsonObject(jsonArraymembersAdded.get(i).getAsJsonObject());
          };
        }
      }
      if (jsonObj.get("membersRemoved") != null && !jsonObj.get("membersRemoved").isJsonNull()) {
        JsonArray jsonArraymembersRemoved = jsonObj.getAsJsonArray("membersRemoved");
        if (jsonArraymembersRemoved != null) {
          // ensure the json data is an array
          if (!jsonObj.get("membersRemoved").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `membersRemoved` to be an array in the JSON string but got `%s`", jsonObj.get("membersRemoved").toString()));
          }

          // validate the optional field `membersRemoved` (array)
          for (int i = 0; i < jsonArraymembersRemoved.size(); i++) {
            ChannelAccount.validateJsonObject(jsonArraymembersRemoved.get(i).getAsJsonObject());
          };
        }
      }
      if (jsonObj.get("reactionsAdded") != null && !jsonObj.get("reactionsAdded").isJsonNull()) {
        JsonArray jsonArrayreactionsAdded = jsonObj.getAsJsonArray("reactionsAdded");
        if (jsonArrayreactionsAdded != null) {
          // ensure the json data is an array
          if (!jsonObj.get("reactionsAdded").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `reactionsAdded` to be an array in the JSON string but got `%s`", jsonObj.get("reactionsAdded").toString()));
          }

          // validate the optional field `reactionsAdded` (array)
          for (int i = 0; i < jsonArrayreactionsAdded.size(); i++) {
            MessageReaction.validateJsonObject(jsonArrayreactionsAdded.get(i).getAsJsonObject());
          };
        }
      }
      if (jsonObj.get("reactionsRemoved") != null && !jsonObj.get("reactionsRemoved").isJsonNull()) {
        JsonArray jsonArrayreactionsRemoved = jsonObj.getAsJsonArray("reactionsRemoved");
        if (jsonArrayreactionsRemoved != null) {
          // ensure the json data is an array
          if (!jsonObj.get("reactionsRemoved").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `reactionsRemoved` to be an array in the JSON string but got `%s`", jsonObj.get("reactionsRemoved").toString()));
          }

          // validate the optional field `reactionsRemoved` (array)
          for (int i = 0; i < jsonArrayreactionsRemoved.size(); i++) {
            MessageReaction.validateJsonObject(jsonArrayreactionsRemoved.get(i).getAsJsonObject());
          };
        }
      }
      if ((jsonObj.get("topicName") != null && !jsonObj.get("topicName").isJsonNull()) && !jsonObj.get("topicName").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `topicName` to be a primitive type in the JSON string but got `%s`", jsonObj.get("topicName").toString()));
      }
      if ((jsonObj.get("locale") != null && !jsonObj.get("locale").isJsonNull()) && !jsonObj.get("locale").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `locale` to be a primitive type in the JSON string but got `%s`", jsonObj.get("locale").toString()));
      }
      if ((jsonObj.get("text") != null && !jsonObj.get("text").isJsonNull()) && !jsonObj.get("text").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `text` to be a primitive type in the JSON string but got `%s`", jsonObj.get("text").toString()));
      }
      if ((jsonObj.get("speak") != null && !jsonObj.get("speak").isJsonNull()) && !jsonObj.get("speak").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `speak` to be a primitive type in the JSON string but got `%s`", jsonObj.get("speak").toString()));
      }
      if ((jsonObj.get("inputHint") != null && !jsonObj.get("inputHint").isJsonNull()) && !jsonObj.get("inputHint").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `inputHint` to be a primitive type in the JSON string but got `%s`", jsonObj.get("inputHint").toString()));
      }
      if ((jsonObj.get("summary") != null && !jsonObj.get("summary").isJsonNull()) && !jsonObj.get("summary").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `summary` to be a primitive type in the JSON string but got `%s`", jsonObj.get("summary").toString()));
      }
      // validate the optional field `suggestedActions`
      if (jsonObj.get("suggestedActions") != null && !jsonObj.get("suggestedActions").isJsonNull()) {
        SuggestedActions.validateJsonObject(jsonObj.getAsJsonObject("suggestedActions"));
      }
      if (jsonObj.get("attachments") != null && !jsonObj.get("attachments").isJsonNull()) {
        JsonArray jsonArrayattachments = jsonObj.getAsJsonArray("attachments");
        if (jsonArrayattachments != null) {
          // ensure the json data is an array
          if (!jsonObj.get("attachments").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `attachments` to be an array in the JSON string but got `%s`", jsonObj.get("attachments").toString()));
          }

          // validate the optional field `attachments` (array)
          for (int i = 0; i < jsonArrayattachments.size(); i++) {
            Attachment.validateJsonObject(jsonArrayattachments.get(i).getAsJsonObject());
          };
        }
      }
      if (jsonObj.get("entities") != null && !jsonObj.get("entities").isJsonNull()) {
        JsonArray jsonArrayentities = jsonObj.getAsJsonArray("entities");
        if (jsonArrayentities != null) {
          // ensure the json data is an array
          if (!jsonObj.get("entities").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `entities` to be an array in the JSON string but got `%s`", jsonObj.get("entities").toString()));
          }

          // validate the optional field `entities` (array)
          for (int i = 0; i < jsonArrayentities.size(); i++) {
            Entity.validateJsonObject(jsonArrayentities.get(i).getAsJsonObject());
          };
        }
      }
      if ((jsonObj.get("action") != null && !jsonObj.get("action").isJsonNull()) && !jsonObj.get("action").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `action` to be a primitive type in the JSON string but got `%s`", jsonObj.get("action").toString()));
      }
      if ((jsonObj.get("replyToId") != null && !jsonObj.get("replyToId").isJsonNull()) && !jsonObj.get("replyToId").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `replyToId` to be a primitive type in the JSON string but got `%s`", jsonObj.get("replyToId").toString()));
      }
      if ((jsonObj.get("label") != null && !jsonObj.get("label").isJsonNull()) && !jsonObj.get("label").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `label` to be a primitive type in the JSON string but got `%s`", jsonObj.get("label").toString()));
      }
      if ((jsonObj.get("valueType") != null && !jsonObj.get("valueType").isJsonNull()) && !jsonObj.get("valueType").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `valueType` to be a primitive type in the JSON string but got `%s`", jsonObj.get("valueType").toString()));
      }
      if ((jsonObj.get("name") != null && !jsonObj.get("name").isJsonNull()) && !jsonObj.get("name").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("name").toString()));
      }
      // validate the optional field `relatesTo`
      if (jsonObj.get("relatesTo") != null && !jsonObj.get("relatesTo").isJsonNull()) {
        ConversationReference.validateJsonObject(jsonObj.getAsJsonObject("relatesTo"));
      }
      if ((jsonObj.get("code") != null && !jsonObj.get("code").isJsonNull()) && !jsonObj.get("code").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `code` to be a primitive type in the JSON string but got `%s`", jsonObj.get("code").toString()));
      }
      if ((jsonObj.get("importance") != null && !jsonObj.get("importance").isJsonNull()) && !jsonObj.get("importance").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `importance` to be a primitive type in the JSON string but got `%s`", jsonObj.get("importance").toString()));
      }
      if ((jsonObj.get("deliveryMode") != null && !jsonObj.get("deliveryMode").isJsonNull()) && !jsonObj.get("deliveryMode").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `deliveryMode` to be a primitive type in the JSON string but got `%s`", jsonObj.get("deliveryMode").toString()));
      }
      // ensure the json data is an array
      if ((jsonObj.get("listenFor") != null && !jsonObj.get("listenFor").isJsonNull()) && !jsonObj.get("listenFor").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `listenFor` to be an array in the JSON string but got `%s`", jsonObj.get("listenFor").toString()));
      }
      if (jsonObj.get("textHighlights") != null && !jsonObj.get("textHighlights").isJsonNull()) {
        JsonArray jsonArraytextHighlights = jsonObj.getAsJsonArray("textHighlights");
        if (jsonArraytextHighlights != null) {
          // ensure the json data is an array
          if (!jsonObj.get("textHighlights").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `textHighlights` to be an array in the JSON string but got `%s`", jsonObj.get("textHighlights").toString()));
          }

          // validate the optional field `textHighlights` (array)
          for (int i = 0; i < jsonArraytextHighlights.size(); i++) {
            TextHighlight.validateJsonObject(jsonArraytextHighlights.get(i).getAsJsonObject());
          };
        }
      }
      // validate the optional field `semanticAction`
      if (jsonObj.get("semanticAction") != null && !jsonObj.get("semanticAction").isJsonNull()) {
        SemanticAction.validateJsonObject(jsonObj.getAsJsonObject("semanticAction"));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!Activity.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'Activity' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<Activity> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(Activity.class));

       return (TypeAdapter<T>) new TypeAdapter<Activity>() {
           @Override
           public void write(JsonWriter out, Activity value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             elementAdapter.write(out, obj);
           }

           @Override
           public Activity read(JsonReader in) throws IOException {
             JsonObject jsonObj = elementAdapter.read(in).getAsJsonObject();
             validateJsonObject(jsonObj);
             return thisAdapter.fromJsonTree(jsonObj);
           }

       }.nullSafe();
    }
  }

 /**
  * Create an instance of Activity given an JSON string
  *
  * @param jsonString JSON string
  * @return An instance of Activity
  * @throws IOException if the JSON string is invalid with respect to Activity
  */
  public static Activity fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, Activity.class);
  }

 /**
  * Convert an instance of Activity to an JSON string
  *
  * @return JSON string
  */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

