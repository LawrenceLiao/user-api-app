package co.zip.candidate.userapi.mapper;

import co.zip.candidate.userapi.dto.client.ClientGetDto;
import co.zip.candidate.userapi.dto.client.ClientPostDto;
import co.zip.candidate.userapi.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientMapper {
    @Mapping(target = "name", source = "clientName")
    Client toEntity(ClientPostDto clientPostDto);

    @Mapping(target = "clientName", source = "name")
    ClientGetDto fromEntity(Client client);
}
