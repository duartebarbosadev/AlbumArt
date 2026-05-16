package com.sample.core.network.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItunesRssResponseDto(
    val feed: FeedDto
)

@Serializable
data class FeedDto(
    val author: AuthorDto? = null,
    val entry: List<EntryDto> = emptyList(),
    val updated: LabelDto? = null,
    val rights: LabelDto? = null,
    val title: LabelDto? = null,
    val icon: LabelDto? = null,
    val link: List<LinkDto> = emptyList(),
    val id: LabelDto? = null
)

@Serializable
data class AuthorDto(
    val name: LabelDto? = null,
    val uri: LabelDto? = null
)

@Serializable
data class EntryDto(
    @SerialName("im:name")
    val name: LabelDto? = null,

    @SerialName("im:image")
    val images: List<ImageDto> = emptyList(),

    @SerialName("im:itemCount")
    val itemCount: LabelDto? = null,

    @SerialName("im:price")
    val price: PriceDto? = null,

    @SerialName("im:contentType")
    val contentType: ContentTypeDto? = null,

    val rights: LabelDto? = null,
    val title: LabelDto? = null,
    val link: LinkDto? = null,
    val id: IdDto? = null,

    @SerialName("im:artist")
    val artist: ArtistDto? = null,

    val category: CategoryDto? = null,

    @SerialName("im:releaseDate")
    val releaseDate: ReleaseDateDto? = null
)

@Serializable
data class LabelDto(
    val label: String? = null
)

@Serializable
data class ImageDto(
    val label: String? = null,
    val attributes: ImageAttributesDto? = null
)

@Serializable
data class ImageAttributesDto(
    val height: String? = null
)

@Serializable
data class PriceDto(
    val label: String? = null,
    val attributes: PriceAttributesDto? = null
)

@Serializable
data class PriceAttributesDto(
    val amount: String? = null,
    val currency: String? = null
)

@Serializable
data class ContentTypeDto(
    @SerialName("im:contentType")
    val innerContentType: InnerContentTypeDto? = null,

    val attributes: ContentTypeAttributesDto? = null
)

@Serializable
data class InnerContentTypeDto(
    val attributes: ContentTypeAttributesDto? = null
)

@Serializable
data class ContentTypeAttributesDto(
    val term: String? = null,
    val label: String? = null
)

@Serializable
data class LinkDto(
    val attributes: LinkAttributesDto? = null
)

@Serializable
data class LinkAttributesDto(
    val rel: String? = null,
    val type: String? = null,
    val href: String? = null
)

@Serializable
data class IdDto(
    val label: String? = null,
    val attributes: IdAttributesDto? = null
)

@Serializable
data class IdAttributesDto(
    @SerialName("im:id")
    val imId: String? = null
)

@Serializable
data class ArtistDto(
    val label: String? = null,
    val attributes: ArtistAttributesDto? = null
)

@Serializable
data class ArtistAttributesDto(
    val href: String? = null
)

@Serializable
data class CategoryDto(
    val attributes: CategoryAttributesDto? = null
)

@Serializable
data class CategoryAttributesDto(
    @SerialName("im:id")
    val imId: String? = null,

    val term: String? = null,
    val scheme: String? = null,
    val label: String? = null
)

@Serializable
data class ReleaseDateDto(
    val label: String? = null,
    val attributes: ReleaseDateAttributesDto? = null
)

@Serializable
data class ReleaseDateAttributesDto(
    val label: String? = null
)