
package bank.soap.jaxws.client;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "SoapBankImplService", targetNamespace = "http://soap.bank/", wsdlLocation = "http://localhost:9001/SoapBank?wsdl")
public class SoapBankImplService
    extends Service
{

    private final static URL SOAPBANKIMPLSERVICE_WSDL_LOCATION;
    private final static WebServiceException SOAPBANKIMPLSERVICE_EXCEPTION;
    private final static QName SOAPBANKIMPLSERVICE_QNAME = new QName("http://soap.bank/", "SoapBankImplService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:9001/SoapBank?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        SOAPBANKIMPLSERVICE_WSDL_LOCATION = url;
        SOAPBANKIMPLSERVICE_EXCEPTION = e;
    }

    public SoapBankImplService() {
        super(__getWsdlLocation(), SOAPBANKIMPLSERVICE_QNAME);
    }

    public SoapBankImplService(WebServiceFeature... features) {
        super(__getWsdlLocation(), SOAPBANKIMPLSERVICE_QNAME, features);
    }

    public SoapBankImplService(URL wsdlLocation) {
        super(wsdlLocation, SOAPBANKIMPLSERVICE_QNAME);
    }

    public SoapBankImplService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, SOAPBANKIMPLSERVICE_QNAME, features);
    }

    public SoapBankImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SoapBankImplService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns SoapBankImpl
     */
    @WebEndpoint(name = "SoapBankImplPort")
    public SoapBankImpl getSoapBankImplPort() {
        return super.getPort(new QName("http://soap.bank/", "SoapBankImplPort"), SoapBankImpl.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SoapBankImpl
     */
    @WebEndpoint(name = "SoapBankImplPort")
    public SoapBankImpl getSoapBankImplPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://soap.bank/", "SoapBankImplPort"), SoapBankImpl.class, features);
    }

    private static URL __getWsdlLocation() {
        if (SOAPBANKIMPLSERVICE_EXCEPTION!= null) {
            throw SOAPBANKIMPLSERVICE_EXCEPTION;
        }
        return SOAPBANKIMPLSERVICE_WSDL_LOCATION;
    }

}
