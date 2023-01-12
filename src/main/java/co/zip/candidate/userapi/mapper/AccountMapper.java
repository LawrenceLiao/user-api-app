package co.zip.candidate.userapi.mapper;

import co.zip.candidate.userapi.dto.account.AccountGetDto;
import co.zip.candidate.userapi.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {

    @Mapping(target = "clientName", source = "client.name")
    @Mapping(target = "clientEmail", source = "client.email")
    AccountGetDto fromEntity(Account account);
}
