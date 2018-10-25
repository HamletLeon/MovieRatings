package com.hamletleon.moviewratings.models.movies

enum class MovieStatus(val value: String, val esValue: String) {
    Rumored("Rumored", "Rumoreada"),
    Planned("Planned", "Planificada"),
    InProduction("In Production", "En Producción"),
    PostProduction("Post Production", "Post Producción"),
    Released("Released", "Publicada"),
    Canceled("Canceled", "Cancelada")
}