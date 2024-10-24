package dev.vivekchib.inshortsapp.domain.usecases

interface UseCase<P, T> {
    suspend operator fun invoke (param: P): T
}