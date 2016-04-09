
package bank.soap.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "withdraw", namespace = "http://soap.bank/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "withdraw", namespace = "http://soap.bank/", propOrder = {
    "number",
    "amount"
})
public class Withdraw {

    @XmlElement(name = "number", namespace = "")
    private String number;
    @XmlElement(name = "amount", namespace = "")
    private double amount;

    /**
     * 
     * @return
     *     returns String
     */
    public String getNumber() {
        return this.number;
    }

    /**
     * 
     * @param number
     *     the value for the number property
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * 
     * @return
     *     returns double
     */
    public double getAmount() {
        return this.amount;
    }

    /**
     * 
     * @param amount
     *     the value for the amount property
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

}
