

# Activity

An Activity is the basic communication type for the Bot Framework 3.0 protocol.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**type** | **String** | Contains the activity type. |  [optional] |
|**id** | **String** | Contains an ID that uniquely identifies the activity on the channel. |  [optional] |
|**timestamp** | **OffsetDateTime** | Contains the date and time that the message was sent, in UTC, expressed in ISO-8601 format. |  [optional] |
|**localTimestamp** | **OffsetDateTime** | Contains the local date and time of the message, expressed in ISO-8601 format.  For example, 2016-09-23T13:07:49.4714686-07:00. |  [optional] |
|**localTimezone** | **String** | Contains the name of the local timezone of the message, expressed in IANA Time Zone database format.  For example, America/Los_Angeles. |  [optional] |
|**serviceUrl** | **String** | Contains the URL that specifies the channel&#39;s service endpoint. Set by the channel. |  [optional] |
|**channelId** | **String** | Contains an ID that uniquely identifies the channel. Set by the channel. |  [optional] |
|**from** | [**ChannelAccount**](ChannelAccount.md) |  |  [optional] |
|**conversation** | [**ConversationAccount**](ConversationAccount.md) |  |  [optional] |
|**recipient** | [**ChannelAccount**](ChannelAccount.md) |  |  [optional] |
|**textFormat** | **String** | Format of text fields Default:markdown |  [optional] |
|**attachmentLayout** | **String** | The layout hint for multiple attachments. Default: list. |  [optional] |
|**membersAdded** | [**List&lt;ChannelAccount&gt;**](ChannelAccount.md) | The collection of members added to the conversation. |  [optional] |
|**membersRemoved** | [**List&lt;ChannelAccount&gt;**](ChannelAccount.md) | The collection of members removed from the conversation. |  [optional] |
|**reactionsAdded** | [**List&lt;MessageReaction&gt;**](MessageReaction.md) | The collection of reactions added to the conversation. |  [optional] |
|**reactionsRemoved** | [**List&lt;MessageReaction&gt;**](MessageReaction.md) | The collection of reactions removed from the conversation. |  [optional] |
|**topicName** | **String** | The updated topic name of the conversation. |  [optional] |
|**historyDisclosed** | **Boolean** | Indicates whether the prior history of the channel is disclosed. |  [optional] |
|**locale** | **String** | A locale name for the contents of the text field.  The locale name is a combination of an ISO 639 two- or three-letter culture code associated with a language  and an ISO 3166 two-letter subculture code associated with a country or region.  The locale name can also correspond to a valid BCP-47 language tag. |  [optional] |
|**text** | **String** | The text content of the message. |  [optional] |
|**speak** | **String** | The text to speak. |  [optional] |
|**inputHint** | **String** | Indicates whether your bot is accepting,  expecting, or ignoring user input after the message is delivered to the client. |  [optional] |
|**summary** | **String** | The text to display if the channel cannot render cards. |  [optional] |
|**suggestedActions** | [**SuggestedActions**](SuggestedActions.md) |  |  [optional] |
|**attachments** | [**List&lt;Attachment&gt;**](Attachment.md) | Attachments |  [optional] |
|**entities** | [**List&lt;Entity&gt;**](Entity.md) | Represents the entities that were mentioned in the message. |  [optional] |
|**channelData** | **Object** | Contains channel-specific content. |  [optional] |
|**action** | **String** | Indicates whether the recipient of a contactRelationUpdate was added or removed from the sender&#39;s contact list. |  [optional] |
|**replyToId** | **String** | Contains the ID of the message to which this message is a reply. |  [optional] |
|**label** | **String** | A descriptive label for the activity. |  [optional] |
|**valueType** | **String** | The type of the activity&#39;s value object. |  [optional] |
|**value** | **Object** | A value that is associated with the activity. |  [optional] |
|**name** | **String** | The name of the operation associated with an invoke or event activity. |  [optional] |
|**relatesTo** | [**ConversationReference**](ConversationReference.md) |  |  [optional] |
|**code** | **String** | The a code for endOfConversation activities that indicates why the conversation ended. |  [optional] |
|**expiration** | **OffsetDateTime** | The time at which the activity should be considered to be \&quot;expired\&quot; and should not be presented to the recipient. |  [optional] |
|**importance** | **String** | The importance of the activity. |  [optional] |
|**deliveryMode** | **String** | A delivery hint to signal to the recipient alternate delivery paths for the activity.  The default delivery mode is \&quot;default\&quot;. |  [optional] |
|**listenFor** | **List&lt;String&gt;** | List of phrases and references that speech and language priming systems should listen for |  [optional] |
|**textHighlights** | [**List&lt;TextHighlight&gt;**](TextHighlight.md) | The collection of text fragments to highlight when the activity contains a ReplyToId value. |  [optional] |
|**semanticAction** | [**SemanticAction**](SemanticAction.md) |  |  [optional] |



