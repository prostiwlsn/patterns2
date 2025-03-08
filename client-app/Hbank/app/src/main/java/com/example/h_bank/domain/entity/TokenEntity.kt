import com.example.h_bank.data.dto.TokenDto

data class TokenEntity(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val expiresAt: Long? = null
) {
    fun isValid(): Boolean = accessToken != null && refreshToken != null && expiresAt != null
    fun isExpired(): Boolean = expiresAt?.let { System.currentTimeMillis() > it } ?: true

    companion object {
        fun fromTokenDto(dto: TokenDto): TokenEntity {
            return TokenEntity(
                accessToken = dto.accessToken,
                refreshToken = dto.refreshToken,
                expiresAt = dto.expiresIn?.let { System.currentTimeMillis() + it * 1000L }
            )
        }
    }
}