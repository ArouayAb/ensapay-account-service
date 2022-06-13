package ensa.ebanking.accountservice.Helpers;

import ensa.ebanking.accountservice.Entities.Creance;
import ensa.ebanking.accountservice.Entities.Creancier;
import ensa.ebanking.accountservice.Entities.ServiceProvider;
import ensa.ebanking.accountservice.soap.request.creanceslist.CreancesListResponse;
import ensa.ebanking.accountservice.soap.request.creancierslist.CreanciersListResponse;
import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Date;
import java.util.GregorianCalendar;

@Component
public class MappingHelper {

    public static void mapServiceProviderForCreanciersList(ServiceProvider serviceProvider, CreanciersListResponse.Creancier creancier) {
        CreanciersListResponse.Creancier.ServiceProvider serviceProviderRes = new CreanciersListResponse.Creancier.ServiceProvider();
        serviceProviderRes.setCode(serviceProvider.getCode());
        serviceProviderRes.setImage(serviceProvider.getImage());
        serviceProviderRes.setName(serviceProvider.getName());
        serviceProviderRes.setSurname(serviceProvider.getSurname());
        serviceProviderRes.setPhoneNumber(serviceProvider.getPhoneNumber());
        creancier.setServiceProvider(serviceProviderRes);
    }

    public static void mapCreancier(Creancier creancier, CreanciersListResponse.Creancier creancierResponse) {
        creancierResponse.setCode(creancier.getCode());
        creancierResponse.setName(creancier.getName());
        creancierResponse.setCreancierCategory(creancier.getCreancierCategory().name());
        MappingHelper.mapServiceProviderForCreanciersList(creancier.getServiceProvider(), creancierResponse);
    }

    public static void mapServiceProvidersForCreancesList(ServiceProvider serviceProvider,
                                                          CreancesListResponse.Creance.Creancier creancierRes) {
        CreancesListResponse.Creance.Creancier.ServiceProvider serviceProviderRes = new CreancesListResponse.Creance.Creancier.ServiceProvider();
        serviceProviderRes.setCode(serviceProvider.getCode());
        serviceProviderRes.setImage(serviceProvider.getImage());
        serviceProviderRes.setName(serviceProvider.getName());
        serviceProviderRes.setSurname(serviceProvider.getSurname());
        serviceProviderRes.setPhoneNumber(serviceProvider.getPhoneNumber());
        creancierRes.setServiceProvider(serviceProviderRes);
    }

    public static void mapCreancierForCreancesList(Creancier creancier,
                                                   CreancesListResponse.Creance creanceRes) {

        CreancesListResponse.Creance.Creancier creancierRes = new CreancesListResponse.Creance.Creancier();
        creancierRes.setCode(creancier.getCode());
        creancierRes.setName(creancier.getName());
        creancierRes.setCreancierCategory(creancier.getCreancierCategory().name());
        MappingHelper.mapServiceProvidersForCreancesList(creancier.getServiceProvider(),
                creancierRes);
        creanceRes.setCreancier(creancierRes);
    }

    public static void mapCreance(Creance creance, CreancesListResponse.Creance creanceRes) {
        try {
            creanceRes.setCode(creance.getCode());
            creanceRes.setCreanceStatus(creance.getCreanceStatus().name());

            GregorianCalendar gregory = new GregorianCalendar();
            gregory.setTime(Date.valueOf(creance.getDate().toLocalDate()));
            XMLGregorianCalendar calendar = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(
                            gregory);
            creanceRes.setDueDate(calendar);

            creanceRes.setAmount(creance.getAmount());
            MappingHelper.mapCreancierForCreancesList(creance.getCreancier(), creanceRes);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }

    }
}
