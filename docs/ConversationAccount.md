

# ConversationAccount

Conversation account represents the identity of the conversation within a channel

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**isGroup** | **Boolean** | Indicates whether the conversation contains more than two participants at the time the activity was generated |  [optional] |
|**conversationType** | **String** | Indicates the type of the conversation in channels that distinguish between conversation types |  [optional] |
|**tenantId** | **String** | This conversation&#39;s tenant ID |  [optional] |
|**id** | **String** | Channel id for the user or bot on this channel (Example: joe@smith.com, or @joesmith or 123456) |  [optional] |
|**name** | **String** | Display friendly name |  [optional] |
|**aadObjectId** | **String** | This account&#39;s object ID within Azure Active Directory (AAD) |  [optional] |
|**role** | **String** | Role of the entity behind the account (Example: User, Bot, etc.) |  [optional] |



