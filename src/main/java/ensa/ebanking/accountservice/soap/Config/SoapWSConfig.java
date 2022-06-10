package ensa.ebanking.accountservice.soap.Config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@Configuration
@EnableWs
public class SoapWSConfig extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<MessageDispatcherServlet>(servlet, "/cmi/*");
    }


    @Bean(name = "createBankAccount")
    public DefaultWsdl11Definition defaultWsdl11Definition(@Qualifier("creationSchema") XsdSchema schema) {
        DefaultWsdl11Definition defaultWsdl11Definition = new DefaultWsdl11Definition();
        defaultWsdl11Definition.setPortTypeName("CMI");
        defaultWsdl11Definition.setLocationUri("/cmi/createAccount");
        defaultWsdl11Definition.setTargetNamespace("http://www.ebanking.ensa/accountservice/Soap/Request/AccountCreation");
        defaultWsdl11Definition.setSchema(schema);
        return defaultWsdl11Definition;
    }

    @Bean(name = "consultBankAccount")
    public DefaultWsdl11Definition defaultWsdl11Definition2(@Qualifier("consultSchema")XsdSchema schema) {
        DefaultWsdl11Definition defaultWsdl11Definition = new DefaultWsdl11Definition();
        defaultWsdl11Definition.setPortTypeName("CMI");
        defaultWsdl11Definition.setLocationUri("/cmi/consultAccount");
        defaultWsdl11Definition.setTargetNamespace("http://www.ebanking.ensa/accountservice/Soap/Request/AccountBalance");
        defaultWsdl11Definition.setSchema(schema);
        return defaultWsdl11Definition;
    }

    @Bean(name = "creanciersList")
    public DefaultWsdl11Definition defaultWsdl11Definition3(@Qualifier("creanciersListSchema")XsdSchema schema) {
        DefaultWsdl11Definition defaultWsdl11Definition = new DefaultWsdl11Definition();
        defaultWsdl11Definition.setPortTypeName("CMI");
        defaultWsdl11Definition.setLocationUri("/cmi/creanciersList");
        defaultWsdl11Definition.setTargetNamespace("http://www.ebanking.ensa/accountservice/Soap/Request/CreanciersList");
        defaultWsdl11Definition.setSchema(schema);
        return defaultWsdl11Definition;
    }

    @Bean(name = "creationSchema")
    public XsdSchema schema() {
        return new SimpleXsdSchema(new ClassPathResource("xmlAccountCreationSchemas.xsd"));
    }

    @Bean(name = "consultSchema")
    public XsdSchema schema2() {
        return new SimpleXsdSchema(new ClassPathResource("xmlAccountBalanceSchemas.xsd"));
    }

    @Bean(name = "creanciersListSchema")
    public XsdSchema schema3() {
        return new SimpleXsdSchema(new ClassPathResource("xmlCreanciersListSchemas.xsd"));
    }

}
