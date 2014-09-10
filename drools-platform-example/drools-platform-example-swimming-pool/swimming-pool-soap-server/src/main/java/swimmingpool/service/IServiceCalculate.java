package swimmingpool.service;

import org.chtijbug.example.swimmingpool.Abonnement;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public interface IServiceCalculate {

    @WebMethod(operationName = "calculate")
    public abstract Abonnement calculate(Abonnement ticket);


}
