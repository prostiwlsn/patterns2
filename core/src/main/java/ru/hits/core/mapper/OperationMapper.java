package ru.hits.core.mapper;

import org.mapstruct.Mapper;
import ru.hits.core.domain.dto.operation.OperationDTO;
import ru.hits.core.domain.entity.OperationEntity;
import ru.hits.core.domain.dto.operation.OperationShortDTO;

@Mapper(componentModel = "spring")
public interface OperationMapper {

    OperationDTO entityToDTO(OperationEntity operationEntity);

    OperationShortDTO entityToShortDTO(OperationEntity operationEntity);

}
