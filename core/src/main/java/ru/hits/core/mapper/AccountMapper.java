package ru.hits.core.mapper;

import org.mapstruct.Mapper;
import ru.hits.core.domain.entity.AccountEntity;
import ru.hits.core.domain.dto.account.AccountDTO;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDTO entityToDTO(AccountEntity accountEntity);

}
