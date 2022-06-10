package ensa.ebanking.accountservice.Helpers;

import ensa.ebanking.accountservice.Entities.Creancier;
import ensa.ebanking.accountservice.soap.request.creancierslist.CreanciersListResponse;
import org.springframework.stereotype.Component;

@Component
public class MappingHelper {
    public static void mapCreancier(Creancier creancier, CreanciersListResponse.Creancier creancierResponse) {
        creancierResponse.setCode(creancier.getCode());
        creancierResponse.setName(creancier.getName());
        creancierResponse.setCreancierCategory(creancier.getCreancierCategory().name());
    }
}
