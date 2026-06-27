package com.logistic.edo.usecases.command;

/**
 * Команда для создания нового документа в системе ЭДО.
 * Содержит все данные, необходимые для создания документа.
 */
public record CreateDocumentCommand(String xmlContent, String companyId) {
}
