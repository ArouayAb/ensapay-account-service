//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.06.12 at 03:21:52 PM WEST 
//


package ensa.ebanking.accountservice.soap.request.creancierslist;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ensa.ebanking.accountservice.soap.request.creancierslist package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CreanciersListRequest_QNAME = new QName("http://www.ebanking.ensa/accountservice/Soap/Request/CreanciersList/", "CreanciersListRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ensa.ebanking.accountservice.soap.request.creancierslist
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CreanciersListResponse }
     * 
     */
    public CreanciersListResponse createCreanciersListResponse() {
        return new CreanciersListResponse();
    }

    /**
     * Create an instance of {@link CreanciersListResponse.Creancier }
     * 
     */
    public CreanciersListResponse.Creancier createCreanciersListResponseCreancier() {
        return new CreanciersListResponse.Creancier();
    }

    /**
     * Create an instance of {@link CreanciersListResponse.Creancier.ServiceProvider }
     * 
     */
    public CreanciersListResponse.Creancier.ServiceProvider createCreanciersListResponseCreancierServiceProvider() {
        return new CreanciersListResponse.Creancier.ServiceProvider();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ebanking.ensa/accountservice/Soap/Request/CreanciersList/", name = "CreanciersListRequest")
    public JAXBElement<Object> createCreanciersListRequest(Object value) {
        return new JAXBElement<Object>(_CreanciersListRequest_QNAME, Object.class, null, value);
    }

}