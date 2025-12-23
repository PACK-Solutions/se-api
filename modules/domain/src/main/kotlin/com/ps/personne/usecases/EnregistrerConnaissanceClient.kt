package com.ps.personne.usecases

import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.fold
import com.github.michaelbull.result.onSuccess
import com.github.michaelbull.result.recover
import com.ps.kommand.Command
import com.ps.kommand.CommandHandler
import com.ps.kommand.CommandResult
import com.ps.kommand.Context
import com.ps.personne.model.*
import com.ps.personne.ports.driven.ConnaissanceClientRepository

class EnregistrerConnnaissanceClientHandler(
    val connaissanceClientRepository: ConnaissanceClientRepository,
) : CommandHandler<EnregistrerConnnaissanceClientCommand> {
    override fun handle(context: Context, command: EnregistrerConnnaissanceClientCommand): CommandResult<IdPersonne, ConnaissanceClientError> {
        return connaissanceClientRepository.recuperer(command.idPersonne)
            .recover { ConnaissanceClient.vierge(command.idPersonne) }
            .andThen { it.mettreAJour(command.statutPPE, command.statutProchePPE, command.vigilance) }
            .onSuccess { connaissanceClientRepository.sauvegarder(it.first) }
            .fold(
                { CommandResult.Success(command.idPersonne, listOf(it.second)) },
                { CommandResult.Failure(it) },
            )
    }
}

data class EnregistrerConnnaissanceClientCommand(
    val idPersonne: IdPersonne,
    val statutPPE: ExpositionPolitique.Ppe?,
    val statutProchePPE: ExpositionPolitique.ProchePpe?,
    val vigilance: Vigilance,
) : Command<IdPersonne, ConnaissanceClientError>
