

# VideoCard

Video card

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**title** | **String** | Title of this card |  [optional] |
|**subtitle** | **String** | Subtitle of this card |  [optional] |
|**text** | **String** | Text of this card |  [optional] |
|**image** | [**ThumbnailUrl**](ThumbnailUrl.md) |  |  [optional] |
|**media** | [**List&lt;MediaUrl&gt;**](MediaUrl.md) | Media URLs for this card. When this field contains more than one URL, each URL is an alternative format of the same content. |  [optional] |
|**buttons** | [**List&lt;CardAction&gt;**](CardAction.md) | Actions on this card |  [optional] |
|**shareable** | **Boolean** | This content may be shared with others (default:true) |  [optional] |
|**autoloop** | **Boolean** | Should the client loop playback at end of content (default:true) |  [optional] |
|**autostart** | **Boolean** | Should the client automatically start playback of media in this card (default:true) |  [optional] |
|**aspect** | **String** | Aspect ratio of thumbnail/media placeholder. Allowed values are \&quot;16:9\&quot; and \&quot;4:3\&quot; |  [optional] |
|**duration** | **String** | Describes the length of the media content without requiring a receiver to open the content. Formatted as an ISO 8601 Duration field. |  [optional] |
|**value** | **Object** | Supplementary parameter for this card |  [optional] |



